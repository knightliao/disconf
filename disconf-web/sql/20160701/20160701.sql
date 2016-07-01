ALTER TABLE `config_history`
ADD COLUMN `update_by` BIGINT(20) NULL DEFAULT NULL
AFTER `create_time`;
