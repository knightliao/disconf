package com.baidu.disconf.web.service.user.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf.web.service.user.service.AuthMgr;
import com.baidu.disconf.web.service.user.service.UserInnerMgr;

/**
 * 
 * @author knightliao
 * 
 */
@Service
public class AuthMgrImpl implements AuthMgr {

    @Autowired
    private UserInnerMgr userInnerMgr;

    @Override
    public boolean verifyApp4CurrentUser(Long appId) {

        Set<Long> idsLongs = userInnerMgr.getVisitorAppIds();
        if (idsLongs.contains(appId)) {
            return true;
        } else {
            return false;
        }

    }

}
