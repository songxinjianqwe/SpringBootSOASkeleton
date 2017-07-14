package cn.sinjinsong.skeleton.service.article;

import cn.sinjinsong.skeleton.domain.entity.article.ArticleDO;

import java.util.List;

/**
 * Created by SinjinSong on 2017/7/14.
 */
public interface ArticleService {
    void save(ArticleDO article);
    void saveAll(List<ArticleDO> articles);
    Iterable<ArticleDO> findAll();

    List<ArticleDO> findByBodyContaining(String keyword);
}
