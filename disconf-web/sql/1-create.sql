CREATE DATABASE IF NOT EXISTS `disconf`;
USE `disconf`;

CREATE TABLE `app` (
    `app_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一的ID（没有啥意义，主键，自增长而已）',
    `name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'APP名(一般是产品线+服务名)',
    `description` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '介绍',
    `create_time` VARCHAR(14) NOT NULL DEFAULT '99991231235959' COMMENT '生成时间',
    `update_time` VARCHAR(14) NOT NULL DEFAULT '99991231235959' COMMENT '修改时',
    PRIMARY KEY (`app_id`)
)
COMMENT='app'
COLLATE='utf8_general_ci'
ENGINE=InnoDB;


CREATE TABLE `config` (
    `config_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一的ID（没有啥意义，主键，自增长而已）',
    `type` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '配置文件/配置项',
    `name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '配置文件名/配置项KeY名',
    `value` TEXT NOT NULL COMMENT '0 配置文件：文件的内容，1 配置项：配置值',
    `app_id` BIGINT(20) NOT NULL COMMENT 'appid',
    `version` VARCHAR(255) NOT NULL DEFAULT 'DEFAULT_VERSION' COMMENT '版本',
    `env_id` BIGINT(20) NOT NULL COMMENT 'envid',
    `create_time` VARCHAR(14) NOT NULL DEFAULT '99991231235959' COMMENT '生成时间',
    `update_time` VARCHAR(14) NOT NULL DEFAULT '99991231235959' COMMENT '修改时间',
    PRIMARY KEY (`config_id`)
)
COMMENT='配置'
COLLATE='utf8_general_ci'
ENGINE=InnoDB;


CREATE TABLE `env` (
    `env_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '环境ID（主键，自增长）',
    `name` VARCHAR(255) NOT NULL DEFAULT 'DEFAULT_ENV' COMMENT '环境名字',
    PRIMARY KEY (`env_id`)
)
COMMENT='rd/qa/local可以自定义，默认为 DEFAULT_ENV'
COLLATE='utf8_general_ci'
ENGINE=InnoDB;


CREATE TABLE `user` (
    `user_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `token` VARCHAR(255) NOT NULL COMMENT 'token',
    PRIMARY KEY (`user_id`)
)
COMMENT='用户表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB;



