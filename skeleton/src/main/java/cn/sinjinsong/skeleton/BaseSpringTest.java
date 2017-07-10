package cn.sinjinsong.skeleton;


import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
/**
 * Created by SinjinSong on 2017/5/4.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=SpringBootSkeletonApplication.class)
@Transactional
public class BaseSpringTest {
    
}
