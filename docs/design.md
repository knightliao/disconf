
Disconf详细设计文档
=======

## 详细设计图： ##

![](http://ww4.sinaimg.cn/bmiddle/60c9620fgw1eh3d9pyghhj20r90qddj4.jpg)

[点击查看大图 ](http://ww4.sinaimg.cn/mw1024/60c9620fgw1eh3d9pyghhj20r90qddj4.jpg)

## 表结构设计 ##

配置数据是存储在Mysql里的。


	config4project      配置（配置文件或配置项）
	    id         		唯一的ID（没有啥意义，主键，自增长而已）
	    type     		配置文件/配置项
	    name			配置文件名/配置项KeY名
	    value			配置文件：文件的内容，配置项：配置值
	    app   			appid
	    version			版本（类似: 1_0_0_0，可以为空，表示不需要版本）
	    env				envid（可以为空，表示不需要环境）
	    create_time		生成时间
	    update_time     修改时间
	
	config_group 		配置组
	    id              唯一的ID（组ID，主键，自增长而已）
	    groupname       组名
	    desc            介绍
	
	config_group_relation 配置组与配置关系表
		id				唯一的ID（没有啥意义，主键，自增长而已）
	    group_id        组ID
	    config_id       配置ID

	app
	 	id				APPID（主键，自增长）
		name			APP名(一般是产品线+服务名)
	 	desc			介绍
	 	create_time		生成时间
	 	update_time		修改时间
	
	env  (rd/qa/local可以自定义)
	  	id				环境ID（主键，自增长）
		name			环境名字
	  	  
	  	