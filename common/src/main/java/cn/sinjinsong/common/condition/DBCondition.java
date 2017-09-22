package cn.sinjinsong.common.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by SinjinSong on 2017/9/22.
 */
public class DBCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getEnvironment().getProperty("mybatis.mapper-locations") != null &&
                context.getEnvironment().getProperty("mybatis.config-location") != null;
    }
}
