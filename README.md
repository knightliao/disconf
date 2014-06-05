disconf
=======

Distributed Configuration Management Tools 

分布式的配置管理工具

## 项目信息 ##

- Java项目(1.6+)
- Maven管理(3.0.5+)

## 它是什么 ##

- 专注于各种系统的配置管理的通用组件/通用平台
- 支持Web/Non-Web系统
- 命名为Disconf

![](http://ww3.sinaimg.cn/bmiddle/60c9620fgw1eh35zjqpz3j20pt0iz75z.jpg)

## 功能特点 ##

- 支持配置（配置项+配置文件）的分布式化管理
- 配置分布式化
    - 配置云端存储：将配置存储在云端系统
    - 配置管理可视化：提供Web平台，方便配置管理
- 配置发布统一化
    - 配置发布统一化：用户统一在Web平台进行发布、更新配置。
    - 配置更新自动化：用户在Web平台更新配置，使用该配置的系统会自动发现该情况，并应用新配置。
- 配置异构管理
    - 异构包部署统一化：这里的异构是指一个系统部署多个实例时由于配置不同，从而需要多个部署包（jar或war）的情况。使用Disconf后，异构系统的部署只需要一个部署包，配置会自动分配。
    - 异构主备自动切换：如果一个异构系统存在主备机，主机发生挂机时，备机可以自动获取主机配置从而变成主机。
    - 异构主备机Context共享工具：异构系统下，主备机切换时可能需要共享Context。可以使用Context共享工具来共享主备的Context。
- 注解式客户端编程：由于SpringMvc注解式编程方式的流行，使用注解式编程方式可以便捷化客户端的开发。
- 配置监控：可以实现线上机器的配置正确与否的监控报警。
- 审核机制：提供了安全审核机制，更新配置时需要系统Owner的审核，审核通过后，配置更新操作才能正常实施。

![](http://ww1.sinaimg.cn/bmiddle/60c9620fgw1eh3618p7a1j20nl0fx408.jpg)

## 架构图  ##

![](http://ww2.sinaimg.cn/bmiddle/60c9620fgw1eh35wh9w55j20b108d0th.jpg)

## Modules Infos ##


- disconf/disconf-core: 分布式配置基础包模块
- disconf/disconf-client: 分布式配置管理客户端模块
- disconf/disconf-web: 分布式配置管理平台Web模块
- disconf/disconf-tools: 分布式配置提供的一些工具包
- disconf/disconf-audit: 分布式配置审核模块
- demo/disconf-springdemo: 使用disconf的spring mvc demo程序
- demo/disconf-demo: 使用disconf的standalone demo程序

## User Guide ##

使用手册

配置说明

## Development ##

总体设计文档

详细设计文档

性能测试

