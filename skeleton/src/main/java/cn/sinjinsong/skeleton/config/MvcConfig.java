package cn.sinjinsong.skeleton.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by SinjinSong on 2017/7/12.
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    /**
     * 访问localhost:8080时就相当于访问localhost:8080/templates/index.html
     * 也可以把index.html放在/static   or /pulic or /resources or /META-INF/resources 下，这样就不用加这个配置了
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }
}