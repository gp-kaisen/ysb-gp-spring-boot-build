package com.ysb.gp.logger;

import java.lang.reflect.Method;

import com.ysb.logger.ms.CompositeMsLogProcessor;
import com.ysb.logger.ms.MsLog;

import org.springframework.scheduling.annotation.Async;

/**
 * 空微服务日志记录处理器，用于屏蔽原微服务日志记录处理器
 */
public class NoneCompositeMsLogProcessor extends CompositeMsLogProcessor {

    public NoneCompositeMsLogProcessor() {
        super(null, null);
    }

    @Async("logAsync")
    @Override
    public void process(MsLog msLog, Method method) {}
}
