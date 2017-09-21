# 该项目骨架集成了以下技术:
- SpringBoot多环境配置
- SpringMVC
- Spring
- MyBaits
- MyBatis Generator
- MyBatis PageHelper
- Druid
- Lombok
- JWT
- Spring Security
- JavaMail
- Thymeleaf
- HttpClient
- FileUpload
- Spring Scheduler
- Hibernate Validator
- Redis Cluster
- MySQL主从复制，读写分离
- Spring Async
- Spring Cache
- Swagger
- Spring Test
- MockMvc
- HTTPS
- Spring DevTools
- Spring Actuator
- Logback+Slf4j多环境日志
- i18n
- Maven Multi-Module
- WebSocket
- ElasticSearch

# 功能们：
## 用户模块
    - 获取图片验证码
    - 登录:解决重复登录问题
    - 注册
    - 分页查询用户信息
    - 修改用户信息
## 站内信模块
    - 一对一发送站内信
    - 管理员广播
    - 读取站内信（未读和已读）
    - 一对多发送站内信
## 文件模块
    - 文件上传
    - 文件下载
## 邮件模块
    - 单独发送邮件
    - 群发邮件
    - Thymeleaf邮件模板
## 安全模块
    - 注解形式的权限校验
    - 拦截器
## 文章管理模块
    - 增改删查

# 整合注意点

1. 每个Mapper上都要加@Mapper

2. yaml文件 @Value获取xx.xx.xx不可行，必须使用@ConfigurationProperties，指定prefix，属性设置setter和getter

3. logback日志重复打印：自定义logger上加上  ` additivity="false"  `

4. SpringBoot 项目没有项目名

5. 登录 Spring Security +JWT
   - 已登录用户验证token
        - 主要是在Filter中操作。
        从requestHeader中取得token，检查token的合法性，检查这一步可以解析出username去查数据库；
        也可以查询缓存，如果缓存中有该token，那么就没有问题，可以放行。
   
   - 未登录用户进行登录
        - 登录时要构造UsernamePasswordAuthenticationToken，用户名和密码来自于参数，然后调用AuthenticationManager的authenticate方法，
        它会去调用UserDetailsService的loadFromUsername，参数是token的username，然后比对password，检查userDetails的一些状态。
        如果一切正常，那么会返回Authentication。返回的Authentication的用户名和密码是正确的用户名和密码，并且还放入了之前查询出的Roles。
        调用getAuthentication然后调用getPrinciple可以得到之前听过UserDetailsService查询出的UserDetails
   - 在Controller中使用@PreAuthorize等注解需要在spring-web配置文件中扫描security包下的类  

6. 引用application.properties中的属性的方式：@ConfigurationProperties(prefix = "spring.mail") + @Component + setter + getter  

7. 引用其他自定义配置文件中的属性的方式：
     - @Component
     - @ConfigurationProperties(prefix = "auth")
     - @PropertySource("classpath:auth.properties")
     - setter & getter 

8. 所以写静态资源位置的时候，不要带上映射的目录名（如/static/，/public/ ，/resources/，/META-INF/resources/）！

9. 所有的html都放在templates下面，只有index.html能直接访问，其他均不可，必须通过Controller的转发
10. 静态资源都放在static下面，访问时Spring Security会检查URL，根据URL进行拦截。如果通过，那么会交给ViewResolver，添加前面的/static（无需配置，SpringBoot自动完成），得到最终的真实路径
11. Mybatis打印SQL http://www.cnblogs.com/lixuwu/p/6323739.html

12. 访问Druid监控： http://localhost:8080/druid
13. spring-devtools热部署：
    - 前提：把Idea的自动编译打开
    - 修改类-->保存：应用会重启
    - 修改配置文件-->保存：应用会重启
    - 修改页面-->保存：应用不会重启，但会重新加载，页面会刷新（原理是将spring.thymeleaf.cache设为false）
14. @Bean  
    - 可以指定name，如果不指定那么使用方法名作为name 
    - 有一个initMethod和destroyMethod两个属性，值为该Bean的方法名 ；当然也可以直接在方法上使用注解@PostConstruct&@PreDestroy

15. @EnableAsync @EnableTransactionManagement @EnableCaching @EnableScheduling @EnableWebSecurity @EnableSwagger2
    - @EnableTransactionManagement | @EnableWebSecurity  可以不加，自动配置
16. @Conditional

17. 注册Servlet、Filter、Listener 

18. ApplicationEvent Spring 提供的Observer模型骨架

19. SpringMVC 拦截器 实现HandlerInterceptor接口或继承HandlerInterceptorAdaptor类，然后将其注册为一个Bean，并在registry(继承了WebMvcConfigurerAdapter的配置类)中调用addInterceptor
如果想要自己完全控制WebMVC，就需要在@Configuration注解的配置类上增加@EnableWebMvc；否则会使用自动配置。一般不使用@EnableWebMvc

20. TODO:WebSocket ,RabbitMQ, Spring Batch,Docker

21. @SpringBootApplication是一个组合注解，组合了@EnableAutoConfiguration，根据类路径中的jar包依赖为当前项目进行自动配置

22. Jaskson 反序列化  mapper.readValue(content, new TypeReference<PageInfo<MailDO>>() {});

23. SSL配置
    1. 生成一个证书 .keystore
    2. 把该证书复制到项目根目录(父项目，不是子模块)，然后在application.properties中添加如下配置
         ```
        server:
            tomcat:
                uri-encoding: UTF-8
                max-threads: 1000
                min-spare-threads: 30
            port: 8443
            ssl:
              key-store: .keystore
              key-store-password: 130119
              key-store-type: JKS
              key-alias: tomcat
         ```
    3.  设置HTTP转向HTTPS   ->HTTPSConfig

24. 如果想直接访问html，那么必须在WebConfig里设置registry
    所有的html都放在/templates下

# ElasticSearch 学习
## application.properties

默认 9300 是 Java 客户端的端口。9200 是支持 Restful HTTP 的接口。


在实体类上加入
## @Document
(indexName="article_index", //索引库的名称，个人建议以项目的名称命名（相当于一个Database）
 indexName 配置必须是全部小写，不然会出异常。
type="article", //类型，个人建议以实体的名称命名（相当于一张表）
shards=5, //默认分区数
replicas=1, //每个分区默认的备份数
indexStoreType="fs", //索引文件存储类型
refreshInterval="-1" //刷新间隔
)
在需要建立索引的类上加上@Document注解，即表明这个实体需要进行索引。默认情况下这个实体中所有的属性都会被建立索引、并且分词。
在主键上加入@Id
我们通过@Field注解来进行详细的指定。
## @Field
(format=DateFormat.date_time,  //default DateFormat.none;
index=FieldIndex.no, //默认情况下分词
store=true, //默认情况下不存储原文
type=FieldType.Object) //自动检测属性的类型
private Date postTime;

## 构建查询：
### Query keywords(查询关键字)




关键字             例子                  
Elasticsearch查询语句

And                 findByNameAndPrice
{"bool" : {"must" : [ {"field" : {"name" : "?"}}, {"field" : {"price" : "?"}} ]}}

Or                  findByNameOrPrice
{"bool" : {"should" : [ {"field" : {"name" : "?"}}, {"field" : {"price" : "?"}} ]}}

Is                  findByName
{"bool" : {"must" : {"field" : {"name" : "?"}}}}

Not                 findByNameNot
{"bool" : {"must_not" : {"field" : {"name" : "?"}}}}

LessThanEqual       findByPriceLessThan
{"bool" : {"must" : {"range" : {"price" : {"from" : null,"to" : ?,"include_lower" : true,"include_upper" : true}}}}}

GreaterThanEqual    findByPriceGreaterThan
{"bool" : {"must" : {"range" : {"price" : {"from" : ?,"to" : null,"include_lower" : true,"include_upper" : true}}}}}

Before              findByPriceBefore
{"bool" : {"must" : {"range" : {"price" : {"from" : null,"to" : ?,"include_lower" : true,"include_upper" : true}}}}}

After               findByPriceAfter
{"bool" : {"must" : {"range" : {"price" : {"from" : ?,"to" : null,"include_lower" : true,"include_upper" : true}}}}}

Like                findByNameLike
{"bool" : {"must" : {"field" : {"name" : {"query" : "?*","analyze_wildcard" : true}}}}}

StartingWith        findByNameStartingWith
{"bool" : {"must" : {"field" : {"name" : {"query" : "?*","analyze_wildcard" : true}}}}}

EndingWith          findByNameEndingWith
{"bool" : {"must" : {"field" : {"name" : {"query" : "*?","analyze_wildcard" : true}}}}}

Containing             findByNameContaining
{"bool" : {"must" : {"field" : {"name" : {"query" : "?","analyze_wildcard" : true}}}}}

In                  findByNameIn(Collectionnames)
{"bool" : {"must" : {"bool" : {"should" : [ {"field" : {"name" : "?"}}, {"field" : {"name" : "?"}} ]}}}}

NotIn               findByNameNotIn(Collectionnames)
{"bool" : {"must_not" : {"bool" : {"should" : {"field" : {"name" : "?"}}}}}}


True                findByAvailableTrue
{"bool" : {"must" : {"field" : {"available" : true}}}}

False               findByAvailableFalse
{"bool" : {"must" : {"field" : {"available" : false}}}}

OrderBy             findByAvailableTrueOrderByNameDesc
{"sort" : [{ "name" : {"order" : "desc"} }],"bool" : {"must" : {"field" : {"available" : true}}}}


### @Query
public interface BookRepository extends ElasticsearchRepository<Book, String> {
    @Query("{"bool" : {"must" : {"field" : {"name" : "?0"}}}}")
    Page<Book> findByName(String name,Pageable pageable);
}

### 自定义Query
Iterable<T> search(QueryBuilder query);
Page<T> search(QueryBuilder query, Pageable pageable);
Page<T> search(SearchQuery searchQuery);
Page<T> searchSimilar(T entity, String[] fields, Pageable pageable);

注意：Mybatis PageHelper 的起始页码是1，而Spring Data分页的起始页码是0
它们的Page也不一样，统一使用时建议前端只看
total
pages
pageNum
pageSize
size


# WebSocket

## 参考资料：
> http://lrwinx.github.io/2017/07/09/%E5%86%8D%E8%B0%88websocket-%E8%AE%BA%E6%9E%B6%E6%9E%84%E8%AE%BE%E8%AE%A1/
> http://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html
> http://blog.csdn.net/daniel7443/article/details/54377326
> http://www.cnblogs.com/winkey4986/p/5622758.html
## 功能：
- 实现服务器端的消息推送，实时页面刷新
- 即时通讯，单聊&群聊
## 实现：
 1. @EnableWebSocketMessageBroker注解表示开启使用STOMP协议来传输基于代理的消息，Broker就是代理的意思。 
 2. registerStompEndpoints方法表示注册STOMP协议的节点，并指定映射的URL。 
 3. stompEndpointRegistry.addEndpoint("/endpointSang").withSockJS();这一行代码用来注册STOMP协议节点，同时指定使用SockJS协议。 
 4. configureMessageBroker方法用来配置消息代理，由于我们是实现推送功能，这里的消息代理是/topic
## 配置：
 1. registry.enableSimpleBroker("/topic","/user");
 2. registry.setApplicationDestinationPrefixes("/app");
 3. registry.setUserDestinationPrefix("/user/");
 第一个是作为@SendTo的前缀
 第二三个是作为客户端发送信息send的前缀，后接@MessageMapping
## 身份验证：
JWT
见WebSocketConfig
## 消息推送：
客户端有两种消息发送方式：
1. 经过了服务器编写的MessageHandler(@MessageMapping)，适用于需要服务器对消息进行处理的，客户端将消息发送给服务器，服务器将消息处理后
广播给所有用户。
示例：客户端订阅了/greetings，并会向/hello发送数据
实现：
- 服务器：@MessageMapping("/hello")   @SendTo("/topic/greetings")
- 客户端：stompClient.send("/app/hello", {}, JSON.stringify(...));
         stompClient.subscribe('/topic/greetings', function (response) {
                showResponse(JSON.parse(response.body).body);
         });   

2. 不经过服务器，客户端发送的消息直接广播给所有用户，此时send和subscribe的路径是一样的
示例：客户端订阅了/greetings，并会向/greetings发送数据
实现：
- 服务器：什么都不用做
- 客户端：stompClient.send("/topic/greetings", {}, JSON.stringify(...));
         stompClient.subscribe('/topic/greetings', function (response) {
                showResponse(JSON.parse(response.body).body);
         });

共同点：都需要登录


- @MessageMapping 客户端发送路径
@MessageMapping注解和我们之前使用的@RequestMapping类似。客户端向该(ApplicationPrefix+@MessageMapping)路径发送消息。
@MessageMapping("/hello")
stompClient.send("/app/hello", {}, JSON.stringify({'body': name}));
客户端会先将信息发送到代理（Broker，位于服务器），然后Broker会再将处理后的信息发送给客户端

- @SendTo 客户端接收路径
@SendTo注解表示当服务器有消息需要推送的时候，会对订阅了@SendTo中路径的浏览器发送消息。
@SendTo("/topic/xxx")中必须要以WebSocketConfig中messageBroker中设置的任一Prefix("/topic")为前缀


## 聊天
- @SendToUser  
发送给单一客户端的标志
注意是谁请求的发送给谁

- convertAndSend 
template.convertAndSend("/topic/hello",greeting) //广播  

- convertAndSendToUser
convertAndSendToUser(userId, "/message",userMessage) //一对一发送，发送特定的客户端 

- @MessageExceptionHandler

# Spring Boot Actuator
HTTP方法	路径	描述	鉴权
GET	/autoconfig	查看自动配置的使用情况	true
GET	/configprops	查看配置属性，包括默认配置	true
GET	/beans	查看bean及其关系列表	true
GET	/dump	打印线程栈	true
GET	/env	查看所有环境变量	true
GET	/env/{name}	查看具体变量值	true
GET	/health	查看应用健康指标	false
GET	/info	查看应用信息	false
GET	/mappings	查看所有url映射	true
GET	/metrics	查看应用基本指标	true
GET	/metrics/{name}	查看具体指标	true
POST /shutdown	关闭应用	true
GET	/trace	查看基本追踪信息	true


## 非服务化分布式

### MySQL读写分离
<!-- 启动对@Aspectj的支持 true为cglib，false为jdk代理，为true的话，会导致拦截不了mybatis的mapper-->
    <aop:aspectj-autoproxy proxy-target-class="false" />
注意数据源的切换必须要在事务开启之前，不能在开启事务时没有确定数据源

动态数据源切换与事务：
事务是加在Service上的，也就是一个service方法中间不能切换数据源
如果数据源的切换是拦截了DAO，那么是有问题的，因为在service开启事务时无法确定数据源，并且同一个事务中间即使调用了多个dao方法，也不能切换数据源。
解决方法1是拦截service
解决方法2是将事务加在service上，但是不推荐这样做。

现在采用的是方法1，是根据service的方法上的@Transactional注解的readOnly属性判断
这就要求我们在编写service时：
所有service上的方法都必须加上@Transactional注解
如果该方法不涉及任何的写操作，那么必须指定readOnly属性为true
该属性的默认值是false

## 服务化分布式


### RocketMQ
添加依赖
```
<!-- https://mvnrepository.com/artifact/com.alibaba.rocketmq/rocketmq-client -->
<dependency>
    <groupId>com.alibaba.rocketmq</groupId>
    <artifactId>rocketmq-client</artifactId>
    <version>3.2.6</version>
</dependency>
```

共三个示例
1. skeleton整合了最简单的producer和consumer，业务逻辑放在Application的main方法中
2. mq-producer-pay和mq-consumer-balance模拟转账事务
   先启动broker们
   启动consumer，入口是main方法
   启动producer，入门是main方法
   
   业务逻辑是pay向balance转账1000元
3. pullConsumer 在skeleton中
   
   


### Zookeeper
添加依赖
```
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.4.5</version>
</dependency>
```

共一个示例：zk模块下面都是zookeeper客户端的示例代码


## 服务化分布式：Dubbo
分为provider和consumer
service:当前有user、article、mail、email模块
 
