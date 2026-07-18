package com.mht.mhtblogservice.entity;

import lombok.Data;
import java.io.Serializable;

@Data // 自动生成全套 Getter/Setter/toString
public class Article implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String content;
}
