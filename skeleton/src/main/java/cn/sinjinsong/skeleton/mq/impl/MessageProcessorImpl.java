package cn.sinjinsong.skeleton.mq.impl;

import cn.sinjinsong.skeleton.mq.MessageProcessor;
import com.alibaba.rocketmq.common.message.MessageExt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by SinjinSong on 2017/9/10.
 */
@Component
@Slf4j
public class MessageProcessorImpl implements MessageProcessor {
    @Override
    public boolean handleMessage(MessageExt messageExt) {
        log.info("receive : {}",messageExt);
        return true;
    }
}