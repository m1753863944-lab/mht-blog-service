package com.mht.mhtblogservice.mapper;

import com.mht.mhtblogservice.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper // 标记为 MyBatis 的 Mapper 接口，由入口类自动扫描
public interface ArticleMapper {

    // 获取文章列表（绑定 XML 中的 id="selectList"）
    List<Article> selectList();

    // 查单篇文章（绑定 XML 中的 id="selectById"）
    Article selectById(Long id);

    // 插入新文章（绑定 XML 中的 id="insert"）
    int insert(Article article);

    int deleteById(Long id);

    int updateById(Article article);


}
