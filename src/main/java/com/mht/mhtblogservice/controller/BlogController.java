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


    // 🚀 新增：根据 ID 删除博客文章（同步清理 MySQL + Redis）
    @DeleteMapping("/delete/{id}")
    public ResultVo<String> deleteArticle(@PathVariable Long id) {
        // 1. 先调用详情服务，检查这篇文章到底在不在
        Article article = articleService.getArticleDetail(id);
        if (article == null) {
            throw new BusinessException("非常抱歉，该文章本身就不存在，无法执行删除！");
        }

        // 2. 执行双清删除逻辑
        int rows = articleService.deleteArticle(id);

        return rows > 0 ? ResultVo.success("文章及缓存数据已成功同步卸载！") : ResultVo.fail("删除失败");
    }


    // 🚀 新增：根据 ID 修改/更新博客文章（完美包装 ResultVo 并享受 AOP 日志记录）
    @PutMapping("/update")
    public ResultVo<String> updateArticle(@RequestBody Article article) {
        // 1. 安全检查：前端必须传过来要修改的文章 ID，否则无法更新
        if (article.getId() == null) {
            throw new BusinessException("更新失败：缺少必要的文章主键 ID 参数！");
        }

        // 2. 核心校验：先调用详情服务，检查这篇文章目前在云端数据库是否真实存在
        Article existArticle = articleService.getArticleDetail(article.getId());
        if (existArticle == null) {
            throw new BusinessException("修改失败：该文章不存在或已被其他人提前删除！");
        }

        // 3. 执行更新逻辑
        int rows = articleService.updateArticle(article);

        return rows > 0 ? ResultVo.success("文章内容已成功同步至云端数据库！") : ResultVo.fail("更新失败");
    }

}
