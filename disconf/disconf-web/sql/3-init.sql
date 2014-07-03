

INSERT INTO `env` VALUES (1, 'rd');
INSERT INTO `env` VALUES (2, 'qa');
INSERT INTO `env` VALUES (3, 'local');


INSERT INTO `app` VALUES (1, 'disconf_testcase', 'disconf测试用例使用', '20140415155127', '20140415155127');
INSERT INTO `app` VALUES (2, 'disconf_demo', 'disconf demo', '99991231235959', '99991231235959');


INSERT INTO `config` VALUES (1, 0, 'confA.properties', 'varA=400000\r\nvarA2=500000', 1, '1_0_0_0', 1, '20140415155127', '20140415155127');
INSERT INTO `config` VALUES (2, 1, 'keyA', '8888', 1, '1_0_0_0', 1, '99991231235959', '99991231235959');
INSERT INTO `config` VALUES (3, 0, 'redis.properties', 'host=127.0.0.1\r\nport=8080', 2, '1_0_0_0', 1, '99991231235959', '99991231235959');
INSERT INTO `config` VALUES (4, 0, 'coefficients.properties', 'baiFaCoe=1.3\r\nyuErBaoCoe=1.2', 2, '1_0_0_0', 1, '99991231235959', '99991231235959');
INSERT INTO `config` VALUES (5, 1, 'moneyInvest', '10000', 2, '1_0_0_0', 1, '99991231235959', '99991231235959');
