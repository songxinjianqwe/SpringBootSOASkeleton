package cn.sinjinsong.skeleton.util;

import cn.sinjinsong.skeleton.security.domain.JWTUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

/**
 * Created by SinjinSong on 2017/7/28.
 */
public class UserUtil {
    public static JWTUser toUser(Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        return (JWTUser) token.getPrincipal();
    }
}
