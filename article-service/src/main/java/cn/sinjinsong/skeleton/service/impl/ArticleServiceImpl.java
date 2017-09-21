package cn.sinjinsong.skeleton.service.impl;

import cn.sinjinsong.skeleton.dao.dao.article.ArticleRepository;
import cn.sinjinsong.skeleton.domain.entity.article.ArticleDO;
import cn.sinjinsong.skeleton.service.article.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
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
    private static final String SCORE_MODE_SUM = "sum"; // 权重分求和模式
    private static final Float MIN_SCORE = 10.0F;      // 由于无相关性的分值默认为 1 ，设置权重分最小值为 10'

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
        log.info("iterable className:{}", all.getClass().getName());
        return all;
    }

    @Override
    public List<ArticleDO> findByBodyContaining(String keyword) {
        return articleRepository.findByBodyContaining(keyword);
    }
    
    @Override
    public Page<ArticleDO> findByBodyContainingWithHighlight(String keyword, Integer pageNum, Integer pageSize) {
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
                .add(QueryBuilders.matchPhraseQuery("title", keyword).analyzer("ik_smart"),
                        ScoreFunctionBuilders.weightFactorFunction(1000))
                .add(QueryBuilders.matchPhraseQuery("body", keyword).analyzer("ik_smart"),
                        ScoreFunctionBuilders.weightFactorFunction(500))
                .scoreMode(SCORE_MODE_SUM).setMinScore(MIN_SCORE);
        // 分页参数
        Pageable pageable = new PageRequest(pageNum, pageSize);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(functionScoreQueryBuilder)
                .build();
        log.info("DSL:{}", query.getQuery().toString());
        return articleRepository.search(query);
    }
}
