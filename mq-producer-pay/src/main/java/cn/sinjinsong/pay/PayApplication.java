package cn.sinjinsong.pay;

import cn.sinjinsong.common.properties.CharsetProperties;
import cn.sinjinsong.common.util.FastJsonConvert;
import cn.sinjinsong.pay.mq.MQProducer;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.LocalTransactionExecuter;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan({"cn.sinjinsong"})
@Slf4j
public class PayApplication extends SpringBootServletInitializer {
    
    public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        ConfigurableApplicationContext context = SpringApplication.run(PayApplication.class, args);
        MQProducer mqProducer = context.getBean(MQProducer.class);
        LocalTransactionExecuter transactionExecutorImpl = context.getBean(LocalTransactionExecuter.class);
        try {
            //构造消息数据
            Message message = new Message();
            //主题
            message.setTopic("pay");
            //子标签
            message.setTags("tag");
            //key
            String uuid = UUID.randomUUID().toString();
            log.info("key: {}", uuid);
            message.setKeys(uuid);
            JSONObject body = new JSONObject();
            body.put("userId", "1");
            body.put("money", "1000");
            body.put("pay_mode", "OUT");
            body.put("balance_mode", "IN");
            message.setBody(FastJsonConvert.convertObjectToJSON(body).getBytes(CharsetProperties.charset));

            //添加参数
            //这个参数可以在transactionExecutorImpl中使用
            Map<String, Object> transactionMapArgs = new HashMap<String, Object>();
            mqProducer.sendTransactionMessage(message, transactionExecutorImpl, transactionMapArgs);
            
            //1、构造消息
            //2、发送PrepareMessage
            //3、执行transactionExecutorImpl的executeLocalTransactionBranch方法
            //本方法中执行本地事务
            //4、如果事务执行成功，该方法返回COMMIT_MESSAGE。
            //   执行失败，返回ROLLBACK_MESSAGE
            //5、返回COMMIT_MESSAGE时，会将预消息发送出去
            //   返回ROLLBACK_MESSAGE，会取消该消息的发送
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PayApplication.class);
    }
}
