package cn.sinjinsong.skeleton.controller.article;

import cn.sinjinsong.common.util.PageUtil;
import cn.sinjinsong.skeleton.domain.entity.article.ArticleDO;
import cn.sinjinsong.skeleton.properties.PageProperties;
import cn.sinjinsong.skeleton.service.article.ArticleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * Created by SinjinSong on 2017/7/14.
 */
@RestController
@RequestMapping("/articles")
@Api(value = "articles", description = "文章管理")
@Slf4j
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    
    @RequestMapping(value = "/{keyword}", method = RequestMethod.GET)
    public PageInfo<ArticleDO> findByBodyContainingWithHighlight(@PathVariable("keyword") String keyword,
                                                                 @RequestParam(value = "pageNum", required = false, defaultValue = PageProperties.DEFAULT_PAGE_NUM) Integer pageNum,
                                                                 @RequestParam(value = "pageSize", required = false, defaultValue = PageProperties.DEFAULT_PAGE_SIZE) Integer pageSize) {

        Page<ArticleDO> page = articleService.findByBodyContainingWithHighlight(keyword, pageNum - 1, pageSize);
        return PageUtil.convertToGeneralPage(page);
    }
}
