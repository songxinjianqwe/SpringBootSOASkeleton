package cn.sinjinsong.pay.service;

import cn.sinjinsong.pay.domain.entity.PayDO;

public interface PayService {

    PayDO selectByPrimaryKey(Long userId);

    int insert(PayDO record);

    int updateByPrimaryKey(PayDO record);

    void updateAmount(PayDO record, String mode, double money);

    void updateDetail(PayDO record, String detail);

}
