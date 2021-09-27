disconf-web 
===========

分布式配置Web平台服务 模块

推荐使用最新的Chrome或Firefox浏览.

注：由于迭代开发快速多变的原因，当前UI可能与下图略有改变。

## 开放API

- 让开发者具有自定义定制web控制台界面的能力: [Tutorial12 web开放API](https://github.com/knightliao/disconf/wiki/Tutorial12-web%E5%BC%80%E6%94%BEAPI)  

## 运行样式 ##

### 主页 ###

![](http://ww1.sinaimg.cn/mw1024/60c9620fgw1ekdfiw180rj20vt0gawfr.jpg)

### 登录页 ###

可以使用 admin   admin 进行登录。

![](http://ww4.sinaimg.cn/mw1024/60c9620fgw1ekdfjkgbdcj20t70ie757.jpg)

### 主界面 ###

![http://ww3.sinaimg.cn/mw1024/60c9620fgw1emxv1nw0u4j20qp0homy0.jpg](http://ww3.sinaimg.cn/mw1024/60c9620fgw1emxv1nw0u4j20qp0homy0.jpg)

左上角可以选择APP和环境，选择之后，就会在中间出现若干个版本，

选择版本后，就会显示 APP、环境、版本 三个条件下的配置列表：

![http://ww1.sinaimg.cn/mw1024/60c9620fgw1emyww39wjmj20qw0keq6m.jpg](http://ww1.sinaimg.cn/mw1024/60c9620fgw1emyww39wjmj20qw0keq6m.jpg)

####表格中 各个列的意义是：

- APP：使用哪个APP，及它的ID
- KEY：配置文件或配置项
- 配置内容：配置文件或配置项在配置中心中的值
- 实例列表：使用此配置文件或配置项的所有实例列表，及每个实例的配置值。如果实例的配置值与配置中心的值不一致，这里会标识出来。
- 修改时间：修改此配置的最后一次时间 
- 操作：个性、删除、下载

####右上角可以

新建配置项、新建配置文件、新建APP

####表格右上方 

可以批量下载所有配置文件至本地，还可以查看ZK上的部署情况。

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
**如果不确定如何配置，可以拷贝/disconf-web/profile/rd/目录下的文件，拷贝过去后修改即可。**

配置文件包括：

	- jdbc-mysql.properties (数据库配置)
	- redis-config.properties (Redis配置)
	- zoo.properties (Zookeeper配置)
	- application.properties (应用配置）

注意，记得执行将application-demo.properties复制成application.properties：
 
    cp application-demo.properties application.properties 

***注意，即使只有一个redis，也应该配置两个redis client，否则将造成内部错误。***


**设置War包将要被部署的地址（以下地址可自行设定）：**

	/home/work/dsp/disconf-rd/war


### 构建 ###

	ONLINE_CONFIG_PATH=/home/work/dsp/disconf-rd/online-resources
	WAR_ROOT_PATH=/home/work/dsp/disconf-rd/war
	export ONLINE_CONFIG_PATH
	export WAR_ROOT_PATH
	cd disconf-web
	sh deploy/deploy.sh

这样会在	/home/work/dsp/disconf-rd/war 生成以下结果：

	-disconf-web.war  
	-html  
	-META-INF  
	-WEB-INF

### 上线前的初始化工作 ###

**初始化数据库：**

可以参考 sql/readme.md 来进行数据库的初始化。注意顺序执行
0-init_table.sql        
1-init_data.sql         
201512/20151225.sql
20160701/20160701.sql

里面默认有6个用户（**请注意线上环境删除这些用户以避免潜在的安全问题**）

name | pwd
------- | -------
admin | admin
testUser1 | MhxzKhl9209
testUser2 | MhxzKhl167
testUser3 | MhxzKhl783
testUser4 | MhxzKhl8758
testUser5 | MhxzKhl112

如果想自己设置初始化的用户名信息，可以参考代码来自己生成用户：

    src/main/java/com/baidu/disconf/web/tools/UserCreateTools.java

### 部署War ###

修改server.xml文件，在Host结点下设定Context：

	<Context path="" docBase="/home/work/dsp/disconf-rd/war"></Context>

并设置端口为 8015

启动Tomcat，即可。

### 部署 前端 ###

修改 nginx.conf

    upstream disconf {
        server 127.0.0.1:8015;
    }

    server {

        listen   8081;
        server_name disconf.com;
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
    
### 关于host

这里的 host 设置成 disconf.com （可以自定义），但它 必须与 application.properties 里的domain一样。

然后浏览器的访问域名也是这个。

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


