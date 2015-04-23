use `disconf`;

update `app`
    set `emails`='liaoqiqi@baidu.com;liaoqiqi@baidu.com;xiaoxingxing01@baidu.com;nieyunfei@baidu.com;zhengbingwen@baidu.com;gexiao@baidu.com' where app_id = 4;

/* mobiledsp	mobiledsp_Mhx*/
INSERT INTO `user` (`user_id`, `name`, `password`, `token`, `ownapps`,`role_id`) VALUES (null, 'mobiledsp', '0855b44a368e44dc6e6825532073b29a368584af', '132069654193f802203d1c6c86e753ecede698f6','4', '1');
