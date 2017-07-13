package cn.sinjinsong.skeleton.dao.article;

import cn.sinjinsong.skeleton.domain.entity.article.ArticleDO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Created by SinjinSong on 2017/7/13.
 */
public interface ArticleRepository extends ElasticsearchRepository<ArticleDO,Long> {
    List<ArticleDO> findByBodyLike(String keyWord); 
}
