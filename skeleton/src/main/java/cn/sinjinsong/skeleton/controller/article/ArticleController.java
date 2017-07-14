package cn.sinjinsong.skeleton.controller.article;

import cn.sinjinsong.common.annotation.BaseRESTController;
import cn.sinjinsong.skeleton.service.article.ArticleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by SinjinSong on 2017/7/14.
 */
@BaseRESTController
@RequestMapping("/articles")
@Api(value="articles",description = "文章管理")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    
}
