USE `disconf`;

INSERT INTO `env` (`env_id`, `name`) VALUES (1, 'rd');
INSERT INTO `env` (`env_id`, `name`) VALUES (2, 'qa');
INSERT INTO `env` (`env_id`, `name`) VALUES (3, 'local');
INSERT INTO `env` (`env_id`, `name`) VALUES (4, 'online');


INSERT INTO `app` (`app_id`, `name`, `description`, `create_time`, `update_time`) VALUES (2, 'disconf_demo', 'disconf demo', '99991231235959', '99991231235959');

INSERT INTO `config` (`config_id`, `type`, `name`, `value`, `app_id`, `version`, `env_id`, `create_time`, `update_time`) VALUES (4, 0, 'coefficients.properties', 'coe.baiFaCoe=1.3\r\ncoe.yuErBaoCoe=1.2\r\n', 2, '1_0_0_0', 1, '99991231235959', '20140729194923');
INSERT INTO `config` (`config_id`, `type`, `name`, `value`, `app_id`, `version`, `env_id`, `create_time`, `update_time`) VALUES (5, 1, 'moneyInvest', '10000', 2, '1_0_0_0', 1, '99991231235959', '20140902183514');
INSERT INTO `config` (`config_id`, `type`, `name`, `value`, `app_id`, `version`, `env_id`, `create_time`, `update_time`) VALUES (6, 0, 'remote.properties', 'remoteHost=127.0.0.1\r\nremotePort=8081', 2, '1_0_0_0', 1, '20140729174707', '20140804233309');
INSERT INTO `config` (`config_id`, `type`, `name`, `value`, `app_id`, `version`, `env_id`, `create_time`, `update_time`) VALUES (7, 1, 'discountRate', '0.5', 2, '1_0_0_0', 1, '20140801142833', '20140905130141');
INSERT INTO `config` (`config_id`, `type`, `name`, `value`, `app_id`, `version`, `env_id`, `create_time`, `update_time`) VALUES (12, 0, 'redis.properties', 'redis.host=10.48.57.42\r\nredis.port=8310', 2, '1_0_0_0', 1, '20140811172327', '20140811172327');
INSERT INTO `config` (`config_id`, `type`, `name`, `value`, `app_id`, `version`, `env_id`, `create_time`, `update_time`) VALUES (16, 0, 'static.properties', 'staticVar=147', 2, '1_0_0_0', 1, '20140814202654', '20140905145616');
INSERT INTO `config` (`config_id`, `type`, `name`, `value`, `app_id`, `version`, `env_id`, `create_time`, `update_time`) VALUES (17, 1, 'staticItem', '30', 2, '1_0_0_0', 1, '20140814210709', '20140814211054');
INSERT INTO `config` (`config_id`, `type`, `name`, `value`, `app_id`, `version`, `env_id`, `create_time`, `update_time`) VALUES (29, 0, 'empty.properties', 'redis.host=10.48.57.42\r\nredis.port=8310', 2, '1_0_0_0', 1, '20140909164001', '20140909164125');
INSERT INTO `config` (`config_id`, `type`, `name`, `value`, `app_id`, `version`, `env_id`, `create_time`, `update_time`) VALUES (48, 0, 'atomserverl.properties', 'server=10.81.11.243:16600,10.81.11.243:16602,10.81.11.243:16603\nretry=4\n', 2, '1_0_0_0', 1, '20140911223117', '20140911223313');


/* 密码是 MhxzKhl5258 */
INSERT INTO `user` (`user_id`, `name`, `password`, `token`) VALUES (1, 'testUser1', 'd34a9fe14b5612cc2e700e557f0d8d0e751ba9ac', '34ab48843f1908cfeef15f459f6d1736362aa8e0');
/*  密码是 MhxzKhl7011 */
INSERT INTO `user` (`user_id`, `name`, `password`, `token`) VALUES (2, 'testUser2', 'd5d5f4689232b7a6514f8d2700ba40d3a608f6d2', 'c59af6d187b423522fa15d2a97fd7c44ce98cc7a');
/*  密码是 MhxzKhl9362 */
INSERT INTO `user` (`user_id`, `name`, `password`, `token`) VALUES (3, 'testUser3', 'fbd03108ecd54d77692e49a2b21a2ded6b0a166a', 'ec626ba96117299ca0524be9c56cc195e1784922');
/*  密码是 MhxzKhl7603 */
INSERT INTO `user` (`user_id`, `name`, `password`, `token`) VALUES (4, 'testUser4', '59eb095e6f95c6b40fe3f049058387bd64e46809', '82fdf59318de643a5d7ceffa764c7e29cdd5a0e4');
/*  密码是 MhxzKhl2077 */
INSERT INTO `user` (`user_id`, `name`, `password`, `token`) VALUES (5, 'testUser5', 'e435b4606e61a4b0d934c9d350ec27da3c0ddc4e', '90ac0c85a9581cb762461dcf35dca6f182dc1805');
/*  dancai   Asd123 */
INSERT INTO `user` (`user_id`, `name`, `password`, `token`) VALUES (8, 'dancai', '1fae3ce0905862435d03af3ce72aa80d4463f445', 'ce9c0985dfab066f1cf587ab8da3244e0c1158dd');
