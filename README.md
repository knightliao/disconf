Disconf 
=======

[![Apache License 2](https://img.shields.io/badge/license-ASF2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)
[![Build Status](https://travis-ci.org/knightliao/disconf.svg?branch=master)](https://travis-ci.org/knightliao/disconf) 
[![Coverage Status](https://coveralls.io/repos/knightliao/disconf/badge.png?branch=master)](https://coveralls.io/r/knightliao/disconf?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.baidu.disconf/disconf-client/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.baidu.disconf/disconf-client)

招聘：我的团队开始招聘啦！ https://shimo.im/docs/mOqakWapJlMh91WP

Distributed Configuration Management Platform(分布式配置管理平台) 

专注于各种「分布式系统配置管理」的「通用组件」和「通用平台」, 提供统一的「配置管理服务」

![](http://ww3.sinaimg.cn/mw1024/60c9620fjw1esvjzny1rmj20aj061t9a.jpg)

包括 **百度**、**滴滴出行**、**银联**、**网易**、**拉勾网**、**苏宁易购**、**顺丰科技** 等知名互联网公司正在使用!

[「disconf」在「2015 年度新增开源软件排名 TOP 100(OSC开源中国提供)」中排名第16强。](http://www.oschina.net/news/69808/2015-annual-ranking-top-100-new-open-source-software)

## 主要目标：

- 部署极其简单：同一个上线包，无须改动配置，即可在 多个环境中(RD/QA/PRODUCTION) 上线
- 部署动态化：更改配置，无需重新打包或重启，即可 实时生效
- 统一管理：提供web平台，统一管理 多个环境(RD/QA/PRODUCTION)、多个产品 的所有配置
- 核心目标：一个jar包，到处运行

## demos && 文档 && 协作

- demos: https://github.com/knightliao/disconf-demos-java
- wiki: https://github.com/knightliao/disconf/wiki
- 文档: http://disconf.readthedocs.io
- 协作开发: 在 master 分支上提pull request
- 提问题: https://github.com/knightliao/disconf/issues 提issue

## 版本

- dev(dev branch): 2.6.36
- master(latest && cooperate && contribute branch)：2.6.36
- stable(release && stable branch): 2.6.36

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

## 大家都在使用disconf ##

- [百度]（20+条产品线使用）
- [滴滴出行(上海/北京)](http://www.xiaojukeji.com/)
- [银联]
- [网易](http://www.163.com/)
- [苏宁易购](http://www.suning.com) (搜索中心数据处理平台)
- [顺丰科技]
- [更多](http://disconf.readthedocs.io/zh_CN/latest/others/src/contribute.html)

## 他人评价

快速递技术总监:

![http://ww1.sinaimg.cn/bmiddle/60c9620fjw1ergy58j978j20i302u0t2.jpg](http://ww1.sinaimg.cn/mw1024/60c9620fjw1ergy58j978j20i302u0t2.jpg)

润生活总监：

![http://ww4.sinaimg.cn/bmiddle/60c9620fjw1est6ptf2dlj20ab01udfy.jpg](http://ww4.sinaimg.cn/bmiddle/60c9620fjw1est6ptf2dlj20ab01udfy.jpg)

人脉通后端RD：

![http://ww4.sinaimg.cn/bmiddle/60c9620fjw1est6pzqo68j208k05tjrm.jpg](http://ww4.sinaimg.cn/bmiddle/60c9620fjw1est6pzqo68j208k05tjrm.jpg)

## 团队招聘

- 招聘：我的团队开始招聘啦！ https://shimo.im/docs/mOqakWapJlMh91WP

## 我的其它作品
- 搜索引擎: https://www.sov5.cn
- 内容开放平台: http://open.sov5.cn
- 今天看啥 - 高品质阅读: http://www.jintiankansha.me
- Disconf - 分布式配置管理平台: http://github.com/knightliao/disconf
- CanalX - 基于 `Canal` 的数据感知服务框架: http://github.com/knightliao/canalX
- jutf - Java Unit Test Framework: https://github.com/knightliao/jutf
- pfrock - A plugin-based server for running fake HTTP services (especially SOA service): https://github.com/knightliao/pfrock
  
