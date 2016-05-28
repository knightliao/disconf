package com.baidu.disconf.web.web.config.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baidu.disconf.web.service.app.bo.App;
import com.baidu.disconf.web.service.app.service.AppMgr;
import com.baidu.disconf.web.service.config.form.ConfForm;
import com.baidu.disconf.web.service.config.service.ConfigMgr;
import com.baidu.disconf.web.service.env.bo.Env;
import com.baidu.disconf.web.service.env.service.EnvMgr;
import com.baidu.disconf.web.web.config.dto.ConfigFullModel;

/**
 * @author knightliao
 */
@Service
public class ConfigValidator4Fetch {

    @Autowired
    private AppMgr appMgr;

    @Autowired
    private EnvMgr envMgr;

    @Autowired
    private ConfigMgr configMgr;

    /**
     * 此接口是客户的接口，非常 重要，目前没有权限的控制
     *
     * @param confForm
     */
    public ConfigFullModel verifyConfForm(ConfForm confForm) throws Exception {

        //
        // app
        //
        if (StringUtils.isEmpty(confForm.getApp())) {
            throw new Exception("app is empty");
        }

        App app = appMgr.getByName(confForm.getApp());
        if (app == null) {
            throw new Exception("app " + confForm.getApp() + " doesn't exist in db.");
        }

        //
        // env
        //
        if (StringUtils.isEmpty(confForm.getEnv())) {
            throw new Exception("app is empty");
        }

        Env env = envMgr.getByName(confForm.getEnv());
        if (env == null) {
            throw new Exception("env " + confForm.getEnv() + " doesn't exist in db.");
        }

        //
        // key
        //
        if (StringUtils.isEmpty(confForm.getKey())) {
            throw new Exception("key is empty");
        }

        //
        // version
        //
        if (StringUtils.isEmpty(confForm.getVersion())) {
            throw new Exception("version is empty");
        }

        return new ConfigFullModel(app, env, confForm.getVersion(), confForm.getKey());
    }
}
