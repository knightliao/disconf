use `disconf`;
/* 删除用户表，创建用户权限表*/
drop table `user`;

CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `name` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(200) NOT NULL COMMENT '密码',
  `ownapps` varchar(255) NOT NULL DEFAULT '' COMMENT '能操作的APPID,逗号分隔',
  `role_id` bigint(20) NOT NULL DEFAULT '1' COMMENT '角色ID',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username_idx` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户权限表';

INSERT INTO `user_role`(`name`,`password`,`role_id`)
VALUES('admin','35cac73ace56659d2311dc81799652edcfb40f03',2);
