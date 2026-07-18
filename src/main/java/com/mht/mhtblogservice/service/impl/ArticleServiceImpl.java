package com.mht.mhtblogservice.service.impl;

import com.mht.mhtblogservice.entity.Article;
import com.mht.mhtblogservice.mapper.ArticleMapper;
import com.mht.mhtblogservice.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Article> getArticleList() {
        return articleMapper.selectList(); // 纯粹返回 List
    }

    @Override
    public int createArticle(Article article) {
        return articleMapper.insert(article); // 纯粹返回受影响行数
    }

    @Override
    public Article getArticleDetail(Long id) {
        Article article = articleMapper.selectById(id);
        if (article != null) {
            // 只有查到文章才触发 Redis 计数
            redisTemplate.opsForValue().increment("blog:view:" + id);
        }
        return article; // 返回具体的文章对象（若不存在则返回 null）
    }

    @Override
    public Integer getArticleViews(Long id) {
        Object views = redisTemplate.opsForValue().get("blog:view:" + id);
        return views != null ? (Integer) views : 0; // 纯粹返回数值
    }
}
