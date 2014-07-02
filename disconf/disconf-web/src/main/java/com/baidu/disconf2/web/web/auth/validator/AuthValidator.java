package com.baidu.disconf2.web.web.auth.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.disconf2.web.service.sign.service.SignMgr;
import com.baidu.disconf2.web.service.user.service.UserMgr;

/**
 * 权限验证
 * 
 * @author liaoqiqi
 * @version 2014-7-2
 */
@Component
public class AuthValidator {

    @Autowired
    private SignMgr signMgr;

    @Autowired
    private UserMgr userMgr;

}
