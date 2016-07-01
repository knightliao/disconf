ALTER TABLE `config_history`
ADD COLUMN `update_by` BIGINT(20) NULL DEFAULT NULL
AFTER `create_time`;

INSERT INTO `role_resource` (`role_id`, `url_pattern`, `url_description`, `method_mask`) VALUES
  (1,'/api/account/password' , '修改密码' , '0100'),
  (2,'/api/account/password' , '修改密码' , '0100'),
  (3,'/api/account/password' , '修改密码' , '0000');
