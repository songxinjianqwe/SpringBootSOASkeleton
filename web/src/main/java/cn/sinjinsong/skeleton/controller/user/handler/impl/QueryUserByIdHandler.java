package cn.sinjinsong.skeleton.controller.user.handler.impl;

import cn.sinjinsong.skeleton.controller.user.handler.QueryUserHandler;
import cn.sinjinsong.skeleton.domain.entity.UserDO;
import cn.sinjinsong.skeleton.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by SinjinSong on 2017/4/27.
 */
@Component("QueryUserHandler.id")
public class QueryUserByIdHandler implements QueryUserHandler {
    @Reference(version="1.0.0")
    private UserService service;
    @Override
    public UserDO handle(String key) {
        return service.findById(Long.parseLong(key));
    }
}
