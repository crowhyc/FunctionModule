package com.javanewb.common.configuration.datasource;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * Description: com.javanewb.common.configuration.datasource
 * </p>
 * <p>
 * </p>
 *
 * @author Dean.Hwang
 */
@Aspect
@Slf4j
public class WriterReadDSAspector {


    @Before("@annotation(transactional)")
    public void before(JoinPoint joinPoint, Transactional transactional) {
        if (transactional.readOnly()) {
            DynamicDataSource.setReadOnly();
        }
        DynamicDataSource.setWriteRead();

    }

}
