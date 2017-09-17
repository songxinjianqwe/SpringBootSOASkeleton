package cn.sinjinsong.balance.service;

import cn.sinjinsong.balance.domain.entity.BalanceDO;

/**
 * Created by SinjinSong on 2017/9/17.
 */
public interface BalanceService {
    int insert(BalanceDO record);
    BalanceDO selectByPrimaryKey(Long userId);
    int updateByPrimaryKey(BalanceDO record);
    void updateAmount(Long userId, String mode, double money);
}
