## 每个Mapper上都要加@Mapper

## yaml文件 @Value获取xx.xx.xx不可行，必须使用@ConfigurationProperties，指定prefix，属性设置setter和getter


## logback日志重复打印：自定义logger上加上  ` additivity="false"  `
## SpringBoot 项目没有项目名

## 登录 Spring Security +JWT
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

## 引用application.properties中的属性的方式：@ConfigurationProperties(prefix = "spring.mail") + @Component + setter + getter

## 引用其他自定义配置文件中的属性的方式：
     - @Component
     - @ConfigurationProperties(prefix = "auth")
     - @PropertySource("classpath:auth.properties")
     - setter & getter 
## 所以写静态资源位置的时候，不要带上映射的目录名（如/static/，/public/ ，/resources/，/META-INF/resources/）！

## 所有的html都放在templates下面，只有index.html能直接访问，其他均不可，必须通过Controller的转发
## 静态资源都放在static下面，访问时Spring Security会检查URL，根据URL进行拦截。如果通过，那么会交给ViewResolver，添加前面的/static（无需配置，SpringBoot自动完成），得到最终的真实路径

## 访问swagger

### 用户模块
    - 获取图片验证码
    - 登录:解决重复登录问题
    - 注册
    - 分页查询用户信息
    - 修改用户信息
### 站内信模块
    - 一对一发送站内信
    - 管理员广播
    - 读取站内信（未读和已读）
    - 一对多发送站内信
### 文件模块
    - 文件上传
    - 文件下载
### 邮件模块
    - 单独发送邮件
    - 群发邮件
    - Thymeleaf邮件模板
### 安全模块
    - 注解形式的权限校验
    - 拦截器
    