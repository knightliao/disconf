# MySQL-Front 5.1  (Build 4.2)

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;


# Host: localhost    Database: disconf
# ------------------------------------------------------
# Server version 5.0.67-community-nt

CREATE DATABASE `disconf` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `disconf`;

#
# Source for table app
#

CREATE TABLE `app` (
  `app_id` bigint(20) NOT NULL auto_increment COMMENT '唯一的ID（没有啥意义，主键，自增长而已）',
  `name` varchar(255) NOT NULL default '' COMMENT 'APP名(一般是产品线+服务名)',
  `description` varchar(255) NOT NULL default '' COMMENT '介绍',
  `create_time` varchar(14) NOT NULL default '99991231235959' COMMENT '生成时间',
  `update_time` varchar(14) NOT NULL default '99991231235959' COMMENT '修改时',
  `emails` varchar(255) NOT NULL default '' COMMENT '邮箱列表逗号分隔',
  PRIMARY KEY  (`app_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='app';

#
# Dumping data for table app
#

LOCK TABLES `app` WRITE;
/*!40000 ALTER TABLE `app` DISABLE KEYS */;
INSERT INTO `app` VALUES (1,'common_syswin','公用基础环境配置','99991231235959','99991231235959','');
/*!40000 ALTER TABLE `app` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table config
#

CREATE TABLE `config` (
  `config_id` bigint(20) NOT NULL auto_increment COMMENT '唯一的ID（没有啥意义，主键，自增长而已）',
  `type` tinyint(4) NOT NULL default '0' COMMENT '配置文件/配置项',
  `name` varchar(255) NOT NULL default '' COMMENT '配置文件名/配置项KeY名',
  `value` text NOT NULL COMMENT '0 配置文件：文件的内容，1 配置项：配置值',
  `app_id` bigint(20) NOT NULL COMMENT 'appid',
  `version` varchar(255) NOT NULL default 'DEFAULT_VERSION' COMMENT '版本',
  `env_id` bigint(20) NOT NULL COMMENT 'envid',
  `create_time` varchar(14) NOT NULL default '99991231235959' COMMENT '生成时间',
  `update_time` varchar(14) NOT NULL default '99991231235959' COMMENT '修改时间',
  PRIMARY KEY  (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='配置';



#
# Source for table env
#

CREATE TABLE `env` (
  `env_id` bigint(20) NOT NULL auto_increment COMMENT '环境ID（主键，自增长）',
  `name` varchar(255) NOT NULL default 'DEFAULT_ENV' COMMENT '环境名字',
  PRIMARY KEY  (`env_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='rd/qa/local可以自定义，默认为 DEFAULT_ENV';

#
# Dumping data for table env
#

LOCK TABLES `env` WRITE;
/*!40000 ALTER TABLE `env` DISABLE KEYS */;
INSERT INTO `env` VALUES (1,'rd');
INSERT INTO `env` VALUES (2,'qa');
INSERT INTO `env` VALUES (3,'local');
INSERT INTO `env` VALUES (4,'online');
/*!40000 ALTER TABLE `env` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table role
#

CREATE TABLE `role` (
  `role_id` int(10) NOT NULL auto_increment COMMENT 'id',
  `role_name` varchar(50) NOT NULL default '' COMMENT '角色名',
  `create_time` varchar(14) NOT NULL default '99991231235959' COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL default '0' COMMENT '创建人',
  `update_time` varchar(14) NOT NULL default '99991231235959' COMMENT '更新时间',
  `update_by` bigint(20) NOT NULL default '0' COMMENT '更新人',
  PRIMARY KEY  (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

#
# Dumping data for table role
#

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'普通人','99991231235959',2,'99991231235959',2);
INSERT INTO `role` VALUES (2,'管理员','99991231235959',2,'99991231235959',2);
INSERT INTO `role` VALUES (3,'测试管理员','99991231235959',2,'99991231235959',2);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table role_resource
#

CREATE TABLE `role_resource` (
  `role_res_id` int(10) NOT NULL auto_increment COMMENT 'role-resource id',
  `role_id` int(10) NOT NULL default '0' COMMENT '用户角色id',
  `url_pattern` varchar(200) NOT NULL default '' COMMENT 'controller_requestMapping_value + method_requestMapping_value',
  `url_description` varchar(200) NOT NULL default '' COMMENT 'url功能描述',
  `method_mask` varchar(4) NOT NULL default '' COMMENT 'GET, PUT, POST, DELETE, 1: accessible',
  `update_time` varchar(14) NOT NULL default '99991231235959' COMMENT '更新时间',
  PRIMARY KEY  (`role_res_id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8 COMMENT='用户角色_url访问权限表';

#
# Dumping data for table role_resource
#

LOCK TABLES `role_resource` WRITE;
/*!40000 ALTER TABLE `role_resource` DISABLE KEYS */;
INSERT INTO `role_resource` VALUES (1,1,'/api/app/list','app列表','1000','99991231235959');
INSERT INTO `role_resource` VALUES (2,2,'/api/app/list','app列表','1000','99991231235959');
INSERT INTO `role_resource` VALUES (3,3,'/api/app/list','app列表','1000','99991231235959');
INSERT INTO `role_resource` VALUES (4,1,'/api/app','生成一个app','0000','99991231235959');
INSERT INTO `role_resource` VALUES (5,2,'/api/app','生成一个app','0010','99991231235959');
INSERT INTO `role_resource` VALUES (6,3,'/api/app','生成一个app','0000','99991231235959');
INSERT INTO `role_resource` VALUES (7,1,'/api/env/list','env-list','1000','99991231235959');
INSERT INTO `role_resource` VALUES (8,2,'/api/env/list','env-list','1000','99991231235959');
INSERT INTO `role_resource` VALUES (9,3,'/api/env/list','env-list','1000','99991231235959');
INSERT INTO `role_resource` VALUES (10,1,'/api/account/session','会话','1000','99991231235959');
INSERT INTO `role_resource` VALUES (11,2,'/api/account/session','会话','1000','99991231235959');
INSERT INTO `role_resource` VALUES (12,3,'/api/account/session','会话','1000','99991231235959');
INSERT INTO `role_resource` VALUES (13,1,'/api/account/signin','登录','1000','99991231235959');
INSERT INTO `role_resource` VALUES (14,2,'/api/account/signin','登录','1000','99991231235959');
INSERT INTO `role_resource` VALUES (15,3,'/api/account/signin','登录','1000','99991231235959');
INSERT INTO `role_resource` VALUES (16,1,'/api/account/signout','登出','1000','99991231235959');
INSERT INTO `role_resource` VALUES (17,2,'/api/account/signout','登出','1000','99991231235959');
INSERT INTO `role_resource` VALUES (18,3,'/api/account/signout','登出','1000','99991231235959');
INSERT INTO `role_resource` VALUES (19,1,'/api/config/item','获取配置项','1000','99991231235959');
INSERT INTO `role_resource` VALUES (20,2,'/api/config/item','获取配置项','1000','99991231235959');
INSERT INTO `role_resource` VALUES (21,3,'/api/config/item','获取配置项','1000','99991231235959');
INSERT INTO `role_resource` VALUES (22,1,'/api/config/file','获取配置文件','1000','99991231235959');
INSERT INTO `role_resource` VALUES (23,2,'/api/config/file','获取配置文件','1000','99991231235959');
INSERT INTO `role_resource` VALUES (24,3,'/api/config/file','获取配置文件','1000','99991231235959');
INSERT INTO `role_resource` VALUES (25,1,'/api/zoo/hosts','zoo','1000','99991231235959');
INSERT INTO `role_resource` VALUES (26,2,'/api/zoo/hosts','zoo','1000','99991231235959');
INSERT INTO `role_resource` VALUES (27,3,'/api/zoo/hosts','zoo','1000','99991231235959');
INSERT INTO `role_resource` VALUES (28,1,'/api/zoo/prefix','zoo','1000','99991231235959');
INSERT INTO `role_resource` VALUES (29,2,'/api/zoo/prefix','zoo','1000','99991231235959');
INSERT INTO `role_resource` VALUES (30,3,'/api/zoo/prefix','zoo','1000','99991231235959');
INSERT INTO `role_resource` VALUES (31,1,'/api/zoo/zkdeploy','zoo','1000','99991231235959');
INSERT INTO `role_resource` VALUES (32,2,'/api/zoo/zkdeploy','zoo','1000','99991231235959');
INSERT INTO `role_resource` VALUES (33,3,'/api/zoo/zkdeploy','zoo','1000','99991231235959');
INSERT INTO `role_resource` VALUES (34,1,'/api/web/config/item','创建item-config','0010','99991231235959');
INSERT INTO `role_resource` VALUES (35,2,'/api/web/config/item','创建item-config','0010','99991231235959');
INSERT INTO `role_resource` VALUES (36,3,'/api/web/config/item','创建item-config','0000','99991231235959');
INSERT INTO `role_resource` VALUES (37,1,'/api/web/config/file','创建file-config','0010','99991231235959');
INSERT INTO `role_resource` VALUES (38,2,'/api/web/config/file','创建file-config','0010','99991231235959');
INSERT INTO `role_resource` VALUES (39,3,'/api/web/config/file','创建file-config','0000','99991231235959');
INSERT INTO `role_resource` VALUES (40,1,'/api/web/config/filetext','创建file-config','0010','99991231235959');
INSERT INTO `role_resource` VALUES (41,2,'/api/web/config/filetext','创建file-config','0010','99991231235959');
INSERT INTO `role_resource` VALUES (42,3,'/api/web/config/filetext','创建file-config','0000','99991231235959');
INSERT INTO `role_resource` VALUES (43,1,'/api/web/config/versionlist','版本list','1000','99991231235959');
INSERT INTO `role_resource` VALUES (44,2,'/api/web/config/versionlist','版本list','1000','99991231235959');
INSERT INTO `role_resource` VALUES (45,3,'/api/web/config/versionlist','版本list','1000','99991231235959');
INSERT INTO `role_resource` VALUES (46,1,'/api/web/config/list','config-list','1000','99991231235959');
INSERT INTO `role_resource` VALUES (47,2,'/api/web/config/list','config-list','1000','99991231235959');
INSERT INTO `role_resource` VALUES (48,3,'/api/web/config/list','config-list','1000','99991231235959');
INSERT INTO `role_resource` VALUES (49,1,'/api/web/config/simple/list','config-list','1000','99991231235959');
INSERT INTO `role_resource` VALUES (50,2,'/api/web/config/simple/list','config-list','1000','99991231235959');
INSERT INTO `role_resource` VALUES (51,3,'/api/web/config/simple/list','config-list','1000','99991231235959');
INSERT INTO `role_resource` VALUES (52,1,'/api/web/config/{configId}','get/post','1001','99991231235959');
INSERT INTO `role_resource` VALUES (53,2,'/api/web/config/{configId}','get/post','1001','99991231235959');
INSERT INTO `role_resource` VALUES (54,3,'/api/web/config/{configId}','get/post','1000','99991231235959');
INSERT INTO `role_resource` VALUES (55,1,'/api/web/config/zk/{configId}','get-zk','1000','99991231235959');
INSERT INTO `role_resource` VALUES (56,2,'/api/web/config/zk/{configId}','get-zk','1000','99991231235959');
INSERT INTO `role_resource` VALUES (57,3,'/api/web/config/zk/{configId}','get-zk','1000','99991231235959');
INSERT INTO `role_resource` VALUES (58,1,'/api/web/config/download/{configId}','download','1000','99991231235959');
INSERT INTO `role_resource` VALUES (59,2,'/api/web/config/download/{configId}','download','1000','99991231235959');
INSERT INTO `role_resource` VALUES (60,3,'/api/web/config/download/{configId}','download','1000','99991231235959');
INSERT INTO `role_resource` VALUES (61,1,'/api/web/config/downloadfilebatch','download','1000','99991231235959');
INSERT INTO `role_resource` VALUES (62,2,'/api/web/config/downloadfilebatch','download','1000','99991231235959');
INSERT INTO `role_resource` VALUES (63,3,'/api/web/config/downloadfilebatch','download','1000','99991231235959');
INSERT INTO `role_resource` VALUES (64,1,'/api/web/config/item/{configId}','update','0100','99991231235959');
INSERT INTO `role_resource` VALUES (65,2,'/api/web/config/item/{configId}','update','0100','99991231235959');
INSERT INTO `role_resource` VALUES (66,3,'/api/web/config/item/{configId}','update','0000','99991231235959');
INSERT INTO `role_resource` VALUES (67,1,'/api/web/config/file/{configId}','update/post','0010','99991231235959');
INSERT INTO `role_resource` VALUES (68,2,'/api/web/config/file/{configId}','update/post','0010','99991231235959');
INSERT INTO `role_resource` VALUES (69,3,'/api/web/config/file/{configId}','update/post','0000','99991231235959');
INSERT INTO `role_resource` VALUES (70,1,'/api/web/config/filetext/{configId}','update','0100','99991231235959');
INSERT INTO `role_resource` VALUES (71,2,'/api/web/config/filetext/{configId}','update','0100','99991231235959');
INSERT INTO `role_resource` VALUES (72,3,'/api/web/config/filetext/{configId}','update','0000','99991231235959');
/*!40000 ALTER TABLE `role_resource` ENABLE KEYS */;
UNLOCK TABLES;

#
# Source for table user
#

CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL auto_increment COMMENT '用户ID',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `token` varchar(255) NOT NULL COMMENT 'token',
  `ownapps` varchar(255) NOT NULL default '' COMMENT '能操作的APPID,逗号分隔',
  `role_id` bigint(20) NOT NULL default '1' COMMENT '角色ID',
  PRIMARY KEY  (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='用户表';

#
# Dumping data for table user
#

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (6,'admin','d033e22ae348aeb5660fc2140aec35850c4da997','f28d164d23291c732f64134e6b7d92be3ff8b1b3','',2);
INSERT INTO `user` VALUES (7,'admin_read','b76f3e20d1c8d0bc17d40158e44097d5eeee8640','2022ab9c2754d62f9ddba5fded91e4238247ebaf','2',3);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
