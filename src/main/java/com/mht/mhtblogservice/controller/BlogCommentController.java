package com.mht.mhtblogservice.controller;

import com.mht.mhtblogservice.entity.ResultVo;
import com.mht.mhtblogservice.entity.BlogComment;
import com.mht.mhtblogservice.exception.BusinessException; // 🚀 引入您的自定义异常
import com.mht.mhtblogservice.service.BlogCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/blog")
@CrossOrigin // 允许 Vue3 跨域无阻直调
public class BlogCommentController {

    @Autowired
    private BlogCommentService blogCommentService;

    /**
     * 1. 获取指定文章的留言列表
     * 异常直接向上抛出，交由全局异常处理器统一组装成 ResultVo 返回
     */
    @GetMapping("/{id}/comments")
    public ResultVo<List<BlogComment>> getCommentsByPostId(@PathVariable("id") String postId) {
        List<BlogComment> comments = blogCommentService.getComments(postId);
        return ResultVo.success(comments);
    }

    /**
     * 2. 在指定文章下发表一条新留言
     * 拦截不合法的空数据，直接触发全局熔断
     */
    @PostMapping("/{id}/comments")
    public ResultVo<Boolean> postComment(@PathVariable("id") String postId, @RequestBody BlogComment comment) {
        // 🚀 黄金校验：如果不合法，直接抛出业务异常，全局拦截器会自动抓取并返回状态码 500
        if (comment.getAuthor() == null || comment.getAuthor().trim().isEmpty()) {
            throw new BusinessException("AUTHOR_NAME_CANNOT_BE_EMPTY");
        }
        if (comment.getText() == null || comment.getText().trim().isEmpty()) {
            throw new BusinessException("COMMENT_CONTENT_CANNOT_BE_EMPTY");
        }

        comment.setPostId(postId);
        boolean success = blogCommentService.saveComment(comment);

        if (!success) {
            throw new BusinessException("SAVE_COMMENT_FAILED");
        }

        return ResultVo.success(true);
    }
}
