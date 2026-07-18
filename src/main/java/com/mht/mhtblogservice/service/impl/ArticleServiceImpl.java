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

    @Override
    public int deleteArticle(Long id) {
        // 1. 调用 Mapper 执行 MySQL 物理删除
        int rows = articleMapper.deleteById(id);

        // 2. 🌟 核心：如果 MySQL 删除成功，同步把 Upstash Redis 里的浏览量计数 Key 彻底抹去
        if (rows > 0) {
            String redisKey = "blog:view:" + id;
            redisTemplate.delete(redisKey); // 强行销毁该缓存键，防止内存泄漏
        }

        return rows;
    }

    @Override
    public int updateArticle(Article article) {
        // 调用 Mapper 根据传入对象的 id 进行 title 和 content 的内容覆写
        return articleMapper.updateById(article);
    }


}
