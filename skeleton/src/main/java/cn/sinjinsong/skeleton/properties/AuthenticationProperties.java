package cn.sinjinsong.skeleton.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by SinjinSong on 2017/4/27.
 */
@Component
@ConfigurationProperties(ignoreUnknownFields = false, prefix = "token",locations = "classpath:token.properties")
@Setter
@Getter
public class AuthenticationProperties {
    private String secretKey;
    private Integer expireTime;
    private Integer captchaExpireTime;
    private Integer activationCodeExpireTime;
    private Integer forgetNameExpireTime;

    public static final String AUTH_HEADER = "Authentication";
    public static final String USER_ID = "id";
    public static final String LOGIN_URL = "/tokens";
    public static final String EXCEPTION_ATTR_NAME = "BaseRESTException";

    public Integer getCaptchaExpireTime() {
        return captchaExpireTime;
    }

    public void setCaptchaExpireTime(Integer captchaExpireTime) {
        this.captchaExpireTime = captchaExpireTime;
    }

    public Integer getActivationCodeExpireTime() {
        return activationCodeExpireTime;
    }

    public void setActivationCodeExpireTime(Integer activationCodeExpireTime) {
        this.activationCodeExpireTime = activationCodeExpireTime;
    }


    public Integer getForgetNameExpireTime() {
        return forgetNameExpireTime;
    }

    public void setForgetNameExpireTime(Integer forgetNameExpireTime) {
        this.forgetNameExpireTime = forgetNameExpireTime;
    }

    public Integer getExpireTime() {
        return expireTime;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
