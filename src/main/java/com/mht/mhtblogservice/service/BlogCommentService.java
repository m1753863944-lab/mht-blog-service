package com.mht.mhtblogservice.service;

import com.mht.mhtblogservice.entity.BlogComment;
import com.mht.mhtblogservice.mapper.BlogCommentMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class BlogCommentService {

    @Autowired
    private BlogCommentMapper blogCommentMapper;

    public List<BlogComment> getComments(String postId) {
        return blogCommentMapper.selectByPostId(postId);
    }

    public boolean saveComment(BlogComment comment) {
        return blogCommentMapper.insertComment(comment) > 0;
    }
}
