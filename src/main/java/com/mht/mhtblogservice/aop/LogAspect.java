package com.mht.mhtblogservice.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mht.mhtblogservice.exception.BusinessException;
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
@Aspect    // 🌟 声明这是一个 AOP 切面类
@Component // 🌟 交给 Spring 容器统一管理
public class LogAspect {

    @Autowired
    private ObjectMapper objectMapper; // SpringBoot 自带的 JSON 转换工具，用来打印入参

    /**
     * 1. 定义切入点 (Pointcut)
     * 拦截 com.mht.mhtblogservice.controller 包及其子包下的所有类的所有方法
     */
    @Pointcut("execution(* com.mht.mhtblogservice.controller..*.*(..))")
    public void controllerPointcut() {
    }

    /**
     * 2. 编写环绕通知 (Around)
     * 核心逻辑：包裹住整个 Controller 的方法执行生命周期
     */
    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // ⏱️ 记录开始时间
        long startTime = System.currentTimeMillis();

        // 🌐 获取当前网络请求的上下文信息（URL、IP等）
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String url = "未知URL";
        String method = "未知方法";
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            url = request.getRequestURI();
            method = request.getMethod();
        }

        // 🏷️ 获取被拦截的 Java 类名和方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        // 获取前端传过来的真实入参参数
        String argsJson = Arrays.toString(joinPoint.getArgs());

        // 🛫 【前置增强】：在接口执行前，优雅打印进站日志
        log.info("🔔 [AOP日志监听] >>> 请求开始 | 路径: [{}] {} | 目标方法: {}.{} | 入参: {}",
                method, url, className, methodName, argsJson);

        Object result;
        try {
            // 🚀 【方法执行】：让 Controller 方法真正去跑业务（去调 Service、查数据库等）
            result = joinPoint.proceed();

            // 🛬 【后置增强】：方法正常 Return 成功，计算耗时
            long executionTime = System.currentTimeMillis() - startTime;
            log.info("🟢 [AOP日志监听] <<< 请求成功 | 路径: {} | 耗时: {}ms", url, executionTime);

            return result;

        } catch (Throwable e) {
            // 🚨 【异常增强】：如果 Controller 抛出了异常，切面在这里死死抓取它！
            long executionTime = System.currentTimeMillis() - startTime;

            // 精准人肉识别：看看是业务提示还是严重的系统故障
            if (e instanceof BusinessException) {
                log.warn("⚠️ [AOP日志监听] <<< 触发业务提示 | 路径: {} | 耗时: {}ms | 提示原因: {}",
                        url, executionTime, e.getMessage());
            } else {
                log.error("💥 [AOP日志监听] <<< 系统产生未知崩溃 | 路径: {} | 耗时: {}ms | 堆栈信息: ",
                        url, executionTime, e);
            }

            // 🌟 核心防坑死穴：记录完日志后，必须原封不动投掷出去！
            // 只有抛出去，外层的 @RestControllerAdvice 全局异常捕获层才能拿到它并给前端包装为标准的 ResultVo.fail()
            throw e;
        }
    }
}
