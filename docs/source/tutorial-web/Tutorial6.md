Tutorial 6 disconf-web 功能详解
=======

## UI及架构 ##

[disconf-web](../install/02.html)

## 主要功能

###新建功能

- 新建APP
- 新建配置文件、新建配置项

###修改功能

- 修改配置文件
- 修改配置项

###删除功能

- 删除配置文件
- 删除配置项

### 下载功能

- 下载单个配置文件
- 批量下载指定 APP、ENV、VERSION 下的所有配置文件

### 查询功能

- 查询配置功能
    - 查询指定 APP、ENV、VERSION 下的所有配置文件列表
    - 查询配置文件内容
    - 查询配置项内容
    - 查询使用此配置的所有实例列表，并且 当实例使用的配置值与中心不一致时，会有飘红提示
- 查询ZK功能
    - 查询 指定 APP、ENV、VERSION 下的ZK信息。

### 权限功能

- 用户访问哪些APP可以控制（USER表里的 ownapps 字段）

### 手动触发的提醒功能

- 当新建或修改配置时，会有邮件提醒（app表里的 email 字段）

![](http://ww3.sinaimg.cn/mw1024/60c9620fgw1emyv9b06rpj20r40c475i.jpg)

### 自动提醒功能

- 系统可以自动监控 所有实例与 配置中心的值 是否一致，如果不一致，就会报警（app表里的 email 字段）

![](http://ww1.sinaimg.cn/mw1024/60c9620fgw1emyvhj84a4j20sq07awfc.jpg)

## 注

- `手动触发的提醒功能` 与 `自动提醒功能` 两个功能的 DIFF 功能 采用的是 [java-diff](http://techv5.com/topic/979/)



