Disconf-web详细设计文档
=======

本文档主要阐述了版本 Disconf-Web 的设计。

## 详细信息 ##

https://github.com/knightliao/disconf/tree/master/disconf-web

## 表结构设计 ##

配置数据是存储在Mysql里的。

	config      配置（配置文件或配置项）
	    config_id       唯一的ID（没有啥意义，主键，自增长而已）
	    type     		配置文件/配置项
	    name			配置文件名/配置项KeY名
	    value			配置文件：文件的内容，配置项：配置值
	    app_id   		appid
	    version			版本
	    env_id			envid
	    create_time		生成时间
	    update_time     修改时间
	
	app
	 	app_id			APPID（主键，自增长）
		name			APP名(一般是产品线+服务名)
	 	description		介绍
	 	create_time		生成时间
	 	update_time		修改时间
        emails			邮箱列表逗号分隔
	
	env  (rd/qa/local可以自定义，默认为 DEFAULT_ENV)
	  	env_id			环境ID（主键，自增长）
		name			环境名字
	  
    user  
	  	user_id			用户ID（主键，自增长）
		name			姓名
        password		密码
		token			token
        ownapps			能操作的APPID,逗号分隔
        role_id			角色ID
        
    role  
	  	role_id			ID（主键，自增长）
		role_name		角色名
        create_time		生成时间
        create_by		创建人
	 	update_time		修改时间
        update_by		更新人
        
    role_resource  
	  	role_res_id		role-resource id（主键，自增长）
		role_id			用户角色id
        url_pattern		controller_requestMapping_value + method_requestMapping_value
        url_description	url功能描述
	 	method_mask		GET, PUT, POST, DELETE, 1: accessible
        update_time		更新时间	        
         