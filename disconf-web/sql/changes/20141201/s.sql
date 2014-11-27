ALTER TABLE `app`
    ADD COLUMN `emails` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '邮箱列表逗号分隔' AFTER `update_time`;
