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
- Redis
- Spring Async
- Spring Cache
- Swagger
- Spring Test
- MockMvc
- HTTPS
- Spring DevTools
- Logback+Slf4j多环境日志
- i18n
- Maven Multi-Module
- WebSocket（待整合）
- RabbitMQ（待整合）
- OAUTH（待整合）
- ElasticSearch（待整合）

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
    不喜欢使用@Value，每个属性都要写一遍名字

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


## ElasticSearch 学习
### application.properties
在实体类上加入
@Document
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
@Field
(format=DateFormat.date_time,  //default DateFormat.none;
index=FieldIndex.no, //默认情况下分词
store=true, //默认情况下不存储原文
type=FieldType.Object) //自动检测属性的类型
private Date postTime;

 
 

