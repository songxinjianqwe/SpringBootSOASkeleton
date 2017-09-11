package cn.sinjinsong.skeleton.config.mq;

import cn.sinjinsong.skeleton.exception.mq.RocketMQException;
import cn.sinjinsong.skeleton.mq.MessageListener;
import cn.sinjinsong.skeleton.mq.MessageProcessor;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Created by SinjinSong on 2017/9/10.
 */
@SpringBootConfiguration
@Slf4j
public class ConsumerConfig {
    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;
    @Value("${rocketmq.consumer.groupName}")
    private String groupName;
    @Value("${rocketmq.consumer.topic}")
    private String topic;
    @Value("${rocketmq.consumer.tag}")
    private String tag;
    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;

    @Autowired
    @Qualifier("messageProcessorImpl")
    private MessageProcessor messageProcessor;

    @Bean
    public DefaultMQPushConsumer getRocketMQConsumer() throws RocketMQException {
        if (StringUtils.isBlank(groupName)) {
            throw new RocketMQException("groupName is null !!!");
        }
        if (StringUtils.isBlank(namesrvAddr)) {
            throw new RocketMQException("namesrvAddr is null !!!");
        }
        if (StringUtils.isBlank(topic)) {
            throw new RocketMQException("topic is null !!!");
        }
        if (StringUtils.isBlank(tag)) {
            throw new RocketMQException("tag is null !!!");
        }
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        MessageListener messageListener = new MessageListener();
        messageListener.setMessageProcessor(messageProcessor);
        consumer.registerMessageListener(messageListener);
        try {
            consumer.subscribe(topic, this.tag);
            consumer.start();
            log.info("consumer is start !!! groupName:{},topic:{},namesrvAddr:{}", groupName, topic, namesrvAddr);
        } catch (MQClientException e) {
            log.error("consumer is start !!! groupName:{},topic:{},namesrvAddr:{}", groupName, topic, namesrvAddr, e);
            throw new RocketMQException(e);
        }
        return consumer;
    }
}
