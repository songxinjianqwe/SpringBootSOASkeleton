package cn.sinjinsong.pay.mq;


import cn.sinjinsong.pay.domain.entity.PayDO;
import cn.sinjinsong.pay.service.PayService;
import cn.sinjinsong.common.util.FastJsonConvert;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.LocalTransactionExecuter;
import com.alibaba.rocketmq.client.producer.LocalTransactionState;
import com.alibaba.rocketmq.common.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 执行本地事务，由客户端回调
 */


@Component
@Slf4j
public class TransactionExecutorImpl implements LocalTransactionExecuter {

    @Autowired
    private PayService payService;

    public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
        try {
            //Message Body
            JSONObject messageBody = FastJsonConvert.convertJSONToObject(new String(msg.getBody(), "utf-8"), JSONObject.class);
            //Transaction MapArgs
            Map<String, Object> mapArgs = (Map<String, Object>) arg;

            // --------------------IN PUT---------------------- //
            log.info("message body = {}", messageBody);
            log.info("message mapArgs = {}", mapArgs);
            log.info("message tag = {}", msg.getTags());
            // --------------------IN PUT---------------------- //

            //userId
            Long userId = messageBody.getLong("userId");
            //money
            double money = messageBody.getDouble("money");
            //mode
            String pay_mode = messageBody.getString("pay_mode");
            //pay
            PayDO pay = this.payService.selectByPrimaryKey(userId);
            //持久化数据
            //执行本地事务
            this.payService.updateAmount(pay, pay_mode, money);
            //成功通知MQ消息变更 该消息变为：<确认发送>
            return LocalTransactionState.COMMIT_MESSAGE;

        } catch (Exception e) {
            e.printStackTrace();
            //失败则不通知MQ 该消息一直处于：<暂缓发送>
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
    }
}