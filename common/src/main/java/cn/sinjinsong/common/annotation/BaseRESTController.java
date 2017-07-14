package cn.sinjinsong.common.annotation;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * Created by SinjinSong on 2017/7/13.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@CrossOrigin
@RestController
public @interface BaseRESTController {
    String value() default "";
}
