package com.mht.mhtblogservice.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class SysLog implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String fullClassName; // 🚀 换成全类名
    private String methodName;
    private String method;
    private String args;
    private String result;        // 🚀 新增返回值
    private Long executionTime;
    private String status;
    private String errorMsg;
}
