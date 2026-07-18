package com.mht.mhtblogservice.exception;

import com.mht.mhtblogservice.entity.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j // 自动注入日志组件，方便在控制台和云端打印错误堆栈
@RestControllerAdvice // 🌟 核心注解：声明这是一个专门拦截控制层异常并返回 JSON 数据的切面组件
public class GlobalExceptionHandler {

    /**
     * 🚀 优先拦截可控的、自定义业务异常
     * 这样即使 Controller 抛出了异常，我们的 Aspect 也可以在环绕通知 (Around)
     * 的 catch 块中通过 `if (e instanceof BusinessException)` 来识别它，并照常记录日志
     */
    @ExceptionHandler(BusinessException.class)
    public ResultVo<String> handleBusinessException(BusinessException e) {
        // 业务异常属于预期内的情况，通常用 WARN 级别记录，不需要打印全量报错堆栈
        log.warn("📢 拦截到业务逻辑提示: {}", e.getMessage());
        return ResultVo.fail(e.getMessage());
    }


    /**
     * 1. 拦截自定义的业务异常 (BusinessException)
     * 当我们在业务中主动发现逻辑错误时，可以直接 throw new RuntimeException("错误原因");
     */
    @ExceptionHandler(RuntimeException.class)
    public ResultVo<String> handleRuntimeException(RuntimeException e) {
        log.error("💥 捕获到业务运行异常: ", e);
        // 将具体的错误信息优雅包装，返回给前端
        return ResultVo.fail(e.getMessage());
    }

    /**
     * 2. 拦截系统致命异常 (如空指针 Exception.class)
     * 用来兜底处理那些程序员没有预料到的未知代码崩溃 bug
     */
    @ExceptionHandler(Exception.class)
    public ResultVo<String> handleException(Exception e) {
        log.error("🚨 捕获到未知的系统严重崩溃: ", e);
        // 对外隐藏具体的代码内部堆栈报错，保护系统安全，返回友好提示
        return ResultVo.fail("系统正在开小差，请稍后再试！");
    }
}
