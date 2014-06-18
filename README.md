Disconf
=======

Distributed Configuration Management Tools 

分布式的配置管理工具

## 项目信息 ##

- Java项目(1.6+)
- Maven管理(3.0.5+)
- Spring(3.1.2+)
- 当前版本：2.0

## 它是什么? ##

- 命名为Disconf
- 专注于各种系统的配置管理的通用组件/通用平台

Disconf与其它平台的关系：

![](http://ww3.sinaimg.cn/bmiddle/60c9620fgw1ehi7wwkedaj20pr0jqmyu.jpg)

## 当前版本（2.0）功能特点 ##

- 支持配置（配置项+配置文件）的分布式化管理
- 配置发布统一化
    - 配置发布、更新统一化（云端存储、发布）
    - 配置更新自动化
- 注解式客户端编程
- 支持Spring方式编程
- 低侵入性

## 未来版本（完全版）功能特点 ##

### 重要功能特点 ###

- **支持配置（配置项+配置文件）的分布式化管理**
- **配置发布统一化**
    - 配置发布、更新统一化（云端存储、发布）:配置存储在云端系统，用户统一在平台上进行发布、更新配置。
    - 配置更新自动化：用户在平台更新配置，使用该配置的系统会自动发现该情况，并应用新配置。
- **配置异构系统管理**
    - 异构包部署统一化：这里的异构系统是指一个系统部署多个实例时，由于配置不同，从而需要多个部署包（jar或war）的情况（下同）。使用Disconf后，异构系统的部署只需要一个部署包，不同实例的配置会自动分配。特别地，在业界大量使用部署虚拟化（如百度的JPASS系统，SAE，BAE）的情况下，同一个系统使用同一个部署包的情景会越来越多，Disconf可以很自然地与他天然契合。
    - 异构主备自动切换：如果一个异构系统存在主备机，主机发生挂机时，备机可以自动获取主机配置从而变成主机。
    - 异构主备机Context共享工具：异构系统下，主备机切换时可能需要共享Context。可以使用Context共享工具来共享主备的Context。
- **注解式客户端编程**：由于SpringMvc注解式编程方式的流行，使用注解式编程方式可以便捷化客户端的开发。
- **支持Spring方式和非Spring方式编程**：用户可以自行选择编程习惯，是否采用Spring方式进行编程。推荐Spring编程方式。

Disconf的功能特点描述图：

![](http://ww1.sinaimg.cn/bmiddle/60c9620fgw1ehi7wwkdtoj20nw0fz0uh.jpg)


### 其它功能特点 ###

- 低侵入性：为程序添加了分布式配置注解后，无论是开启还是关闭Disconf，原有程序不会受任何影响。
- 支持配置项多个项目共享，支持批量处理项目配置。
- 配置监控：平台提供自校验功能（进一步提高稳定性），可以定时校验应用系统的配置是否正确。

## 模块架构图  ##

![](http://ww3.sinaimg.cn/bmiddle/60c9620fgw1ehictqxgsaj20in0b0dh0.jpg)

## 模块信息##

- **disconf**
	- disconf-core: 分布式配置基础包模块
	- disconf-client: 分布式配置客户端模块
	- disconf-web: 分布式配置平台服务模块
- **demo**
	- disconf-springdemo: 使用disconf的spring mvc web demo程序
	- disconf-demo: 使用disconf的standalone demo程序(基于spring)

## 用户教程 ##

- [Tutorial 1 分布式的配置文件](https://github.com/knightliao/disconf/wiki/Tutorial1)
- [Tutorial 2 分布式的配置文件高级篇: 配置更新的通知](https://github.com/knightliao/disconf/wiki/Tutorial2)
- [Tutorial 3 分布式的配置项](https://github.com/knightliao/disconf/wiki/Tutorial3)
- [配置说明](https://github.com/knightliao/disconf/wiki/%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E)

## 开发人员指南 ##

- [详细设计文档](https://github.com/knightliao/disconf/wiki/%E8%AF%A6%E7%BB%86%E8%AE%BE%E8%AE%A1%E6%96%87%E6%A1%A3)
- [细节讨论](https://github.com/knightliao/disconf/wiki/%E7%BB%86%E8%8A%82%E8%AE%A8%E8%AE%BA)

## 其它 ##

- 安全性: Disconf并没有配置审核相关的实现，但这并不意味着Disconf不重视安全性。Disconf未来可以与其它审核系统对接。

