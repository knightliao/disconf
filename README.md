Disconf [![Build Status](https://travis-ci.org/knightliao/disconf.svg?branch=master)](https://travis-ci.org/knightliao/disconf) [![Coverage Status](https://coveralls.io/repos/knightliao/disconf/badge.png?branch=master)](https://coveralls.io/r/knightliao/disconf?branch=master)
=======

Distributed Configuration Management Platform  

分布式配置管理平台

## 项目信息 ##

- Java项目(1.6+)
- Maven管理(3.0.5+)

disconf.git branches and Maven version:

- dev(develop branch): 2.6.22-SNAPSHOT
- master(stable branch)：2.6.22
- [更新日志](https://github.com/knightliao/disconf/wiki/updates) 

在Maven Central Repository里查看 [com.baidu.disconf](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.baidu.disconf%22 )

## 它是什么? ##

- 命名为Disconf
- 专注于各种系统的配置管理的通用组件/通用平台

Disconf可以为各种业务平台提供统一的配置管理服务。

![](http://ww3.sinaimg.cn/bmiddle/60c9620fgw1eidaxpqdy3j20pr0jrgno.jpg)

## 当前版本（2.6.22）功能特点 ##

- **支持配置（配置项+配置文件）的分布式化管理**
- **配置发布统一化**
- **极简的使用方式（注解式编程 或 XML代码无代码侵入模式）**
- **低侵入性或无侵入性、强兼容性** 
- **需要Spring编程环境**

## 未来版本（完全版）功能特点 ##

### 重要功能特点 ###

- **支持配置（配置项+配置文件）的分布式化管理**
- **配置发布统一化**
    - 配置发布、更新统一化（云端存储、发布）:配置存储在云端系统，用户统一在平台上进行发布、更新配置。
    - 配置更新自动化：用户在平台更新配置，使用该配置的系统会自动发现该情况，并应用新配置。特殊地，如果用户为此配置定义了回调函数类，则此函数类会被自动调用。
- **配置异构系统管理**
    - 异构包部署统一化：这里的异构系统是指一个系统部署多个实例时，由于配置不同，从而需要多个部署包（jar或war）的情况（下同）。使用Disconf后，异构系统的部署只需要一个部署包，不同实例的配置会自动分配。特别地，在业界大量使用部署虚拟化（如JPAAS系统，SAE，BAE）的情况下，同一个系统使用同一个部署包的情景会越来越多，Disconf可以很自然地与他天然契合。
    - 异构主备自动切换：如果一个异构系统存在主备机，主机发生挂机时，备机可以自动获取主机配置从而变成主机。
    - 异构主备机Context共享工具：异构系统下，主备机切换时可能需要共享Context。可以使用Context共享工具来共享主备的Context。
- **极简的使用方式（注解式编程 或 XML代码无代码侵入模式）**：我们追求的是极简的、用户编程体验良好的编程方式。目前支持两种开发模式：基于XML配置或才基于注解，即可完成复杂的配置分布式化。
- **需要Spring编程环境**

注：配置项是指某个类里的某个Field字段。

**Disconf的功能特点描述图：**

![](http://ww1.sinaimg.cn/bmiddle/60c9620fgw1ehi7wwkdtoj20nw0fz0uh.jpg)

[查看大图](http://ww1.sinaimg.cn/mw1024/60c9620fgw1ehi7wwkdtoj20nw0fz0uh.jpg)

### 其它功能特点 ###

- **低侵入性或无侵入性、强兼容性**：
	- 低侵入性：通过极少的注解式代码撰写，即可实现分布式配置。
	- 无侵入性：通过XML简单配置，即可实现分布式配置。
	- 强兼容性：为程序添加了分布式配置注解后，开启Disconf则使用分布式配置；若关闭Disconf则使用本地配置；若开启Disconf后disconf-web不能正常Work，则Disconf使用本地配置。
- **支持配置项多个项目共享，支持批量处理项目配置**。
- **配置监控**：平台提供自校验功能（进一步提高稳定性），可以定时校验应用系统的配置是否正确。

## 模块架构图  ##

![](http://ww1.sinaimg.cn/bmiddle/60c9620fjw1eqi7cnhjp0j20e4097wfq.jpg)

[查看大图](http://ww1.sinaimg.cn/mw1024/60c9620fjw1eqi7cnhjp0j20e4097wfq.jpg)

### 模块信息###

- **disconf**
	- [disconf-core](https://github.com/knightliao/disconf/tree/master/disconf-core): 分布式配置基础包模块
	- [disconf-client](https://github.com/knightliao/disconf/tree/master/disconf-client): 分布式配置客户端模块, 依赖disconf-core包。 用户程序使用它作为Jar包进行分布式配置编程。
	- [disconf-tool](https://github.com/knightliao/disconf/tree/master/disconf-tool): 分布式配置工具包，依赖disconf-core包。 Disconf-tool是disconf的辅助工具类。
	- [disconf-web](https://github.com/knightliao/disconf/tree/master/disconf-web): 分布式配置平台服务模块, 依赖disconf-core包。采用SpringMvc+纯HTML方式实现。
	用户使用它来进行日常的分布式配置管理。
- **demo**
	- [disconf-standalone-demo](https://github.com/knightliao/disconf/tree/master/disconf-demos/disconf-standalone-demo): 使用disconf的基于Spring的standalone demo程序
	- [disconf-standalone-dubbo-demo](https://github.com/knightliao/disconf/tree/dev/disconf-demos/disconf-standalone-dubbo-demo): 集成了disconf和dubbo的基于Spring的standalone demo程序
    - [disconf-spring-demo](https://github.com/knightliao/disconf/tree/master/disconf-demos/disconf-spring-demo): 使用disconf的SpringMvc Web demo程序
	
## 用户指南 ##

用户请关注这里。

### 概述 ###

Disconf为应用方提供了三个工具，

1. [disconf-client](https://github.com/knightliao/disconf/tree/master/disconf-client), 您可以在您的应用系统里加入此Jar包；
2. [disconf-web](https://github.com/knightliao/disconf/tree/master/disconf-web), 它是一个Web平台，您可以此Web平台上管理您的配置。
3. [disconf-tool](https://github.com/knightliao/disconf/tree/master/disconf-tool),可选包。

### disconf-client 使用 ###

在您的 Maven POM 文件里加入：

    <dependency>
        <groupId>com.baidu.disconf</groupId>
        <artifactId>disconf-client</artifactId>
        <version>2.6.22</version>
    </dependency>

### disconf-web 使用 ###

部署方法请参见：[https://github.com/knightliao/disconf/tree/master/disconf-web](https://github.com/knightliao/disconf/tree/master/disconf-web)

全新主页，高清大图：

APP+环境+版本+ZK查询：

![http://ww1.sinaimg.cn/mw1024/60c9620fgw1emyww39wjmj20qw0keq6m.jpg](http://ww1.sinaimg.cn/mw1024/60c9620fgw1emyww39wjmj20qw0keq6m.jpg)

### disconf-client/disconf-web Tutorials ###

#### 总体概述

- [TutorialSummary 功能总体概述](https://github.com/knightliao/disconf/wiki/TutorialSummary)
- 文章介绍：[分布式配置管理平台Disconf](https://github.com/knightliao/disconf/wiki/%E5%88%86%E5%B8%83%E5%BC%8F%E9%85%8D%E7%BD%AE%E7%AE%A1%E7%90%86%E5%B9%B3%E5%8F%B0Disconf)

#### 基于注解式的分布式配置（支持配置文件和配置项）

推荐新建的项目使用disconf时 使用

- [Tutorial 1 注解式分布式的配置文件](https://github.com/knightliao/disconf/wiki/Tutorial1)
- [Tutorial 2 注解式分布式的配置文件高级篇: 配置更新的通知](https://github.com/knightliao/disconf/wiki/Tutorial2)
- [Tutorial 3 注解式分布式的配置项](https://github.com/knightliao/disconf/wiki/Tutorial3)
- [Tutorial 4 注解式分布式静态配置文件和静态配置项](https://github.com/knightliao/disconf/wiki/Tutorial4)

##### 注

将配置文件移至一个专有类里，而不是分散在项目的各个地方，整个代码架构清晰易懂、易管理。
即便如果哪天不使用disconf，也只需要将注解去掉即可。

#### 基于XML的分布式配置（无代码侵入）（仅支持配置文件）

推荐旧项目使用disconf时 使用

- [Tutorial 8 基于XML的分布式配置文件管理,自动reload ](https://github.com/knightliao/disconf/wiki/Tutorial8)
- [Tutorial 5 基于XML的分布式配置文件管理,不会自动reload,对于那些比较重的资源如jdbc等，特别有用](https://github.com/knightliao/disconf/wiki/Tutorial5)

#### 其它

- [Tutorial 6 disconf-web 功能详解](https://github.com/knightliao/disconf/wiki/Tutorial6)
- [Tutorial 7 可自定义的部分托管的分布式配置](https://github.com/knightliao/disconf/wiki/Tutorial7)
- [Tutorial 8 disconf与dubbo的集成 demo](https://github.com/knightliao/disconf/tree/dev/disconf-demos/disconf-standalone-dubbo-demo)
- [配置说明](https://github.com/knightliao/disconf/wiki/%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E)
- [异常考虑](https://github.com/knightliao/disconf/wiki/%E5%BC%82%E5%B8%B8%E8%80%83%E8%99%91)
- [局限性和注意事项](https://github.com/knightliao/disconf/wiki/%E5%B1%80%E9%99%90%E6%80%A7%E5%92%8C%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9)
- [注意事项](https://github.com/knightliao/disconf/wiki/%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9)
- [Zookeeper异常考虑](https://github.com/knightliao/disconf/wiki/Zookeeper%E5%BC%82%E5%B8%B8%E8%80%83%E8%99%91)

## 开发人员指南 ##

- [disconf-client详细设计文档](https://github.com/knightliao/disconf/wiki/disconf-client%E8%AF%A6%E7%BB%86%E8%AE%BE%E8%AE%A1%E6%96%87%E6%A1%A3)
- [disconf-web详细设计文档](https://github.com/knightliao/disconf/wiki/disconf-web%E8%AF%A6%E7%BB%86%E8%AE%BE%E8%AE%A1%E6%96%87%E6%A1%A3)
- [细节讨论](https://github.com/knightliao/disconf/wiki/%E7%BB%86%E8%8A%82%E8%AE%A8%E8%AE%BA)

## 其它##

- [PPT下载: 分布式配置中心服务20140624.pptx](https://github.com/knightliao/disconf/wiki/%E5%88%86%E5%B8%83%E5%BC%8F%E9%85%8D%E7%BD%AE%E4%B8%AD%E5%BF%83%E6%9C%8D%E5%8A%A120140624.pptx)
- 安全性: Disconf并没有配置审核相关的实现，但这并不意味着Disconf不重视安全性。Disconf未来可以与其它审核系统对接。 
    
##使用情况##

- 联系·讨论
    - QQ群: 239203866 
- [百度]（5+条产品线使用）
- [润生活](http://www.szzjcs.com/) （千万融资，全线产品使用）
- [拉勾网](http://www.lagou.com/) 
- [人脉通](http://renmaitong.com/) （目前已B轮融资，4条产品线使用）

## 他人评价

others:

![http://ww1.sinaimg.cn/bmiddle/60c9620fjw1ergy58j978j20i302u0t2.jpg](http://ww1.sinaimg.cn/mw1024/60c9620fjw1ergy58j978j20i302u0t2.jpg)

润生活总监：

![http://ww4.sinaimg.cn/bmiddle/60c9620fjw1est6ptf2dlj20ab01udfy.jpg](http://ww4.sinaimg.cn/bmiddle/60c9620fjw1est6ptf2dlj20ab01udfy.jpg)

人脉通后端RD：

![http://ww4.sinaimg.cn/bmiddle/60c9620fjw1est6pzqo68j208k05tjrm.jpg](http://ww4.sinaimg.cn/bmiddle/60c9620fjw1est6pzqo68j208k05tjrm.jpg)

## 联系与赞助作者

[https://github.com/knightliao/disconf/wiki/sponsor](https://github.com/knightliao/disconf/wiki/sponsor)