package cn.sinjinsong.balance.service.impl;

import cn.sinjinsong.balance.dao.BalanceDOMapper;
import cn.sinjinsong.balance.domain.entity.BalanceDO;
import cn.sinjinsong.balance.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BalanceServiceImpl implements BalanceService {

	@Autowired
	private BalanceDOMapper balanceMapper;
	
	public int insert(BalanceDO record){
		return this.balanceMapper.insert(record);
	}
	
	public BalanceDO selectByPrimaryKey(Long userId){
		return this.balanceMapper.selectByPrimaryKey(userId);
	}
	
	public int updateByPrimaryKey(BalanceDO record){
		return this.balanceMapper.updateByPrimaryKey(record);
	}
	
	@Transactional
	public void updateAmount(Long userId, String mode, double money) {
		BalanceDO BalanceDO = this.selectByPrimaryKey(userId);
		if("IN".equals(mode)){
			BalanceDO.setAmount(BalanceDO.getAmount() + Math.abs(money));
		} else if("OUT".equals(mode)){
			BalanceDO.setAmount(BalanceDO.getAmount() - Math.abs(money));
		}
		BalanceDO.setUpdateTime(LocalDateTime.now());
		this.updateByPrimaryKey(BalanceDO);
	}
	
}
