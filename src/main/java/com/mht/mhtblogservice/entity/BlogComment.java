package com.mht.mhtblogservice.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class BlogComment implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String postId;
    private String author;
    private String text;
    private Date createTime;
}
