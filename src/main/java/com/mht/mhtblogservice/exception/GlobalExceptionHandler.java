package com.mht.mhtblogservice.exception;

import com.mht.mhtblogservice.entity.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. 拦截自定义的业务提示 (BusinessException)
     * 属于预期内的逻辑（如文章本身不存在），直接把提示字眼扔给前端展示即可
     */
    @ExceptionHandler(BusinessException.class)
    public ResultVo<String> handleBusinessException(BusinessException e) {
        log.warn("📢 [业务拦截] 触发逻辑提示 -> {}", e.getMessage());
        return ResultVo.fail(e.getMessage());
    }

    /**
     * 2. 🌟 拦截系统崩溃异常 (RuntimeException.class)
     * 包括你刚才遇到的 SQL 语法错误、空指针等。
     * 核心：在控制台打印现场方便你修 Bug，但给前端只返回友好脱敏的提示！
     */
    @ExceptionHandler(RuntimeException.class)
    public ResultVo<String> handleRuntimeException(RuntimeException e) {
        // 把真实的满屏幕红字和堆栈死死锁在后端控制台，保护数据库隐私
        log.error("💥 [系统级崩溃拦截] 错误类型: {} | 原因: ", e.getClass().getName(), e);

        // 🚀 温柔翻译：对外统一口径，给前端一个体面的交代
        return ResultVo.fail("云端数据交互失败，请检查输入格式或稍后再试！");
    }

    /**
     * 3. 拦截最终未知的系统致命错误 (Exception.class)
     */
    @ExceptionHandler(Exception.class)
    public ResultVo<String> handleException(Exception e) {
        log.error("🚨 [终极未知异常拦截] 崩溃原因: ", e);
        return ResultVo.fail("服务器开小差了，请联系管理员 MHT！");
    }
}
