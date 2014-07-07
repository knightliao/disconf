disconf-web
===========

分布式配置平台服务模块

## 业务功能 ##

- 支持用户登录/登出
- 浏览配置
- 修改配置
- 新建配置

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


