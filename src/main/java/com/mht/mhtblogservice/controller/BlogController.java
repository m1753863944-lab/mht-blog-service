package com.mht.mhtblogservice.controller;

import com.mht.mhtblogservice.entity.Article;
import com.mht.mhtblogservice.entity.ResultVo;
import com.mht.mhtblogservice.exception.BusinessException;
import com.mht.mhtblogservice.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/blog")
@CrossOrigin(origins = "*")
public class BlogController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/list")
    public ResultVo<List<Article>> getArticleList() {
        List<Article> list = articleService.getArticleList();
        return ResultVo.success(list); // 在控制层统一组装成功返回
    }

    @PostMapping("/create")
    public ResultVo<String> createArticle(@RequestBody Article article) {
        int rows = articleService.createArticle(article);
        return rows > 0 ? ResultVo.success("发布文章成功！") : ResultVo.fail("发布失败");
    }

    @GetMapping("/{id}")
    public ResultVo<Article> getArticleDetail(@PathVariable Long id) {
        Article article = articleService.getArticleDetail(id);
        if (article == null) {
            throw new BusinessException("非常抱歉，该博客文章已被作者删除或不存在！");
        }

        return ResultVo.success(article);
    }

    @GetMapping("/{id}/views")
    public ResultVo<Integer> getArticleViews(@PathVariable Long id) {
        Integer views = articleService.getArticleViews(id);
        return ResultVo.success(views);
    }
}
