package cn.sinjinsong.skeleton.controller.chat;

import cn.sinjinsong.common.util.DateTimeUtil;
import cn.sinjinsong.skeleton.converter.UserUtil;
import cn.sinjinsong.skeleton.domain.dto.chat.Greeting;
import cn.sinjinsong.skeleton.security.domain.JWTUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @sendTo和convertAndSend和convertAndSendToUser 的前缀一定是某一个brokerPrefix
 * 客户端send发送消息：如果是要与服务器打交道（单聊，群聊），那么以app为前缀（applicationPrefix）
 * 如果是直接发送给其他客户端，不与服务器打交道，那么同subscribe
 */
@Controller
@Slf4j
public class WsController {
    @Autowired
    private SimpMessagingTemplate template;

    /**
     * 表示服务端可以接收客户端通过主题“/app/hello”发送过来的消息，
     * 客户端需要在主题"/topic/hello"上监听并接收服务端发回的消息
     * 动态发送
     * template.convertAndSend("/topic/hello",greeting) //广播
     * template.convertAndSendToUser(userId, "/message",userMessage) //一对一发送，发送特定的客户端
     *
     * @param message
     * @return
     * @throws Exception
     * @Payload可加可不加，如要获取用户信息，则加上Principal
     */
    @MessageMapping("/hello") // send(appPrefix(/app)+/hello)
    @SendTo("/topic/greetings") // subscribe(/topic/greetings)
    public Greeting broadcast(Principal principal, @Payload @Validated Greeting message) throws Exception {
        JWTUser user = UserUtil.toUser(principal);
        message.setBody(user.getUsername() + ":" + message.getBody());
        log.info("群聊：{}", message.getBody());
        //从一个客户端接收到消息后，转发给所有客户端
        return message;
    }

    /**
     * 单聊
     *
     * @param username
     * @param message
     * @return
     * @throws Exception
     */
    @MessageMapping("/queue/chat/{username}") // send(appPrefix(/app) + /queue/chat/{username}
    public void chatSingle(Principal principal, @DestinationVariable("username") String username, @Payload Greeting message) throws Exception {
        JWTUser user = UserUtil.toUser(principal);
        message.setBody(user.getUsername() + ":" + message.getBody());
        log.info("单聊：{}", message.getBody());
        //subscribe(/user/queue/chat)
        template.convertAndSendToUser(username, "/queue/chat", message);
    }
    
    @MessageExceptionHandler
    //subscribe(/user/queue/error)
    @SendToUser(value = "/queue/error", broadcast = false)
    public String handleException(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        if (!bindingResult.hasErrors()) {
            return "未知错误";
        }
        List<FieldError> allErrors = bindingResult.getFieldErrors();
        return "JSR 303 错误: " + allErrors.iterator().next().getDefaultMessage();
    }
    

    /**
     * 这里是设置了一个定时任务
     * 如果是实时更新数据的话，应该是在更新数据的地方调用template.convertAndSend，最好不要使用@Scheduled，
     * 因为这样子就和ajax轮询差不多了，效率很低
     * 可以用在定时消息推送中
     */
    @Scheduled(fixedRate = 1000)
    public void broadcast() {
        this.template.convertAndSend("/topic/hotspot", DateTimeUtil.formatLocalDateTime(LocalDateTime.now()));
    }

    /**
     * 服务器的其他Controller可以调用此方法，会将消息发送给所有客户端（广播）
     * 也可以在某个Controller中注入template，然后使用template的convertAndSend方法，不必非要把与WebSocket有关的代码
     * 都写到当前Controller中
     * 比如某个Controller中调用了一个service的更新某哥数据的方法，那么controller最好是先调用service的相应方法，
     * 然后调用template.convertAndSend，这样就可以实时动态刷新了
     *
     * @param message
     */
    public void broadcast(Greeting message) {
        this.template.convertAndSend("/topic/greetings", message);
    }
}
