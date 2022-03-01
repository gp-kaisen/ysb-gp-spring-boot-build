package com.ysb.gp.web.exception.handler;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author laosy
 * @date 2021/9/28
 */
@Aspect
@Component
@Order(120)
@RequiredArgsConstructor
public class ControllerExceptionInterceptor {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void mappingPointcut() {}

    @Pointcut("execution(* com.ysb.web.controller..*(..))")
    public void controllerPointcut() {}

    private final AspectExceptionHandler aspectExceptionHandler;

    /**
     * 拦截器具体实现
     */
    @Around("controllerPointcut() && mappingPointcut()")
    public Object intercept(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } catch (Exception ex) {
            return aspectExceptionHandler.handlerThrowable(ex);
        }
    }
}
