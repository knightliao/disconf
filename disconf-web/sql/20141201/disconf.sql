use `disconf`;

ALTER TABLE `app`
    ADD COLUMN `emails` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '邮箱列表逗号分隔' AFTER `update_time`;

ALTER TABLE `user`
    ADD COLUMN `ownapps` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '能操作的APPID,逗号分隔' AFTER `token`;

    
/* testUser1  MhxzKhl9209*/
/* testUser2  MhxzKhl167*/
/* testUser3  MhxzKhl783*/
/* testUser4  MhxzKhl8758*/
/* testUser5  MhxzKhl112*/
DELETE FROM `user` where user_id=1;
INSERT INTO `user` (`user_id`, `name`, `password`, `token`, `ownapps`) VALUES (1, 'testUser1', '5eec8499597a115c88e0a9580ae1562ab85d0b1a', 'b9070d385a13357efa09e50e080607c2b299241b','2');
DELETE FROM `user` where user_id=2;
INSERT INTO `user` (`user_id`, `name`, `password`, `token`, `ownapps`) VALUES (2, 'testUser2', '71e9dc667eefa5a3a4840cb4f1ce22bc246f22f0', 'b169dec42f61ec6cbad88d70e7c4c6b89630ccfb','2');
DELETE FROM `user` where user_id=3;
INSERT INTO `user` (`user_id`, `name`, `password`, `token`, `ownapps`) VALUES (3, 'testUser3', 'e2cdc4a9195030543e38e19a923f075d54471cc4', 'a1a20b0e03a5191c530cbfc064eda3c16254df64','2');
DELETE FROM `user` where user_id=4;
INSERT INTO `user` (`user_id`, `name`, `password`, `token`, `ownapps`) VALUES (4, 'testUser4', '5cef2d7e4ada5a615f03e12b569d80aedfb056fc', '007b07fccbc1c82c987f7b8e4651e85cca01cf2b','2');
DELETE FROM `user` where user_id=5;
INSERT INTO `user` (`user_id`, `name`, `password`, `token`, `ownapps`) VALUES (5, 'testUser5', 'f996eeaa224abe0037d99adbce73c315e13238f9', 'c9dfdcb50a3d84f2b6a4771dcb7c2ceb19e7d281','2');


/* admin    admin*/
INSERT INTO `user` (`user_id`, `name`, `password`, `token`, `ownapps`) VALUES (null, 'admin', 'd033e22ae348aeb5660fc2140aec35850c4da997', 'f28d164d23291c732f64134e6b7d92be3ff8b1b3','2');



