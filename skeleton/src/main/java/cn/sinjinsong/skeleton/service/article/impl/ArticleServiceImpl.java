package cn.sinjinsong.skeleton.service.article.impl;

import cn.sinjinsong.skeleton.dao.article.ArticleRepository;
import cn.sinjinsong.skeleton.domain.entity.article.ArticleDO;
import cn.sinjinsong.skeleton.service.article.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SinjinSong on 2017/7/14.
 */
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public void save(ArticleDO articleDO) {
        articleRepository.save(articleDO);        
    }

    @Override
    public void saveAll(List<ArticleDO> articles) {
        articleRepository.save(articles);
    }

    @Override
    public Iterable<ArticleDO> findAll() {
        Iterable<ArticleDO> all = articleRepository.findAll();
        log.info("iterable className:{}",all.getClass().getName());  
        return all;
    }

    @Override
    public List<ArticleDO> findByBodyContaining(String keyword) {
        return articleRepository.findByBodyContaining(keyword);
    }
}
