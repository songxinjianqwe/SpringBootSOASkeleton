package cn.sinjinsong.pay;


import cn.sinjinsong.BaseSpringTest;
import cn.sinjinsong.pay.domain.entity.PayDO;
import cn.sinjinsong.pay.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
@Slf4j
public class BaseTest extends BaseSpringTest {
	
	@Autowired
	private PayService payService;
	
	@Test
	public void testSave() throws Exception {
		PayDO pay = new PayDO();
		pay.setUserId(1L);
		pay.setUsername("张三");
		pay.setAmount(5000d);
		pay.setDetail("0");
		pay.setUpdater(1L);
		pay.setUpdateTime(LocalDateTime.now());
		this.payService.insert(pay);
	}
	
	@Test
	public void testUpdate() throws Exception {
		PayDO pay = this.payService.selectByPrimaryKey(1L);
		log.info("{}",pay);
		pay.setAmount(pay.getAmount() - 1000d);
		pay.setUpdateTime(LocalDateTime.now());
		this.payService.updateByPrimaryKey(pay);
	}
}
