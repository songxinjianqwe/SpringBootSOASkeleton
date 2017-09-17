package cn.sinjinsong.pay;


import cn.sinjinsong.BaseSpringTest;
import cn.sinjinsong.common.properties.CharsetProperties;
import cn.sinjinsong.pay.mq.MQProducer;
import cn.sinjinsong.common.util.FastJsonConvert;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.QueryResult;
import com.alibaba.rocketmq.client.producer.LocalTransactionExecuter;
import com.alibaba.rocketmq.client.producer.LocalTransactionState;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class PayTest extends BaseSpringTest {


    @Autowired
    private MQProducer mqProducer;

    @Autowired
    private LocalTransactionExecuter transactionExecutorImpl;

    @Test
    public void test1() {
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

            this.mqProducer.sendTransactionMessage(message, this.transactionExecutorImpl, transactionMapArgs);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test2() throws Exception {
        long end = System.currentTimeMillis();
        long begin = end - 60 * 1000 * 60 * 24;
        QueryResult qr = this.mqProducer.queryMessage("pay", "6e4cefbb-5216-445f-9027-d96dd54a4afb", 10, begin, end);
        List<MessageExt> list = qr.getMessageList();
        for (MessageExt me : list) {
            Map<String, String> m = me.getProperties();
            log.info(m.keySet().toString());
            log.info(m.values().toString());
            log.info(me.toString());
            log.info("内容: " + new String(me.getBody(), "utf-8"));
            log.info("Prepared :" + me.getPreparedTransactionOffset());
            LocalTransactionState ls = this.mqProducer.check(me);
            log.info("{}", ls);
            //this.mqProducer.getTransactionCheckListener()
        }
        //System.out.println("qr: " + qr.toString());
        //C0A8016F00002A9F0000000000034842
    }
}
