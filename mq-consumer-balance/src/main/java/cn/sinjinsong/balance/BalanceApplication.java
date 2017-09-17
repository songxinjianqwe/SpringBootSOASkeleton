package cn.sinjinsong.balance;

import cn.sinjinsong.balance.mq.MQConsumer;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan({"cn.sinjinsong"})
@Slf4j
public class BalanceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        ConfigurableApplicationContext context = SpringApplication.run(BalanceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BalanceApplication.class);
    }
}
