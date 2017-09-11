package cn.sinjinsong.skeleton.mq;

import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * Created by SinjinSong on 2017/9/10.
 */
public interface MessageProcessor {
    /**
     * 处理消息的接口
     * @param messageExt
     * @return
     */
    public boolean handleMessage(MessageExt messageExt);
}