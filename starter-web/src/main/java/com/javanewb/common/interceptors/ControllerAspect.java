package com.javanewb.common.interceptors;

import com.javanewb.common.filter.LoggerMDCFilter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * Created by Dean.Hwang on 16/11/25.
 */
@Aspect
@Component
public class ControllerAspect {

    private Logger log = LoggerFactory.getLogger("MVCLOG");

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void classpoint() {
        //占位
    }

    /**
     * 环绕通知 记录用户请求的方法,参数,返回的数据
     */
    @Around(value = "classpoint() && args(object,..) ")
    public Object wrapperController(ProceedingJoinPoint pj, Object object) throws Throwable {
        log.info("Execute Method {}: [{}] - Method Params: [{}]", MDC.get(LoggerMDCFilter.IDENTIFIER), pj.getSignature(), object);
        return pj.proceed();
    }

}
