package cn.sinjinsong.skeleton.service.article;

import cn.sinjinsong.skeleton.test.BaseSpringTest;
import cn.sinjinsong.skeleton.domain.entity.article.ArticleDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * Created by SinjinSong on 2017/7/14.
 */
public class ArticleServiceTest extends BaseSpringTest{
    @Autowired
    private ArticleService articleService;
    
    @Test
    public void save() throws Exception {
        ArticleDO articleDO = new ArticleDO(1L,"陆奇","成为优秀工程师的五个方法","首先要相信技术，我刚才已经讲了，整个我们工业界，特别是像百度这样的公司，对技术坚定的、不动摇的信念特别重要。\n" +
                "\n" +
                "我也分享一下，盖茨提到微软公司的宗旨就是：写软件代表的是世界的将来。\n" +
                "\n" +
                "为什么？未来任何一个工业都会变成软件工业。盖茨是对的，因为任何工业任何行业自动化的程度会越来越高，最后你所处理的就是信息和知识。\n" +
                "\n" +
                "但现在软件的做法又往前提了一次，因为在人工智能时代，不光是写代码，你必须懂算法，懂硬件，懂数据，整个人工智能的开发过程有一个很大程度的提高，但是，技术，特别是我们这个工业所代表的技术一定是将来任何工业的前沿。\n" +
                "\n" +
                "所以 我们一定要有一个坚定不移的深刻的理念，相信整个世界终究是为技术所驱动的。");
        ArticleDO articleDO1 = new ArticleDO(2L,"杜艳新","58同城iOS客户端Hybrid框架探索","58同城iOS客户端的Hybrid框架在最初设计和演进的过程中，遇到了许多问题。为此，整个Hybrid框架产生了很大的变化。本文作者将遇到的典型问题进行了总结，并重点介绍58 iOS采用的解决方案，希望能给读者搭建自己的Hybrid框架提供一些参考。\n" +
                "\n" +
                "引言\n" +
                "Hybrid App是指同时使用Native与Web的App。Native界面具有良好的用户体验，但是不易动态改变，且开发成本较高。对于变动较大的页面，使用Web来实现是一个比较好的选择，所以，目前很多主流App都采用Native与Web混合的方式搭建。58同城客户端上线不久即采用了Hybrid方式，至今已有六七年。而iOS客户端的Hybrid框架在最初设计和演进的过程中，随着时间推移和业务需求的不断增加，遇到了许多问题。为了解决它们，整个Hybrid框架产生了很大的变化。本文将遇到的典型问题进行了总结，并重点介绍58 iOS采用的解决方案，希望能给读者搭建自己的Hybrid框架一些参考。主要包括以下四个方面：\n" +
                "\n" +
                "1. 通讯方式以及通讯框架\n" +
                "58 App最初采用的Web调用Native的通讯方式是AJAX请求，不仅存在内存泄露问题，且Native在回调给Web结果时无法确定回调给哪个Web View。另外，如何搭建一个简单、实用、扩展性好的Hybrid框架是一个重点内容。这些内容将在通讯部分详细介绍。\n" +
                "\n" +
                "2. 缓存原理及缓存框架\n" +
                "提升Web页面响应速度的一个有效手段就是使用缓存。58 iOS客户端如何对Web资源进行缓存以及如何搭建Hybrid缓存框架将在缓存部分介绍。\n" +
                "\n" +
                "3. 性能\n" +
                "iOS 8推出了WebKit框架，核心是WKWebView，其在性能上要远优于UIWebView，并且提供了一些新的功能，但遗憾的是WKWebView不支持自定义缓存。我们经过调研和测试发现了一些从UIWebView升级到WKWebView的可行解决方案，将在性能部分重点介绍。\n" +
                "\n" +
                "4. 耦合\n" +
                "58 iOS客户端最初的Hybrid框架设计过于简单，导致Web载体页渐渐变得十分臃肿，继承关系十分复杂。耦合部分详细介绍了平稳解决载体页耦合问题的方案。\n" +
                "\n" +
                "通讯\n" +
                "Hybrid框架首先要考虑的问题就是Web与Native之间的通讯。苹果在iOS 7系统推出了JavaScriptCore.framework框架，通过该框架可以方便地实现JavaScript与Native的通讯工作。但是在58 App最早引入Hybrid时，需要支持iOS 7以下的系统版本，所以58 App并没有使用JavaScriptCore.framework，而是采用了更原始的方式。\n" +
                "\n" +
                "传统的通讯方式（如图1所示）中，Native调用JavaScript代码比较简单，直接使用UIWebView提供的接口stringByEvaluatingJavaScriptFromString:就可以实现。而JavaScript调用Native的功能需要通过拦截请求的方式来实现。即JavaScript发送一个特殊的URL请求，该请求并不是真正的网络访问请求，而是调用Native功能的请求，并传递相关的参数。Native端收到请求后进行判断，如果是功能调URL请求则调用Native的相应功能，而不进行网络访问。");
        articleService.saveAll(Arrays.asList(articleDO,articleDO1));
    }

    @Test
    public void findAll() throws Exception {
        articleService.findAll().forEach(System.out::println);        
    }

    @Test
    public void findByBodyContaining() throws Exception {
        articleService.findByBodyContaining("Hybrid").forEach(System.out::println);
    }

}