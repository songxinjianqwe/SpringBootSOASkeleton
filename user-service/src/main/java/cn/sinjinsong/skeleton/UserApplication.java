package cn.sinjinsong.skeleton;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import java.io.IOException;

/**
 * Created by SinjinSong on 2017/9/21.
 */
@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan({"cn.sinjinsong"})
@ImportResource("classpath:dubbo.xml")
@Slf4j
public class UserApplication implements CommandLineRunner {

    public static void main(String[] args) throws IOException {
        SpringApplication app = new SpringApplication(UserApplication.class);
        app.setWebEnvironment(false);
        app.run(args);
        synchronized (UserApplication.class) {
            while (true) {
                try {
                    UserApplication.class.wait();
                } catch (Throwable e) {
                }
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
