Disconf
=======

Distributed Configuration Management Platform 

分布式配置管理平台

## 项目信息 ##

- Java项目(1.6+)
- Maven管理(3.0.5+)

Disconf当前最新版本：

- disconf-client 2.6.9

在Maven Central Repository里查看[com.baidu.disconf](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.baidu.disconf%22 )

## 它是什么? ##

- 命名为Disconf
- 专注于各种系统的配置管理的通用组件/通用平台
- 百度研发

Disconf可以为各种业务平台提供统一的配置管理服务。

目前正在百度各大业务系统广泛推广。

![](http://ww3.sinaimg.cn/bmiddle/60c9620fgw1eidaxpqdy3j20pr0jrgno.jpg)

## 当前版本（2.6.9）功能特点 ##

- **支持配置（配置项+配置文件）的分布式化管理**
- **配置发布统一化**
- **注解式编程，极简的使用方式**
- **Spring方式编程**
- **低侵入性**
- **完全包容Spring配置**
- 支持配置文件/配置项为static

## 未来版本（完全版）功能特点 ##

### 重要功能特点 ###

- **支持配置（配置项+配置文件）的分布式化管理**
- **配置发布统一化**
    - 配置发布、更新统一化（云端存储、发布）:配置存储在云端系统，用户统一在平台上进行发布、更新配置。
    - 配置更新自动化：用户在平台更新配置，使用该配置的系统会自动发现该情况，并应用新配置。特殊地，如果用户为此配置定义了回调函数类，则此函数类会被自动调用。
- **配置异构系统管理**
    - 异构包部署统一化：这里的异构系统是指一个系统部署多个实例时，由于配置不同，从而需要多个部署包（jar或war）的情况（下同）。使用Disconf后，异构系统的部署只需要一个部署包，不同实例的配置会自动分配。特别地，在业界大量使用部署虚拟化（如百度的JPASS系统，SAE，BAE）的情况下，同一个系统使用同一个部署包的情景会越来越多，Disconf可以很自然地与他天然契合。
    - 异构主备自动切换：如果一个异构系统存在主备机，主机发生挂机时，备机可以自动获取主机配置从而变成主机。
    - 异构主备机Context共享工具：异构系统下，主备机切换时可能需要共享Context。可以使用Context共享工具来共享主备的Context。
- **注解式编程，极简的使用方式**：我们追求的是极简的、用户编程体验良好的编程方式。通过简单的标注+极简单的代码撰写，即可完成复杂的配置分布式化。
- **支持Spring方式和非Spring方式编程**：用户可以自行选择编程习惯，是否采用Spring方式进行编程。推荐Spring编程方式。
- **完全包容Spring配置**：使用分布式配置，可以完全兼容Spring的配置使用方式。使用Disconf，正常情况下，实例会使用分布式配置模式。如果Disconf远程服务挂机，则实例会退化成使用本地配置模式。

**Disconf的功能特点描述图：**

![](http://ww1.sinaimg.cn/bmiddle/60c9620fgw1ehi7wwkdtoj20nw0fz0uh.jpg)

[查看大图](http://ww1.sinaimg.cn/mw1024/60c9620fgw1ehi7wwkdtoj20nw0fz0uh.jpg)

### 其它功能特点 ###

- **低侵入性**：为程序添加了分布式配置注解后，无论是开启还是关闭Disconf，原有程序不会受任何影响。
- **支持配置项多个项目共享，支持批量处理项目配置**。
- **配置监控**：平台提供自校验功能（进一步提高稳定性），可以定时校验应用系统的配置是否正确。

## 模块架构图  ##

![](http://ww2.sinaimg.cn/bmiddle/60c9620fgw1ehqlp58zqej20iq0bdq46.jpg)

[查看大图](http://ww2.sinaimg.cn/mw1024/60c9620fgw1ehqlp58zqej20iq0bdq46.jpg)

## 模块信息##

- **disconf**
	- [disconf-core](https://github.com/knightliao/disconf/tree/master/disconf-core): 分布式配置基础包模块
	- [disconf-client](https://github.com/knightliao/disconf/tree/master/disconf-client): 分布式配置客户端模块, 依赖disconf-core包。
	用户程序使用它作为Jar包进行分布式配置编程。
	- [disconf-web](https://github.com/knightliao/disconf/tree/master/disconf-web): 分布式配置平台服务模块, 依赖disconf-core包。采用SpringMvc+纯HTML方式实现。
	用户使用它来进行日常的分布式配置管理。
- **demo**
	- [disconf-spring-demo](https://github.com/knightliao/disconf/tree/master/disconf-demos/disconf-spring-demo): 使用disconf的SpringMvc Web demo程序
	- [disconf-standalone-demo](https://github.com/knightliao/disconf/tree/master/disconf-demos/disconf-standalone-demo): 使用disconf的基于Spring的standalone demo程序
	
## 用户指南 ##

用户请关注这里。

### 概述 ###

Disconf为应用方提供了两个工具，

1. 一个是 [disconf-client](https://github.com/knightliao/disconf/tree/master/disconf-client), 您可以在您的应用系统里加入此Jar包；
2. 另一个是[disconf-web](https://github.com/knightliao/disconf/tree/master/disconf-web)，它是一个Web平台，您可以此Web平台上管理您的配置。

### disconf-client 使用 ###

在您的 Maven POM 文件里加入：

    <dependency>
        <groupId>com.baidu.disconf</groupId>
        <artifactId>disconf-client</artifactId>
        <version>2.6.9</version>
    </dependency>

主要依赖为：

- Spring(3.1.2+)
- Zookeeper(3.3.6)
- Jedis(2.1.0
- reflections(0.9.9-RC1)
- gson(2.2.4)
- guava(16.0)
- wiremock(1.4.6, test)
- jmockit(15.0, test)

### disconf-web 使用 ###

部署方法请参见：[https://github.com/knightliao/disconf/tree/master/disconf-web](https://github.com/knightliao/disconf/tree/master/disconf-web)

###  disconf-client 的极简使用方式 预览 ###

**配置类：**

	package com.baidu.disconf.demo.config;
	
	import org.springframework.stereotype.Service;
	
	import com.baidu.disconf.client.common.annotations.DisconfFile;
	import com.baidu.disconf.client.common.annotations.DisconfFileItem;
	
	/**
	 * Redis配置文件
	 * 
	 * @author liaoqiqi
	 * @version 2014-6-17
	 */
	@Service
	@DisconfFile(filename = "redis.properties")
	public class JedisConfig {
	
	    // 代表连接地址
	    private String host;
	
	    // 代表连接port
	    private int port;
	
	    /**
	     * 地址, 分布式文件配置
	     * 
	     * @return
	     */
	    @DisconfFileItem(name = "redis.host", associateField = "host")
	    public String getHost() {
	        return host;
	    }
	
	    public void setHost(String host) {
	        this.host = host;
	    }
	
	    /**
	     * 端口, 分布式文件配置
	     * 
	     * @return
	     */
	    @DisconfFileItem(name = "redis.port", associateField = "port")
	    public int getPort() {
	        return port;
	    }
	
	    public void setPort(int port) {
	        this.port = port;
	    }
	}

**使用时：**
    
    package com.baidu.disconf.demo.service;
    
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.DisposableBean;
    import org.springframework.beans.factory.InitializingBean;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    
    import redis.clients.jedis.Jedis;
    
    import com.baidu.disconf.demo.config.JedisConfig;
    import com.baidu.disconf.demo.utils.JedisUtil;
    
    /**
     * 一个简单的Redis服务
     * 
     * @author liaoqiqi
     * @version 2014-6-17
     */
    @Service
    public class SimpleRedisService implements InitializingBean, DisposableBean {
    
        protected static final Logger LOGGER = LoggerFactory
                .getLogger(SimpleRedisService.class);
    
        // jedis 实例
        private Jedis jedis = null;
    
        /**
         * 分布式配置
         */
        @Autowired
        private JedisConfig jedisConfig;
    
        /**
         * 关闭
         */
        public void destroy() throws Exception {
    
            if (jedis != null) {
                jedis.disconnect();
            }
        }
    
        /**
         * 进行连接
         */
        public void afterPropertiesSet() throws Exception {
    
            jedis = JedisUtil.createJedis(jedisConfig.getHost(),
                    jedisConfig.getPort());
        }
    
        /**
         * 获取一个值
         * 
         * @param key
         * @return
         */
        public String getKey(String key) {
            if (jedis != null) {
                return jedis.get(key);
            }
    
            return null;
        }
    }


无须其它额外配置，上面这段配置即可实现分布式配置。Disconf分布式配置包容了Spring配置方式。

（注：这里说的不需要额外配置，指的是：不需要额外地使用Spring将配置项的值注入到类中）

### disconf-client Tutorials ###

- [Tutorial 1 分布式的配置文件](https://github.com/knightliao/disconf/wiki/Tutorial1)
- [Tutorial 2 分布式的配置文件高级篇: 配置更新的通知](https://github.com/knightliao/disconf/wiki/Tutorial2)
- [Tutorial 3 分布式的配置项](https://github.com/knightliao/disconf/wiki/Tutorial3)
- [Tutorial 4 分布式静态配置文件和静态配置项](https://github.com/knightliao/disconf/wiki/Tutorial4)
- [配置说明](https://github.com/knightliao/disconf/wiki/%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E)
- [异常考虑](https://github.com/knightliao/disconf/wiki/%E5%BC%82%E5%B8%B8%E8%80%83%E8%99%91)
- [局限性和注意事项](https://github.com/knightliao/disconf/wiki/%E5%B1%80%E9%99%90%E6%80%A7%E5%92%8C%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9)

## 开发人员指南 ##

- [disconf-client详细设计文档](https://github.com/knightliao/disconf/wiki/disconf-client%E8%AF%A6%E7%BB%86%E8%AE%BE%E8%AE%A1%E6%96%87%E6%A1%A3)
- [disconf-web详细设计文档](https://github.com/knightliao/disconf/wiki/disconf-web%E8%AF%A6%E7%BB%86%E8%AE%BE%E8%AE%A1%E6%96%87%E6%A1%A3)
- [细节讨论](https://github.com/knightliao/disconf/wiki/%E7%BB%86%E8%8A%82%E8%AE%A8%E8%AE%BA)

## 其它 ##

- [PPT下载: 分布式配置中心服务20140624.pptx](https://github.com/knightliao/disconf/wiki/%E5%88%86%E5%B8%83%E5%BC%8F%E9%85%8D%E7%BD%AE%E4%B8%AD%E5%BF%83%E6%9C%8D%E5%8A%A120140624.pptx)
- 安全性: Disconf并没有配置审核相关的实现，但这并不意味着Disconf不重视安全性。Disconf未来可以与其它审核系统对接。

## 联系·讨论 ##

QQ群: 239203866

## Authors ##

- [Qiqi Liao](https://github.com/knightliao): knightliao AT gmail.com
- [Ning Wu](https://github.com/ngloom): ngloom AT gmail.com