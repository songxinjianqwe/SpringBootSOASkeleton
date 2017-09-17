package cn.sinjinsong.balance;


import cn.sinjinsong.BaseSpringTest;
import cn.sinjinsong.balance.domain.entity.BalanceDO;
import cn.sinjinsong.balance.mq.MQConsumer;
import cn.sinjinsong.balance.service.BalanceService;
import com.alibaba.rocketmq.client.QueryResult;
import com.alibaba.rocketmq.common.message.MessageExt;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
public class BaseTest extends BaseSpringTest {
	
	@Autowired
	private MQConsumer mqConsumer;
	
	@Autowired
	private BalanceService balanceService;
	
	@Test
	public void testSave() throws Exception {
		BalanceDO balance = new BalanceDO();
		balance.setUserId(1L);
		balance.setUsername("张三");
		balance.setAmount(5000d);
		balance.setUpdater(1L);
		balance.setUpdateTime(LocalDateTime.now());
		this.balanceService.insert(balance);
	}
	
	@Test
	public void testUpdate() throws Exception {
		BalanceDO balance = this.balanceService.selectByPrimaryKey(1L);
		balance.setAmount(balance.getAmount() + 1000d);
		balance.setUpdateTime(LocalDateTime.now());
		this.balanceService.updateByPrimaryKey(balance);
	}
	
	
	@Test
	public void test1() throws Exception {
		log.info("{}",this.mqConsumer);
		long end = System.currentTimeMillis();
		long begin = end - 60 * 1000 * 60 * 24;
		QueryResult qr = this.mqConsumer.queryMessage("topic_pay", "k8", 10, begin, end);
		List<MessageExt> list = qr.getMessageList();
		for(MessageExt me : list){
			Map<String, String> m = me.getProperties();
			log.info(m.keySet().toString());
			log.info(m.values().toString());
			log.info(me.toString());
			log.info("内容: " + new String(me.getBody(), "utf-8"));
			log.info("Prepared :" + me.getPreparedTransactionOffset());
		}
	}
}
