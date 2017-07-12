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
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }
}