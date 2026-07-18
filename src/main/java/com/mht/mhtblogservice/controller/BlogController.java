package com.mht.mhtblogservice.controller;

import com.mht.mhtblogservice.entity.Article;
import com.mht.mhtblogservice.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog")
public class BlogController {

    @Autowired
    private ArticleMapper articleMapper; // 完美通过原生 XML 的配置进行自动织入

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 1. 获取所有文章列表
    @GetMapping("/list")
    public List<Article> getArticleList() {
        return articleMapper.selectList();
    }

    // 2. 发布新博客
    @PostMapping("/create")
    public String createArticle(@RequestBody Article article) {
        int rows = articleMapper.insert(article);
        return rows > 0 ? "发布文章成功！" : "发布失败";
    }

    // 3. 点击某篇文章阅读（查 MySQL，同时将远在东京的 Upstash Redis 计数器异步 +1）
    @GetMapping("/{id}")
    public Article getArticleDetail(@PathVariable Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new RuntimeException("该博客不存在！");
        }

        // 核心统计逻辑 (Key 格式 -> blog:view:文章ID)
        String redisKey = "blog:view:" + id;
        redisTemplate.opsForValue().increment(redisKey);

        return article;
    }

    // 4. 从 Redis 里秒级拉取最新计数值
    @GetMapping("/{id}/views")
    public Integer getArticleViews(@PathVariable Long id) {
        String redisKey = "blog:view:" + id;
        Object views = redisTemplate.opsForValue().get(redisKey);
        return views != null ? (Integer) views : 0;
    }
}
