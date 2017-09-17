package cn.sinjinsong.pay.service.impl;

import cn.sinjinsong.pay.dao.PayDOMapper;
import cn.sinjinsong.pay.domain.entity.PayDO;
import cn.sinjinsong.pay.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Created by SinjinSong on 2017/9/17.
 */
@Service
public class PayServiceImpl implements PayService {
    @Autowired
	private PayDOMapper payMapper;
	
	public PayDO selectByPrimaryKey(Long userId) {
		return this.payMapper.selectByPrimaryKey(userId);
	}
	
	public int insert(PayDO record)  {
		return this.payMapper.insert(record);
	}
	
	public int updateByPrimaryKey(PayDO record) {
		return this.payMapper.updateByPrimaryKey(record);
	}
	
	
	@Transactional
	public void updateAmount(PayDO record, String mode, double money) {
		if("IN".equals(mode)){
			record.setAmount(record.getAmount() + Math.abs(money));
		} else if("OUT".equals(mode)){
			record.setAmount(record.getAmount() - Math.abs(money));
		}
		record.setUpdateTime(LocalDateTime.now());
		this.updateByPrimaryKey(record);
		//失败测试：
		//int a = 1/0;
	}
	
	public void updateDetail(PayDO record, String detail) {
		record.setDetail(detail);
		this.updateByPrimaryKey(record);
	}
}
