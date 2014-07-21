USE `disconf`;

INSERT INTO `env` VALUES (1, 'rd');
INSERT INTO `env` VALUES (2, 'qa');
INSERT INTO `env` VALUES (3, 'local');


INSERT INTO `app` VALUES (2, 'disconf_demo', 'disconf demo', '99991231235959', '99991231235959');


INSERT INTO `config` VALUES (1, 0, 'confA.properties', 'varA=400000\r\nvarA2=500000', 1, '1_0_0_0', 1, '20140415155127', '20140415155127');
INSERT INTO `config` VALUES (2, 1, 'keyA', '8888', 1, '1_0_0_0', 1, '99991231235959', '99991231235959');
INSERT INTO `config` VALUES (3, 0, 'redis.properties', 'host=127.0.0.1\r\nport=8080', 2, '1_0_0_0', 1, '99991231235959', '99991231235959');
INSERT INTO `config` VALUES (4, 0, 'coefficients.properties', 'baiFaCoe=1.3\r\nyuErBaoCoe=1.2', 2, '1_0_0_0', 1, '99991231235959', '99991231235959');
INSERT INTO `config` VALUES (5, 1, 'moneyInvest', '10000', 2, '1_0_0_0', 1, '99991231235959', '99991231235959');


INSERT INTO `user` (`user_id`, `name`, `password`, `token`) VALUES (1, 'testUser1', 'c20f327928d0142c3253d585cf68460db7590b83', '59311b56b3a07eb20c9587de51aa04b78d55944c');
INSERT INTO `user` (`user_id`, `name`, `password`, `token`) VALUES (2, 'testUser2', 'e9f4148b4ac3fee71a7d409f41c9391c213a4dd3', '2bacc0d0f8714882773f28c37514c12817a4466c');
INSERT INTO `user` (`user_id`, `name`, `password`, `token`) VALUES (3, 'testUser3', '435a82d6c7077249cba339ace53320fe0af10ea8', '887687dfb655d39481680734ae300c5fda5753e6');
INSERT INTO `user` (`user_id`, `name`, `password`, `token`) VALUES (4, 'testUser4', '8779cff3ec2fee4302fbcb83ca6323600308f1aa', '3c9359d18a032dfb03ce4e79b2ed721fd11e9308');
INSERT INTO `user` (`user_id`, `name`, `password`, `token`) VALUES (5, 'testUser5', 'd9dd0fdaaee964982cf984fcd052b31db477ccee', 'deb17ce553d3cb10e56f038a2dea3dbe7f815ab5');
