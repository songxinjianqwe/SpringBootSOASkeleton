package cn.sinjinsong.skeleton.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Aspect
@Configuration
@Slf4j
public class LoggingAspect {
    //execution(* cn.sinjinsong.skeleton.service.*.*(..))
    @Pointcut("@within(org.springframework.stereotype.Service)||@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void declareJoinPointExpression() {
    }

    @Before("declareJoinPointExpression()")
    public void beforeMethod(JoinPoint joinPoint) {// 连接点
        Object[] args = joinPoint.getArgs();// 取得方法参数
        log.info("The method [ {} ] begins with Parameters: {}", joinPoint.getSignature(), Arrays.toString(args));
    }

    @AfterReturning(value = "declareJoinPointExpression()", returning = "result")
    public void afterMethodReturn(JoinPoint joinPoint, Object result) {
        log.info("The method [ {} ] ends with Result: {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "declareJoinPointExpression()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        log.error("Error happened in method: [ {} ]", joinPoint.getSignature());
        log.error("Parameters: {}", Arrays.toString(joinPoint.getArgs()));
        log.error("Exception StackTrace: {}", e);
    }
}
