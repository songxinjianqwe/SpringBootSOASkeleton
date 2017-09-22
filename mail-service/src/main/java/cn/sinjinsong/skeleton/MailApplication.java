package cn.sinjinsong.skeleton;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by SinjinSong on 2017/9/22.
 */
@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan({"cn.sinjinsong"})
@Slf4j
public class MailApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MailApplication.class);
        app.setWebEnvironment(false);
        app.run(args);
    }


    @Override
    public void run(String... args) throws Exception {

    }
}
