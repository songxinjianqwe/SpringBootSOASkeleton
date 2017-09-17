package cn.sinjinsong.balance.mq;

import cn.sinjinsong.balance.service.BalanceService;
import cn.sinjinsong.common.util.FastJsonConvert;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.QueryResult;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MQConsumer {
	
	private final String GROUP_NAME = "transaction-balance";
	private final String NAMESRV_ADDR = "192.168.1.113:9876;192.168.1.114:9876";
	private DefaultMQPushConsumer consumer;
	
	@Autowired
	private BalanceService balanceService;
	
	
	public MQConsumer() {
		try {
			this.consumer = new DefaultMQPushConsumer(GROUP_NAME);
			this.consumer.setNamesrvAddr(NAMESRV_ADDR);
			this.consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
			this.consumer.subscribe("pay", "*");
			this.consumer.registerMessageListener(new PayMessageListener());
			this.consumer.start();
			log.info("consumer start");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public QueryResult queryMessage(String topic, String key, int maxNum, long begin, long end) throws Exception {
		long current = System.currentTimeMillis();
		return this.consumer.queryMessage(topic, key, maxNum, begin, end);
	}
	
	class PayMessageListener implements MessageListenerConcurrently {
		public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
			MessageExt msg = msgs.get(0);
			try {
				String topic = msg.getTopic();
				//Message Body
				JSONObject messageBody = FastJsonConvert.convertJSONToObject(new String(msg.getBody(), "utf-8"), JSONObject.class);
				String tags = msg.getTags();
				String keys = msg.getKeys();
				
				log.info("balance服务收到消息, keys : " + keys + ", body : " + new String(msg.getBody(), "utf-8"));
				//userid
				Long userId = messageBody.getLong("userId");
				//money
				double money = messageBody.getDouble("money");
				//mode
				String balance_mode = messageBody.getString("balance_mode");
				//业务逻辑处理
				balanceService.updateAmount(userId, balance_mode, money);
				
			} catch (Exception e) {	
				e.printStackTrace();
				//重试次数为3情况 
				if(msg.getReconsumeTimes() == 3){
				    log.info("客户端重试三次");
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
					//记录日志
				}
				log.info("消费失败");
				return ConsumeConcurrentlyStatus.RECONSUME_LATER;
			}			
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
	}
}
