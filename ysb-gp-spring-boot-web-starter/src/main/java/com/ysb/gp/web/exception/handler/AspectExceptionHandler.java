package com.ysb.gp.web.exception.handler;

import com.ysb.web.common.util.WWebUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author laosy
 * @date 2021/9/28
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AspectExceptionHandler implements InitializingBean {
    ExceptionHandlerExceptionResolver exceptionHandlerResolver;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public Object handlerThrowable(Exception e) throws Exception {
        try {
            ServletInvocableHandlerMethod method1 = getMethod(e);
            if (method1 != null) {
                ServletWebRequest webRequest = new ServletWebRequest(WWebUtils.getRequest(), WWebUtils.getResponse());
                return method1.invokeForRequest(webRequest, null, e);
            }
        } catch (Exception illegalAccessException) {
            log.error("异常处理器执行失败", illegalAccessException);
        }
        log.debug("全局异常捕获，消化不了 Exception ，抛出去", e);
        throw e;
    }

    /**
     * 拿取异常对应的 @ExceptionHandler 配置 不支持 接口内定义的 @ExceptionHandler
     * 
     * @param e
     * @return
     */
    private ServletInvocableHandlerMethod getMethod(Exception e) {
        for (Map.Entry<ControllerAdviceBean, ExceptionHandlerMethodResolver> entry : exceptionHandlerResolver
                .getExceptionHandlerAdviceCache().entrySet()) {
            ControllerAdviceBean advice = entry.getKey();
            if (advice.isApplicableToBeanType(null)) {
                ExceptionHandlerMethodResolver resolver = entry.getValue();
                Method method = resolver.resolveMethod(e);
                if (method != null) {
                    ServletInvocableHandlerMethod handlerMethod =
                            new ServletInvocableHandlerMethod(advice.resolveBean(), method);

                    if (exceptionHandlerResolver.getArgumentResolvers() != null) {
                        handlerMethod.setHandlerMethodArgumentResolvers(
                                exceptionHandlerResolver.getArgumentResolvers());
                    }
                    if (exceptionHandlerResolver.getReturnValueHandlers() != null) {
                        handlerMethod.setHandlerMethodReturnValueHandlers(
                                exceptionHandlerResolver.getReturnValueHandlers());
                    }
                    return handlerMethod;
                }
            }

        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (handlerExceptionResolver instanceof HandlerExceptionResolverComposite) {
            List<HandlerExceptionResolver> exceptionResolvers =
                    ((HandlerExceptionResolverComposite)handlerExceptionResolver).getExceptionResolvers();
            for (HandlerExceptionResolver exceptionResolver : exceptionResolvers) {
                if (exceptionResolver instanceof ExceptionHandlerExceptionResolver) {
                    exceptionHandlerResolver = (ExceptionHandlerExceptionResolver)exceptionResolver;
                }
            }

        }
    }

}
