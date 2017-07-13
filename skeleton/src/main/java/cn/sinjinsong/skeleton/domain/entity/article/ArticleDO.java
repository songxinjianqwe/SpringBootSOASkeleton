package cn.sinjinsong.skeleton.domain.entity.article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by SinjinSong on 2017/7/13.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "spring_boot_skeleton",type="article",shards = 5,replicas = 1,indexStoreType = "fs",refreshInterval = "-1")
public class ArticleDO {
    @Id
    private Long id;
    private String author;
    private String title;
    private String body;
}
