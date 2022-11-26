package com.zgnba.clos.aop;


import com.zgnba.clos.common.utils.Result;
import com.zgnba.clos.config.shiro.ThreadLocalToken;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 拦截所有Web方法返回值
 * 判断是否生成新令牌
 */
@Slf4j
@Aspect
@Component
public class TokenAspect {

    @Autowired
    private ThreadLocalToken threadLocalToken;

    @Pointcut("execution(public * com.zgnba.clos.controller.*.*(..))")
    public void aspect(){

    }

    @Around("aspect()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Result result = (Result) point.proceed();
        String token = threadLocalToken.getToken();
        if (token!=null){
            result.put("token", token);
            log.info("本地线程token提取成功, 并清除");
            threadLocalToken.clear();
        }
        return result;
    }
}
