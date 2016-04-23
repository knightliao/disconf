Disconf [![Build Status](https://travis-ci.org/knightliao/disconf.svg?branch=master)](https://travis-ci.org/knightliao/disconf) [![Coverage Status](https://coveralls.io/repos/knightliao/disconf/badge.png?branch=master)](https://coveralls.io/r/knightliao/disconf?branch=master)
=======

Distributed Configuration Management Platform(分布式配置管理平台)

专注于各种 `分布式系统配置管理` 的`通用组件`/`通用平台`, 提供统一的`配置管理服务`。

![](http://ww3.sinaimg.cn/mw1024/60c9620fjw1esvjzny1rmj20aj061t9a.jpg)

包括 **百度**、**滴滴出行**、**银联**、**网易**、**拉勾网**、**苏宁易购**、**顺丰科技** 等知名互联网公司正在使用!

[「disconf」在「2015 年度新增开源软件排名 TOP 100(OSC开源中国提供)」中排名第16强。](http://www.oschina.net/news/69808/2015-annual-ranking-top-100-new-open-source-software)

## 主要目标：

- 部署极其简单：同一个上线包，无须改动配置，即可在 多个环境中(RD/QA/PRODUCTION) 上线
- 部署动态化：更改配置，无需重新打包或重启，即可 实时生效
- 统一管理：提供web平台，统一管理 多个环境(RD/QA/PRODUCTION)、多个产品 的所有配置
- 核心目标：一个jar包，到处运行

## demos

https://github.com/knightliao/disconf-demos-java

## 项目信息 

- CLIENT 端：
    - Java: 目前唯一支持语言
    - python：打算支持
    - PHP：暂未支持
    - 开放API，让开发者具有自定义开发客户端的能力: [Tutorial12 web开放API-client](https://github.com/knightliao/disconf/wiki/Tutorial12-web%E5%BC%80%E6%94%BEAPI-client)  
- WEB 管理端：   
    - Java SpringMvc 实现，前后端分离 实现方式(基于Spring 4.1.7.RELEASE)
    - 开放API，让开发者具有自定义定制web控制台界面的能力 [Tutorial12 web开放API](https://github.com/knightliao/disconf/wiki/Tutorial12-web%E5%BC%80%E6%94%BEAPI)  

### java client

disconf.git branches and Maven version:

- dev(develop branch): 2.6.33-SNAPSHOT
- master(stable branch)：2.6.32
- [更新日志](https://github.com/knightliao/disconf/wiki/updates) 
- 在Maven Central Repository里查看 [com.baidu.disconf](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.baidu.disconf%22 )

#### Java Client Elegant Usage Preview ##

- [注解式分布式配置使用方式](http://ww3.sinaimg.cn/mw1024/60c9620fgw1eu5lsrsixcj20ga06ygna.jpg)
- [XML配置式分布式配置方式](http://ww1.sinaimg.cn/mw1024/60c9620fgw1eu5ltt9uglj20ia0j0tbo.jpg)

## 功能特点 ##

- 支持配置（配置项+配置文件）的分布式化管理
- 配置发布统一化
    - 配置发布、更新统一化: 
        - 同一个上线包 无须改动配置 即可在 多个环境中(RD/QA/PRODUCTION) 上线
        - 配置存储在云端系统，用户统一管理 多个环境(RD/QA/PRODUCTION)、多个平台 的所有配置
    - 配置更新自动化：用户在平台更新配置，使用该配置的系统会自动发现该情况，并应用新配置。特殊地，如果用户为此配置定义了回调函数类，则此函数类会被自动调用。
- 极简的使用方式（注解式编程 或 XML无代码侵入模式）：我们追求的是极简的、用户编程体验良好的编程方式。目前支持两种开发模式：基于XML配置或者基于注解，即可完成复杂的配置分布式化。

注：配置项是指某个类里的某个Field字段。

Disconf的功能特点描述图：

![](http://ww4.sinaimg.cn/bmiddle/006oy5Ulgw1f25z80js0fj30fl08uq3z.jpg)

[查看大图](http://ww3.sinaimg.cn/mw1024/006oy5Ulgw1f25z80js0fj30fl08uq3z.jpg)

### 其它功能特点 ###

- 低侵入性或无侵入性、强兼容性：
	- 低侵入性：通过极少的注解式代码撰写，即可实现分布式配置。
	- 无侵入性：通过XML简单配置，即可实现分布式配置。
	- 强兼容性：为程序添加了分布式配置注解后，开启Disconf则使用分布式配置；若关闭Disconf则使用本地配置；若开启Disconf后disconf-web不能正常Work，则Disconf使用本地配置。
- 支持配置项多个项目共享，支持批量处理项目配置。
- 配置监控：平台提供自校验功能（进一步提高稳定性），可以定时校验应用系统的配置是否正确。

## 模块架构图  ##

![](http://ww2.sinaimg.cn/bmiddle/006oy5Ulgw1f25zc2vfwpj30nh0d6q5t.jpg)

[查看大图](http://ww2.sinaimg.cn/mw1024/006oy5Ulgw1f25zc2vfwpj30nh0d6q5t.jpg)

### 模块信息###

- CLIENT: client目标是支持多语言。目前只提供了java语言客户端。
    - JAVA
    	- [disconf-core](https://github.com/knightliao/disconf/tree/master/disconf-core): 分布式配置基础包模块
    	- [disconf-client](https://github.com/knightliao/disconf/tree/master/disconf-client): 分布式配置客户端模块, 依赖disconf-core包。 用户程序使用它作为Jar包进行分布式配置编程。
    	- [disconf-tool](https://github.com/knightliao/disconf/tree/master/disconf-tool): 分布式配置工具包，依赖disconf-core包。 Disconf-tool是disconf的辅助工具类, 目前使用不多，建议不使用。
- 管理端：disconf-web是统一的分布式配置管理平台。[disconf-web](https://github.com/knightliao/disconf/tree/master/disconf-web): 分布式配置平台服务模块, 依赖disconf-core包。采用SpringMvc+纯HTML方式（前后端分离架构）实现。用户使用它来进行日常的分布式配置管理。
	
## 用户指南 ##

### client

#### java client: disconf-client 使用 ###

在您的 Maven POM 文件里加入：

    <dependency>
        <groupId>com.baidu.disconf</groupId>
        <artifactId>disconf-client</artifactId>
        <version>2.6.31</version>
    </dependency>

### server: disconf-web 使用 ###

部署方法请参见：[https://github.com/knightliao/disconf/tree/master/disconf-web](https://github.com/knightliao/disconf/tree/master/disconf-web)

全新主页，高清大图：

APP+环境+版本+ZK查询：

![http://ww1.sinaimg.cn/mw1024/60c9620fgw1emyww39wjmj20qw0keq6m.jpg](http://ww1.sinaimg.cn/mw1024/60c9620fgw1emyww39wjmj20qw0keq6m.jpg)

### java client Tutorials ###

#### 总体概述

- [TutorialSummary 功能总体概述](https://github.com/knightliao/disconf/wiki/TutorialSummary)
- 文章介绍：[分布式配置管理平台Disconf](https://github.com/knightliao/disconf/wiki/%E5%88%86%E5%B8%83%E5%BC%8F%E9%85%8D%E7%BD%AE%E7%AE%A1%E7%90%86%E5%B9%B3%E5%8F%B0Disconf)

#### 基于注解式的分布式配置（支持配置文件和配置项）

推荐新建的项目使用disconf时使用

- [Tutorial 1 注解式分布式的配置文件](https://github.com/knightliao/disconf/wiki/Tutorial1)
- [Tutorial 2 注解式分布式的配置文件高级篇: 配置更新的通知](https://github.com/knightliao/disconf/wiki/Tutorial2)
- [Tutorial 3 注解式分布式的配置项](https://github.com/knightliao/disconf/wiki/Tutorial3)
- [Tutorial 4 注解式分布式静态配置文件和静态配置项](https://github.com/knightliao/disconf/wiki/Tutorial4)

注：将配置文件移至一个专有类里，而不是分散在项目的各个地方，整个代码架构清晰易懂、易管理。
即便如果哪天不使用disconf，也只需要将注解去掉即可。

#### 基于XML的分布式配置（无代码侵入）（仅支持配置文件）

推荐新建的项目或旧项目使用disconf时使用

- [Tutorial 8 基于XML的分布式配置文件管理,自动reload ](https://github.com/knightliao/disconf/wiki/Tutorial8)
- [Tutorial 5 基于XML的分布式配置文件管理,不会自动reload,对于那些比较重的资源如jdbc等，特别有用](https://github.com/knightliao/disconf/wiki/Tutorial5)

#### demos

[https://github.com/knightliao/disconf-demos-java](https://github.com/knightliao/disconf-demos-java)

#### 其它

- [Tutorial 6 disconf-web 功能详解](https://github.com/knightliao/disconf/wiki/Tutorial6)
- [Tutorial 7 可自定义的部分托管的分布式配置](https://github.com/knightliao/disconf/wiki/Tutorial7)
- [Tutorial disconf与dubbo的集成 demo](https://github.com/knightliao/disconf/tree/master/disconf-demos/disconf-standalone-dubbo-demo)
- [Tutorial 9 实现真正意义上的统一上线包](https://github.com/knightliao/disconf/wiki/Tutorial9)
- [Tutorial 10 实现一个配置更新下载器agent](https://github.com/knightliao/disconf/wiki/Tutorial10)
- [Tutorial 13 增加统一的回调类,unify-notify模式：灵活处理更新配置通知](https://github.com/knightliao/disconf/wiki/Tutorial13-unify-notify)
- [Tutorial 14 配置初始化或更新时，通知采用 "bean setter模式"](https://github.com/knightliao/disconf/wiki/Tutorial14-bean-setter-mode)
- [配置说明](https://github.com/knightliao/disconf/wiki/%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E)
    
## 大家都在使用disconf ##

- [百度]（20+条产品线使用）
- [滴滴出行(上海/北京)](http://www.xiaojukeji.com/)
- [银联]
- [网易](http://www.163.com/)
- [苏宁易购](http://www.suning.com) (搜索中心数据处理平台)
- [顺丰科技]
- [更多](https://github.com/knightliao/disconf/wiki/users)

## 他人评价

快速递技术总监:

![http://ww1.sinaimg.cn/bmiddle/60c9620fjw1ergy58j978j20i302u0t2.jpg](http://ww1.sinaimg.cn/mw1024/60c9620fjw1ergy58j978j20i302u0t2.jpg)

润生活总监：

![http://ww4.sinaimg.cn/bmiddle/60c9620fjw1est6ptf2dlj20ab01udfy.jpg](http://ww4.sinaimg.cn/bmiddle/60c9620fjw1est6ptf2dlj20ab01udfy.jpg)

人脉通后端RD：

![http://ww4.sinaimg.cn/bmiddle/60c9620fjw1est6pzqo68j208k05tjrm.jpg](http://ww4.sinaimg.cn/bmiddle/60c9620fjw1est6pzqo68j208k05tjrm.jpg)

开源中国社区：

[「disconf」在「2015 年度新增开源软件排名 TOP 100(OSC开源中国提供)」中排名第16强。](http://www.oschina.net/news/69808/2015-annual-ranking-top-100-new-open-source-software)

## 群·联系·讨论

- disconf技术QQ群: 239203866 
- [媒体报道与网友教程](https://github.com/knightliao/disconf/wiki/%E5%AA%92%E4%BD%93%E6%8A%A5%E9%81%93%E4%B8%8E%E7%BD%91%E5%8F%8B%E6%95%99%E7%A8%8B)

## 关于

- 搜索引擎推荐：[sov5搜索引擎, 支持谷歌网页搜索/电影搜索/资源搜索/问答搜索](http://sov5.com)
- python论坛推荐：[Django中国社区](http://www.django-china.cn/)
- [联系与赞助作者](https://github.com/knightliao/disconf/wiki/sponsor) 捐助者会留名
