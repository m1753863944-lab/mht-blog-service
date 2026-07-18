package com.mht.mhtblogservice.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class ResultVo<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;    // 状态码：200表示成功，500表示失败
    private String message;  // 提示信息（如：发布成功、文章不存在）
    private T data;          // 后端真实的返回数据（可以是对象、列表等，没有则为null）

    // 快捷成功返回（不带数据）
    public static <T> ResultVo<T> success() {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(200);
        result.setMessage("success");
        return result;
    }

    // 快捷成功返回（带数据）
    public static <T> ResultVo<T> success(T data) {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    // 快捷失败返回
    public static <T> ResultVo<T> fail(String message) {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }
}
