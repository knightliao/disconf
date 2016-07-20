CREATE DATABASE IF NOT EXISTS `disconf`;
USE `disconf`;

CREATE TABLE `app` (
  `app_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一的ID（没有啥意义，主键，自增长而已）',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT 'APP名(一般是产品线+服务名)',
  `description` varchar(255) NOT NULL DEFAULT '' COMMENT '介绍',
  `create_time` varchar(14) NOT NULL DEFAULT '99991231235959' COMMENT '生成时间',
  `update_time` varchar(14) NOT NULL DEFAULT '99991231235959' COMMENT '修改时',
  `emails` varchar(255) NOT NULL DEFAULT '' COMMENT '邮箱列表逗号分隔',
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='app';

CREATE TABLE `config` (
  `config_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一的ID（没有啥意义，主键，自增长而已）',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '配置文件/配置项',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '配置文件名/配置项KeY名',
  `value` text NOT NULL COMMENT '0 配置文件：文件的内容，1 配置项：配置值',
  `app_id` bigint(20) NOT NULL COMMENT 'appid',
  `version` varchar(255) NOT NULL DEFAULT 'DEFAULT_VERSION' COMMENT '版本',
  `env_id` bigint(20) NOT NULL COMMENT 'envid',
  `create_time` varchar(14) NOT NULL DEFAULT '99991231235959' COMMENT '生成时间',
  `update_time` varchar(14) NOT NULL DEFAULT '99991231235959' COMMENT '修改时间',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=150 DEFAULT CHARSET=utf8 COMMENT='配置';

CREATE TABLE `env` (
  `env_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '环境ID（主键，自增长）',
  `name` varchar(255) NOT NULL DEFAULT 'DEFAULT_ENV' COMMENT '环境名字',
  PRIMARY KEY (`env_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='rd/qa/local可以自定义，默认为 DEFAULT_ENV';

CREATE TABLE `role` (
  `role_id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_name` varchar(50) NOT NULL DEFAULT '' COMMENT '角色名',
  `create_time` varchar(14) NOT NULL DEFAULT '99991231235959' COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `update_time` varchar(14) NOT NULL DEFAULT '99991231235959' COMMENT '更新时间',
  `update_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新人',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE `role_resource` (
  `role_res_id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'role-resource id',
  `role_id` int(10) NOT NULL DEFAULT '0' COMMENT '用户角色id',
  `url_pattern` varchar(200) NOT NULL DEFAULT '' COMMENT 'controller_requestMapping_value + method_requestMapping_value',
  `url_description` varchar(200) NOT NULL DEFAULT '' COMMENT 'url功能描述',
  `method_mask` varchar(4) NOT NULL DEFAULT '' COMMENT 'GET, PUT, POST, DELETE, 1: accessible',
  `update_time` varchar(14) NOT NULL DEFAULT '99991231235959' COMMENT '更新时间',
  PRIMARY KEY (`role_res_id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8 COMMENT='用户角色_url访问权限表';

CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `token` varchar(255) NOT NULL COMMENT 'token',
  `ownapps` varchar(255) NOT NULL DEFAULT '' COMMENT '能操作的APPID,逗号分隔',
  `role_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '角色ID',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='用户表';

