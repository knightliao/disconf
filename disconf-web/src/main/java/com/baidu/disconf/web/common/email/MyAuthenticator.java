package com.baidu.disconf.web.common.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author liaoqiqi
 * @version 2014-3-2
 */
public class MyAuthenticator extends Authenticator {

    String username = null;
    String password = null;

    // 通过构造方法接受外部传入的用户信息
    public MyAuthenticator(String user, String pass) {
        this.username = user;
        this.password = pass;
    }

    // 覆盖Authenticator类中的getPasswordAuthentication方法
    protected PasswordAuthentication getPasswordAuthentication() {
        // 使用外部传入的用户名和密码创建对象
        return new PasswordAuthentication(username, password);
    }
}