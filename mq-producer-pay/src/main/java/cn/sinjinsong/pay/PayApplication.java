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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PayApplication.class);
    }
}
