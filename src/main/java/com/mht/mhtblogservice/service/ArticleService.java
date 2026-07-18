package com.mht.mhtblogservice.service;

import com.mht.mhtblogservice.entity.Article;
import java.util.List;

public interface ArticleService {
    List<Article> getArticleList();

    int createArticle(Article article);

    Article getArticleDetail(Long id);

    Integer getArticleViews(Long id);


    int deleteArticle(Long id);

    int updateArticle(Article article);


}
