package com.mht.mhtblogservice.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mht.mhtblogservice.entity.SysLog;
import com.mht.mhtblogservice.exception.BusinessException;
import com.mht.mhtblogservice.service.SysLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private ObjectMapper objectMapper; // 🚀 用来将后端返回的 ResultVo 序列化为 JSON 字符串保存

    @Pointcut("execution(* com.mht.mhtblogservice.controller..*.*(..))")
    public void controllerPointcut() {}

    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String method = "未知方法";
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            method = request.getMethod();
        }

        // 🚀 核心改动 1：获取包含完整包路径的全类名（形如 com.mht.mhtblogservice.controller.BlogController）
        String fullClassName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        String argsJson = Arrays.toString(joinPoint.getArgs());

        // 提前组装日志基础数据
        SysLog sysLog = new SysLog();
        sysLog.setFullClassName(fullClassName);
        sysLog.setMethodName(methodName);
        sysLog.setMethod(method);
        sysLog.setArgs(argsJson.length() > 500 ? argsJson.substring(0, 500) : argsJson);

        Object result;
        try {
            // 执行真正的核心业务逻辑
            result = joinPoint.proceed();

            long executionTime = System.currentTimeMillis() - startTime;
            sysLog.setExecutionTime(executionTime);
            sysLog.setStatus("SUCCESS");

            // 🚀 核心改动 2：将后端吐出的 ResultVo 返回值对象完美转为 JSON 文本落库
            if (result != null) {
                String resultJson = objectMapper.writeValueAsString(result);
                sysLog.setResult(resultJson.length() > 1000 ? resultJson.substring(0, 1000) : resultJson); // 防超长字段
            }

            // 扔给后台虚拟线程默默写入 SQLPub
            sysLogService.saveLog(sysLog);
            return result;

        } catch (Throwable e) {
            long executionTime = System.currentTimeMillis() - startTime;
            sysLog.setExecutionTime(executionTime);
            sysLog.setErrorMsg(e.getMessage());

            if (e instanceof BusinessException) {
                sysLog.setStatus("WARN");
            } else {
                sysLog.setStatus("ERROR");
            }

            // 异常时也会将错误信息和入参数据异步落库
            sysLogService.saveLog(sysLog);
            throw e;
        }
    }
}
