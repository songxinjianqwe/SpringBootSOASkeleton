package cn.sinjinsong.skeleton.config.web;

import cn.sinjinsong.skeleton.exception.token.TokenStateInvalidException;
import cn.sinjinsong.skeleton.properties.AuthenticationProperties;
import cn.sinjinsong.skeleton.security.domain.TokenCheckResult;
import cn.sinjinsong.skeleton.security.token.TokenManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;


@Configuration
@EnableWebSocketMessageBroker
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenManager tokenManager;


    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        //对应客户端代码：new SockJS('/endpoint');
        //客户端只在new连接时使用，其他时候都不涉及endpoint，类似于appName
        stompEndpointRegistry
                .addEndpoint("/endpoint")
                .setAllowedOrigins("*").withSockJS()
                .setSessionCookieNeeded(false);
    }

    /**
     * config.enableSimpleBroker("/topic","/user");这句表示在topic和user这两个域上可以向客户端发消息；
     * config.setUserDestinationPrefix("/user/");这句表示给指定用户发送（一对一）的主题前缀是“/user/”;
     * config.setApplicationDestinationPrefixes("/app"); 这句表示客户端向服务端发送时的主题上面需要加"/app"作为前缀；
     *
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //一个用于消息推送，一个用于即时通讯
        //topic是1-n，queue是1-1
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user/");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(new ChannelInterceptorAdapter() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader(AuthenticationProperties.AUTH_HEADER);
                    log.info("{}", token);
                    TokenCheckResult result = tokenManager.checkToken(token);
                    if (result.isValid()) {
                        log.info("checking authentication {}", result);
                        UserDetails userDetails = userDetailsService.loadUserByUsername(result.getUsername());
                        //如果未登录
                        if (SecurityContextHolder.getContext().getAuthentication() == null) {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            log.info("authenticated user {} ,setting security context", result);
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                        accessor.setUser(SecurityContextHolder.getContext().getAuthentication());
                    }else{
                        log.info("Token无效：{}",token);
                        throw new TokenStateInvalidException(token);
                    }
                }
                return message;
            }
        });
    }
}

