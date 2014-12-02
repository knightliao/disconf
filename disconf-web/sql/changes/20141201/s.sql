ALTER TABLE `app`
    ADD COLUMN `emails` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '邮箱列表逗号分隔' AFTER `update_time`;

ALTER TABLE `user`
    ADD COLUMN `ownapps` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '能操作的APPID,逗号分隔' AFTER `token`;
