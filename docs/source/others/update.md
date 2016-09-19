版本更新
=======

## 2.6.36 

2.6.36 发布于 20160911

- disconf-web:
    - 配置和配置项可支持自定义app https://github.com/knightliao/disconf/issues/147 
- disconf-client:
    - 支持https的web端 https://github.com/knightliao/disconf/issues/158
    - path支持windows: https://github.com/knightliao/disconf/issues/166
    - 删除类 DisconfMgrJustHostFileBean
    - DisconfFile 的属性 copy2TargetDirPath 更改为 targetDirPath
    

## 2.6.35 ##
    
2016年7月1号

- disconf-client: 
    - fix bug: support bean annotation @Value (但是不支持reload特性)
    - fix bug: 当同时使用XML式和注解式时，当修改配置时，XML式的BEAN也可以重新被注入 https://github.com/knightliao/disconf/issues/70
- disconf-web:
    - 支持自定义数据库名
        - 需要修改 jdbc-mysql.properties ：
            - 以前是：jdbc.db_0.url=jdbc:mysql://127.0.0.1:3306?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=false
            - 现在是 jdbc.db_0.url=jdbc:mysql://127.0.0.1:3306/disconf?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull
                          &rewriteBatchedStatements=false
    - 提供修改密码功能
        - 需要执行 disconf-web/sql/20160701/20160701.sql
    - 提供生成密码的工具
        - /disconf-web/bin/sql/makeSql.py
    - 支持client两个api
        - /api/config/list 可以自由的 指定app env version 的配置项列表（包含值）
        - /api/config/simple/list 可以自由的 指定app env version 的配置项列表（不包含值）
    
## 2.6.34 ##
    
2016年5月31号

- disconf-client: 
    - 删除临时创建的lock文件
        - https://github.com/knightliao/disconf/issues/79
    - 支持从自定义路径读取 disconf.properties
        - 例如使用 -Ddisconf.conf=/tmp/disconf.properties 来读取 disconf.properties 文件。默认是从classpath根目录读取此文件。
            - issue: https://github.com/knightliao/disconf/issues/93
            - 说明: hhttp://disconf.readthedocs.io/zh_CN/stable/config/src/client-config.html#disconf-client
    - 增加统一的类 来个性化编程式的获取任何配置数据
        - issue: https://github.com/knightliao/disconf/issues/94
        - 说明: http://disconf.readthedocs.io/zh_CN/stable/tutorial-client/src/config-getter.html
    - fix bug: 
        - 使用xml分布式配置时，当disconf.ignore掉某个配置文件时，启动后会抛异常说该配置文件找不到。修复后不会再报错
        - 新增配置项，值可以为空格，修改该配置项，就算改为有效字符，都会提示“服务器内部错误”
            - https://github.com/knightliao/disconf/issues/77
        - java.lang.IllegalArgumentException: wrong number of arguments
            - https://github.com/knightliao/disconf/issues/89
    - upgrade common-lang -> common-lang3
        - https://github.com/knightliao/disconf/issues/81
- disconf-web:
    - 删除web的jackson依赖
        - https://github.com/knightliao/disconf/issues/82
    - upgrade common-lang -> common-lang3
        - https://github.com/knightliao/disconf/issues/81
    
## 2.6.33  ##

2016月5月07日

- disconf-client: 
    - （随着以-jar方式启动的程序变得越来越流行）当使用以-jar方式启动的程序（非tomcat,web方式）时，例如springboot时，可以无缝对接（不会出现配置文件找不到的情况）。
        - 说明: http://disconf.readthedocs.io/zh_CN/stable/tutorial-client/src/jar-start-up.html  
    
## 2.6.32 ##

2016月3月27日

- disconf-client: 
    - 增加统一的回调类,unify-notify模式：灵活处理更新配置通知（方便大家在这里自由控制更新逻辑）[issue-67](https://github.com/knightliao/disconf/issues/67)
        [Tutorial-13](http://disconf.readthedocs.io/zh_CN/stable/tutorial-client/src/Tutorial13-unify-notify.html)
    - 配置初始化或更新时，通知采用 "bean setter模式": 在注入配置到实例时，优先使用set方法（方便大家在这里写自己逻辑代码），其次才是反射注入。
        [Tutorial-14](http://disconf.readthedocs.io/zh_CN/stable/tutorial-client/src/Tutorial14-bean-setter-mode.html)

## 2.6.31 ##

发布于：2016月1月8日

- disconf-client: 
    - 精减库依赖,去掉使用jersey库,避免库冲突: 改采用简单的httpclient下载
- disconf-web:
    - 支持历史操作记录第一期，支持数据库记录。
        - 需要更新SQL: https://github.com/knightliao/disconf/blob/master/disconf-web/sql/201512/20151225.sql
    
## 2.6.30  ##

发布于：2015年12月1日

- disconf-client: 
    - 不再需要将`com.baidu`加入扫描包了，只需要扫描自己的包即可。
    
        原来的方式
        
            <context:component-scan base-package="com.baidu,com.example"/>
        
        现在的
        
            <context:component-scan base-package="com.example"/>

## 2.6.29  ##

存在BUG，已废弃

## 2.6.28 ##

发布于：2015年11月20日

- disconf-client: 
    - 每个配置文件的路径支持个性化指定，不一定非是classpath 
        - http://disconf.readthedocs.io/zh_CN/stable/tutorial-client/src/Tutorial11-config-download-path.html
    - 支持spring-boot jar包识别方式 参见：https://github.com/knightliao/disconf-demos-java/tree/master/disconf-spring-boot-demo
    - 修复bug:
        - 在高于spring-bean3.1.2版本中出现基于XML配置的配置文件无法reload
    - 使 disconf.enable_local_download_dir_in_class_path 该配置用户可以自行配置：
        - http://disconf.readthedocs.io/zh_CN/stable/config/src/client-config.html
- 精减依赖项
   
## 2.6.27 ##

发布于：2015年10月26日

- disconf-client: 
    - fix bean order 问题
- disconf-web:
    - fix 上传文件（使用贴文本方式）的version无法指定的bug 
    
## 2.6.26 ##

发布于：2015年10月26日

- disconf-client: 
    - 实现真正意义上的统一上线包：disconf-client 配置文件 disconf.properties 的 所有配置项均支持环境变量方式（命令行）传入 均支持 
        - 说明：[配置说明](http://disconf.readthedocs.io/zh_CN/stable/config/src/client-config.html) 
        - [Tutorial 9 实现真正意义上的统一上线包](http://disconf.readthedocs.io/zh_CN/stable/tutorial-client/src/Tutorial9.html)
    - (重要)更新disconf.properties中所有配置项，均加上前缀 "disconf."(此升级具有兼容性，原有配置亦可以运行，但推荐升级) 
        - 说明: [配置说明](http://disconf.readthedocs.io/zh_CN/stable/config/src/client-config.html)
- disconf-web:
    - 新建配置时 app下拉页面被截断bug修复 https://github.com/knightliao/disconf/issues/22

## 2.6.25  ##

发布于：2015年8月20日

- disconf-client: 
    - 实现真正意义上的统一上线包：disconf-client 配置文件 disconf.properties 的 app,env,version 均支持 环境变量方式（命令行）参数传入方式 
        - 说明: [配置说明](http://disconf.readthedocs.io/zh_CN/stable/config/src/client-config.html) 
        - [Tutorial 9 实现真正意义上的统一上线包](http://disconf.readthedocs.io/zh_CN/stable/tutorial-client/src/Tutorial9.html)
    - disconf.properties支持 user_define_download_dir 项目，用户可以指定将配置下载到你想要的目录 
        - 说明: [配置说明](http://disconf.readthedocs.io/zh_CN/stable/config/src/client-config.html) 
        - [Tutorial 10 实现一个配置更新下载器agent](http://disconf.readthedocs.io/zh_CN/stable/tutorial-client/src/Tutorial10.html)
    - fix bugs
- disconf-demos 
    - [disconf-spring-boot-demo](https://github
    .com/knightliao/disconf/tree/dev/disconf-demos/disconf-spring-boot-demo): 使用disconf的spring-boot demo程序,更少的配置
    
## 2.6.24  ##

发布于：2015年7月3日

- disconf-client: 
    - fix bug  https://github.com/knightliao/disconf/issues/11

## 2.6.23 ##

发布于：2015年7月2日

- disconf-client: 
    - 增加功能：scanPackage 增加扫描多包功能，逗号分隔，例如：
    
            <bean id="disconfMgrBean" class="com.baidu.disconf.client.DisconfMgrBean"
                  destroy-method="destroy">
                <property name="scanPackage" value="com.example.disconf.demo,com.example.disconf.demo2"/>
            </bean>

## 2.6.22 ##

发布于：2015年6月3日

- disconf-client: 
    - fix bug: 当enable.remote.conf为false时，disconf-client可能无法读取本地配置的问题

## 2.6.21 ##

发布于：2015年4月14日

- disconf-client: 
    - 其它小修改
    - 优化 pom.xml

## 2.6.20 ##

发布于：2015年3月27日

- disconf-client: 
    - [支持基于XML配置的，无任何代码侵入的 分布式配置](http://disconf.readthedocs.io/zh_CN/stable/tutorial-client/src/Tutorial8.html)

## 2.6.19  ##

发布于：2015年1月22日

- disconf-client: 
    - [支持任意文件的配置托管](http://disconf.readthedocs.io/zh_CN/stable/tutorial-client/src/Tutorial5.html)
    - [回调时支持以配置key作为key](http://disconf.readthedocs.io/zh_CN/stable/tutorial-client/src/Tutorial2.html)
- disconf-web:
    - 支持角色系统【普通，管理员，只读管理员】
    - 当配置文件里面含有unicode时，支持显示成UTF8

## 2.6.18  ##

发布于：2014年12月19日

- disconf-client: 
    - FIX BUG: 同一台机器多个实例使用同一个classpath下的并发设置配置文件BUG（非常重要）
    - FIX BUG: disconf store use 'get' （非常重要）

## 2.6.16  ##

发布于：2014年12月3日

- disconf-client: 
    - fix zookeeper session expired error: 当ZK集群不可用时，disconf-client可以自动重连，并保证配置watch信息不丢失。
- disconf-web:
    - [主页配置获取、ZK监控情况改成ajax请求（为了避免主页载入数据太多）](http://disconf.readthedocs.io/zh_CN/stable/tutorial-web/src/Tutorial6.html)
    - [支持新建、修改任何配置时发送邮件通知](hhttp://disconf.readthedocs.io/zh_CN/stable/tutorial-web/src/Tutorial6.html)
    - [支持多用户对多APP的权限控制](http://disconf.readthedocs.io/zh_CN/stable/tutorial-web/src/Tutorial6.html)
    - [支持定时校验中心的配置 和 多客户端配置的一致性](http://disconf.readthedocs.io/zh_CN/stable/tutorial-web/src/Tutorial6.html)

## 2.6.15 ##

发布于：2014年11月7日

- disconf-client: 
    - [非注解式（托管式）的配置文件添加，增加额外的定义方式。原有的方式（2.6.14版本）的方式亦兼容，但不推荐使用。](http://disconf.readthedocs.io/zh_CN/stable/tutorial-client/src/Tutorial5.html)
    - [非注解式（托管式）的配置文件额外支持xml（以前仅支持properties）格式。](http://disconf.readthedocs.io/zh_CN/stable/tutorial-client/src/Tutorial5.html)

##  2.6.14 ##

发布于：2014年9月18日

- disconf-client: 
	- [支持非注解方式（托管式）的配置文件统一化（只支持.propertes格式)](http://disconf.readthedocs.io/zh_CN/stable/tutorial-client/src/Tutorial5.html)
	- fix bug: 静态配置文件无法动态更新的BUG
	- ZK session expire time enlarge from 5 to 30 seconds
	- [支持自定义过滤分布式配置](http://disconf.readthedocs.io/zh_CN/stable/tutorial-client/src/Tutorial7.html)
- disconf-web: 支持更便捷人性化的ZK查询
	- 配置文件的输入支持 直接文本输入+上传配置文件方式
	- 支持配置文件下载，批量下载
	- 支持显示配置所影响的机器源，并提供配置数据校验工具
	- 全新Web主页 

## 2.6.13 ##

发布于：2014年9月4日 

- fix bug: 配置里解析Integer（或类似非String）数据时出错
- Zoo Preifix: client get this value from server, not from local config
- fix bug: disconf不是最高优先级启动，导致在本地没有配置文件时，PropertyPlaceholderConfigurer在Disconf启动前初始化，
location为空，因此它认为没有配置文件存在，出现Spring启动失败。
修改方法是，使用BeanDefinitionRegistryPostProcessor使Disconf最高优先级启动，这样后面执行PropertyPlaceholderConfigurer初始化
时就可以发现所有的配置文件。

## 2.6.11 & 2.6.12   ##

- 修复BUG: 当不使用Disconf时，close会有Null异常
- 打日志策略更新：原则上日志为Debug，出错为ERROR，需要注意为WARN
 
## 2.6.10 ##

- change log: 
    - 注入静态配置域时不再打印错误字段
    - 配置完成后打印配置仓库时打印方式pretty化 
- 修复BUG: 支持空配置文件类，如EmptyConf.java，可以使用它来实现简单的同步

## 2.6.9 ##

- FixBug：找不到 disconf_sys.properties 
- 增加功能：
	- 支持静态配置文件分布式
	- 支持配置配置项分布式

## 2.6.8  ##

- Init Version