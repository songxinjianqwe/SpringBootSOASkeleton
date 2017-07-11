package cn.sinjinsong.skeleton.service.user;

import cn.sinjinsong.skeleton.BaseSpringTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * Created by SinjinSong on 2017/7/10.
 */
@Slf4j
public class UserServiceTest extends BaseSpringTest{
    @Autowired
    private UserService userService;
    @Autowired
    private MessageSource ms;
    
    @Test
    public void findByUsername() throws Exception {
//        UserDO user = userService.findByUsername("admin");
//        System.out.println(user);
//        log.info("user:{} ",user);
        String message = ms.getMessage("i18n.TokenStateInvalid", null, Locale.getDefault());
        log.info("message:{}",message);
    }
    

    @Test
    public void findByPhone() throws Exception {
    }

    @Test
    public void findById() throws Exception {
    }

    @Test
    public void save() throws Exception {
    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void findAll() throws Exception {
    }

    @Test
    public void findAvatarById() throws Exception {
    }

    @Test
    public void findByEmail() throws Exception {
    }

    @Test
    public void resetPassword() throws Exception {
    }

}