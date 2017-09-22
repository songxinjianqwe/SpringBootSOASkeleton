package cn.sinjinsong.skeleton.converter;

import cn.sinjinsong.skeleton.domain.dto.JWTUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

/**
 * Created by SinjinSong on 2017/7/28.
 */
public class UserConverter {
    public static JWTUser toUser(Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        return (JWTUser) token.getPrincipal();
    }
}
