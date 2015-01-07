use `disconf`;

CREATE TABLE `role` (
	`role_id` INT(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
	`role_name` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '角色名',
	`create_time` VARCHAR(14) NOT NULL DEFAULT '99991231235959' COMMENT '创建时间',
	`create_by` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '创建人',
	`update_time` VARCHAR(14) NOT NULL DEFAULT '99991231235959' COMMENT '更新时间',
	`update_by` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '更新人',
	PRIMARY KEY (`role_id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB;


CREATE TABLE `role_resource` (
  `role_res_id` INT(10) NOT NULL AUTO_INCREMENT COMMENT 'role-resource id',
  `role_id` INT(10) NOT NULL DEFAULT '0' COMMENT '用户角色id',
  `url_pattern` VARCHAR(200) NOT NULL DEFAULT '' COMMENT 'controller_requestMapping_value + method_requestMapping_value',
  `url_description` VARCHAR(200) NOT NULL DEFAULT '' COMMENT 'url功能描述',
  `method_mask` VARCHAR(4) NOT NULL DEFAULT '' COMMENT 'GET, PUT, POST, DELETE, 1: accessible',
  `update_time` VARCHAR(14) NOT NULL DEFAULT '99991231235959' COMMENT '更新时间',
  PRIMARY KEY (`role_res_id`)
)
COMMENT='用户角色_url访问权限表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB;


INSERT INTO `role` (`role_id`, `role_name`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (1, '普通人',
                                                                                                            '99991231235959', 2, '99991231235959', 2);
INSERT INTO `role` (`role_id`, `role_name`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (2, '管理员',
                                                                                                            '99991231235959', 2, '99991231235959', 2);
INSERT INTO `role` (`role_id`, `role_name`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES (3,'测试管理员',
                                                                                                            '99991231235959', 2, '99991231235959', 2);
ALTER TABLE `user`
ADD COLUMN `role_id` BIGINT(20) NOT NULL DEFAULT '1' COMMENT '角色ID' AFTER `ownapps`;

update `user` set role_id=2,ownapps='' where name='admin';

/* admin_read	admin_read*/
INSERT INTO `user` (`user_id`, `name`, `password`, `token`, `ownapps`,`role_id`) VALUES (null, 'admin_read', 'b76f3e20d1c8d0bc17d40158e44097d5eeee8640', '2022ab9c2754d62f9ddba5fded91e4238247ebaf','2', '3');


use disconf;
delete from role_resource;
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/app/list' , 'app列表' , '1000'),
  (2,'/api/app/list' , 'app列表' , '1000'),
  (3,'/api/app/list' , 'app列表' , '1000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/app' , '生成一个app' , '0000'),
  (2,'/api/app' , '生成一个app' , '0010'),
  (3,'/api/app' , '生成一个app' , '0000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/env/list' , 'env-list' , '1000'),
  (2,'/api/env/list' , 'env-list' , '1000'),
  (3,'/api/env/list' , 'env-list' , '1000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/account/session' , '会话' , '1000'),
  (2,'/api/account/session' , '会话' , '1000'),
  (3,'/api/account/session' , '会话' , '1000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/account/signin' , '登录' , '1000'),
  (2,'/api/account/signin' , '登录' , '1000'),
  (3,'/api/account/signin' , '登录' , '1000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/account/signout' , '登出' , '1000'),
  (2,'/api/account/signout' , '登出' , '1000'),
  (3,'/api/account/signout' , '登出' , '1000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/config/item' , '获取配置项' , '1000'),
  (2,'/api/config/item' , '获取配置项' , '1000'),
  (3,'/api/config/item' , '获取配置项' , '1000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/config/file' , '获取配置文件' , '1000'),
  (2,'/api/config/file' , '获取配置文件' , '1000'),
  (3,'/api/config/file' , '获取配置文件' , '1000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/zoo/hosts' , 'zoo' , '1000'),
  (2,'/api/zoo/hosts' , 'zoo' , '1000'),
  (3,'/api/zoo/hosts' , 'zoo' , '1000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/zoo/prefix' , 'zoo' , '1000'),
  (2,'/api/zoo/prefix' , 'zoo' , '1000'),
  (3,'/api/zoo/prefix' , 'zoo' , '1000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/zoo/zkdeploy' , 'zoo' , '1000'),
  (2,'/api/zoo/zkdeploy' , 'zoo' , '1000'),
  (3,'/api/zoo/zkdeploy' , 'zoo' , '1000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/web/config/item' , '创建item-config' , '0010'),
  (2,'/api/web/config/item' , '创建item-config' , '0010'),
  (3,'/api/web/config/item' , '创建item-config' , '0000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/web/config/file' , '创建file-config' , '0010'),
  (2,'/api/web/config/file' , '创建file-config' , '0010'),
  (3,'/api/web/config/file' , '创建file-config' , '0000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/web/config/filetext' , '创建file-config' , '0010'),
  (2,'/api/web/config/filetext' , '创建file-config' , '0010'),
  (3,'/api/web/config/filetext' , '创建file-config' , '0000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/web/config/versionlist' , '版本list' , '1000'),
  (2,'/api/web/config/versionlist' , '版本list' , '1000'),
  (3,'/api/web/config/versionlist' , '版本list' , '1000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/web/config/list' , 'config-list' , '1000'),
  (2,'/api/web/config/list' , 'config-list' , '1000'),
  (3,'/api/web/config/list' , 'config-list' , '1000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/web/config/simple/list' , 'config-list' , '1000'),
  (2,'/api/web/config/simple/list' , 'config-list' , '1000'),
  (3,'/api/web/config/simple/list' , 'config-list' , '1000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/web/config/{configId}' , 'get/post' , '1001'),
  (2,'/api/web/config/{configId}' , 'get/post' , '1001'),
  (3,'/api/web/config/{configId}' , 'get/post' , '1000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/web/config/zk/{configId}' , 'get-zk' , '1000'),
  (2,'/api/web/config/zk/{configId}' , 'get-zk' , '1000'),
  (3,'/api/web/config/zk/{configId}' , 'get-zk' , '1000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/web/config/download/{configId}' , 'download' , '1000'),
  (2,'/api/web/config/download/{configId}' , 'download' , '1000'),
  (3,'/api/web/config/download/{configId}' , 'download' , '1000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/web/config/downloadfilebatch' , 'download' , '1000'),
  (2,'/api/web/config/downloadfilebatch' , 'download' , '1000'),
  (3,'/api/web/config/downloadfilebatch' , 'download' , '1000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/web/config/item/{configId}' , 'update' , '0100'),
  (2,'/api/web/config/item/{configId}' , 'update' , '0100'),
  (3,'/api/web/config/item/{configId}' , 'update' , '0000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/web/config/file/{configId}' , 'update/post' , '0010'),
  (2,'/api/web/config/file/{configId}' , 'update/post' , '0010'),
  (3,'/api/web/config/file/{configId}' , 'update/post' , '0000');
INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/web/config/filetext/{configId}' , 'update' , '0100'),
  (2,'/api/web/config/filetext/{configId}' , 'update' , '0100'),
  (3,'/api/web/config/filetext/{configId}' , 'update' , '0000');


INSERT INTO `config` (`config_id`, `type`, `name`, `value`, `app_id`, `version`, `env_id`, `create_time`, `update_time`)
VALUES
  (146, 0, 'code.properties', 'syserror.paramtype=\\u8bf7\\u6c42\\u53c2\\u6570\\u89e3\\u6790\\u9519\" + \"\\u8bef', 2, '1_0_0_0', 1, '20150107115835', '20150107115835');
