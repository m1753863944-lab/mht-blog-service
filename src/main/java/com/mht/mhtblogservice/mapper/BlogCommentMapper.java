package com.mht.mhtblogservice.mapper;

import com.mht.mhtblogservice.entity.BlogComment;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BlogCommentMapper {
    // 1. 根据文章 ID 拉取留言
    List<BlogComment> selectByPostId(String postId);

    // 2. 插入新留言
    int insertComment(BlogComment comment);
}
