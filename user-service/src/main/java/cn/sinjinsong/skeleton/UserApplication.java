package cn.sinjinsong.skeleton;

import com.alibaba.rocketmq.remoting.exception.RemotingException;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by SinjinSong on 2017/9/21.
 */
@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan({"cn.sinjinsong"})
@Slf4j
public class UserApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        SpringApplication.run(SpringBootSkeletonApplication.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootSkeletonApplication.class);
    }
}
