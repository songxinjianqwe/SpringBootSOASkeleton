package cn.sinjinsong.skeleton.service.impl;

import cn.sinjinsong.skeleton.dao.RoleDOMapper;
import cn.sinjinsong.skeleton.dao.UserDOMapper;
import cn.sinjinsong.skeleton.domain.entity.UserDO;
import cn.sinjinsong.skeleton.enumeration.UserStatus;
import cn.sinjinsong.skeleton.service.UserService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by SinjinSong on 2017/4/27.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private RoleDOMapper roleDOMapper;
        
    @Override
    @Cacheable("UserDO")
    @Transactional(readOnly = true)
    public UserDO findByUsername(String username) {
        return userDOMapper.findByUsername(username);
    }

    @Override
    @Cacheable("UserDO")
    @Transactional(readOnly = true)
    public UserDO findByPhone(String phone) {
        return userDOMapper.findByPhone(phone);
    }

    @Override
    @Cacheable("UserDO")
    @Transactional(readOnly = true)
    public UserDO findById(Long id) {
        return userDOMapper.selectByPrimaryKey(id);
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "UserDO",allEntries = true)
    public void save(UserDO userDO) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //对密码进行加密
        userDO.setPassword(passwordEncoder.encode(userDO.getPassword()));
        userDO.setRegTime(LocalDateTime.now());
        //设置用户状态为未激活
        userDO.setUserStatus(UserStatus.UNACTIVATED);
        userDOMapper.insert(userDO);
        //添加用户的角色，每个用户至少有一个user角色
        long roleId = roleDOMapper.findRoleIdByRoleName("ROLE_USER");
        roleDOMapper.insertUserRole(userDO.getId(),roleId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "UserDO",allEntries = true)
    public void update(UserDO userDO) {
        userDOMapper.updateByPrimaryKeySelective(userDO);
    }
    
    @Override
    @Transactional
    @CacheEvict(value = "UserDO",allEntries = true)
    public void resetPassword(Long id,String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserDO userDO = new UserDO();
        userDO.setId(id);
        userDO.setPassword(passwordEncoder.encode(newPassword));
        userDOMapper.updateByPrimaryKeySelective(userDO);
    }

    @Override
    public List<Long> findAllUserIds() {
        return userDOMapper.findAllUserIds();
    }


    @Override
    public PageInfo<UserDO> findAll(int pageNum, int pageSize) {
        return userDOMapper.findAll(pageNum,pageSize).toPageInfo();
    }

    @Override
    public String findAvatarById(Long id) {
        return userDOMapper.findAvatarById(id);
    }

    @Override
    public UserDO findByEmail(String email) {
        return userDOMapper.findByEmail(email);
    }
}
