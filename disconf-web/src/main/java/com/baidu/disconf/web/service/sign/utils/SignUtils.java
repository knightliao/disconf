package com.baidu.disconf.web.service.sign.utils;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

public class SignUtils {

    /**
     * 生成密码， 使用shaHex加密
     *
     * @return
     */
    public static String createPassword(String password) {

        String data = DigestUtils.shaHex(password);
        return data;
    }

    /**
     * 生成token ,使用 UUID + 手机生成
     *
     * @return
     */
    public static String createToken(String phone) {

        String uuid = UUID.randomUUID().toString();

        String data = DigestUtils.shaHex(uuid + phone);
        return data;
    }
}
