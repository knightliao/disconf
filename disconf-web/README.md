disconf-web
===========

分布式配置Web平台服务 模块

## 运行样式 ##

首页：

![](http://ww4.sinaimg.cn/bmiddle/60c9620fgw1ek914j9wgyj21ar0hf3zw.jpg)

登录页面：

![](http://ww2.sinaimg.cn/bmiddle/60c9620fgw1ek915hlp6qj20v80i8ab1.jpg)

更多页面：

![](http://ww1.sinaimg.cn/bmiddle/60c9620fgw1ek9165yvlpj20uh0lidjr.jpg)

![](http://ww3.sinaimg.cn/bmiddle/60c9620fgw1ek916ibw6hj20x20mgdkh.jpg)

## How to deploy ##

###安装依赖软件###

- 安装Mysql（Ver 14.12 Distrib 5.0.45, for unknown-linux-gnu (x86_64) using  EditLine wrapper）
- 安装Tomcat（apache-tomcat-7.0.50）
- 安装Nginx（nginx/1.5.3）
- 安装 zookeeeper （zookeeper-3.3.0）
- 安装 Redis （2.4.5）

### 准备配置 ###
	
**将你的配置文件放到此地址目录下（以下地址可自行设定）：**

	home/work/dsp/disconf-rd/online-resources

配置文件包括：

	- jdbc-mysql.properties
	- redis-config.properties  
	- zoo.properties 
	- application.properties

分别是 数据库配置，Redis配置，Zookeeper配置，Cookie域设置

**设置War包将要被部署的地址（以下地址可自行设定）：**

	/home/work/dsp/disconf-rd/war

### 构建 ###

	ONLINE_CONFIG_PATH=/home/work/dsp/disconf-rd/online-resources
	WAR_ROOT_PATH=/home/work/dsp/disconf-rd/war
	export ONLINE_CONFIG_PATH
	export WAR_ROOT_PATH
	cd disconf-web
	sh deploy/deplopy.sh

这样会在	/home/work/dsp/disconf-rd/war 生成以下结果：

	-disconf-web.war  
	-html  
	-META-INF  
	-WEB-INF

### 上线前的初始化工作 ###

**初始化数据库：**

- 执行 sql/1-create.sql
- 执行 sql/onlinesql/3-init.sql 

里面默认有5个用户，例如，第一个用户的用户名密码是 testUser1/MhxzKhl5258

如果想自己设置初始化的用户名信息，可以参考代码 src/main/java/com/baidu/disconf/web/tools/UserCreateTools.java

### 部署War ###

修改server.xml文件，在Host结点下设定Context：

	<Context path="" docBase="/home/work/dsp/disconf-rd/war"></Context>

并设置端口为 8015

启动Tomcat，即可。

### 部署 前端 ###

修改 nginx.conf

    #
    # disconftest.baidu.com
    #

    upstream disconf {
        server 127.0.0.1:8015;
    }

    server {

        listen   8081;
        server_name localhost;
        access_log /home/work/var/logs/disconf/access.log;
        error_log /home/work/var/logs/disconf/error.log;

        location / {
            root /home/work/dsp/disconf-rd/war/html;
            if ($query_string) {
                expires max;
            }
        }

        location ~ ^/(api|export) {
            proxy_pass_header Server;
            proxy_set_header Host $http_host;
            proxy_redirect off;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Scheme $scheme;
            proxy_pass http://disconf;
        }
    }

## 业务功能 ##

- 支持用户登录/登出
- 浏览配置
	- 按 APP/版本/环境 选择
- 修改配置
	- 修改配置项
	- 修改配置文件
- 新建配置
	- 新建配置项
	- 新建配置文件
	- 新建APP

## 架构方案 ##

Nginx(处理静态请求) + Tomcat(处理动态请求）

- **后端**
    - SpringMvc（3.1.2+)
    - Jdbc-Template
    - Mysql
    - RestFul API
    - Redis for user login/logout
    - H2内存数据库测试方案/Junit/SpringTest
- **前端**
    - HTML
    - Jquery(1.10.4)：JS工具集合
    - Bootstrap(2.3.2)：界面UI
    - Node(ejs/fs/eventproxy): 用于前端的HTML的模板化管理
- **前后端接口(前后端分离)**
    - 完全Ajax接口
    - JSON
    - RestFul API


