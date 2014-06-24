package com.baidu.disconf2.web.web.config.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf2.web.service.app.bo.App;
import com.baidu.disconf2.web.service.app.service.AppMgr;
import com.baidu.disconf2.web.service.config.bo.Config;
import com.baidu.disconf2.web.service.config.form.ConfForm;
import com.baidu.disconf2.web.service.config.service.ConfigMgr;
import com.baidu.disconf2.web.service.env.bo.Env;
import com.baidu.disconf2.web.service.env.service.EnvMgr;
import com.baidu.dsp.common.exception.FieldException;
import com.baidu.ub.common.utils.StringUtils;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Service
public class ConfigValidator {

    @Autowired
    private AppMgr appMgr;

    @Autowired
    private EnvMgr envMgr;

    @Autowired
    private ConfigMgr configMgr;

    public static class ConfigModel {

        private Long appId;
        private Long envId;
        private String version;
        private String key;

        public ConfigModel(Long appId, Long envId, String version, String key) {
            super();
            this.appId = appId;
            this.envId = envId;
            this.version = version;
            this.key = key;
        }

        public Long getAppId() {
            return appId;
        }

        public void setAppId(Long appId) {
            this.appId = appId;
        }

        public Long getEnvId() {
            return envId;
        }

        public void setEnvId(Long envId) {
            this.envId = envId;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "ConfigModel [appId=" + appId + ", envId=" + envId
                    + ", version=" + version + ", key=" + key + "]";
        }
    }

    /**
     * 
     * @param confForm
     */
    public ConfigModel verifyConfForm(ConfForm confForm) throws Exception {

        //
        // app
        //
        if (StringUtils.isEmpty(confForm.getApp())) {
            throw new Exception("app is empty");
        }

        App app = appMgr.getByName(confForm.getApp());
        if (app == null) {
            throw new Exception("app " + confForm.getApp()
                    + " doesn't exist in db.");
        }

        //
        // env
        //
        if (StringUtils.isEmpty(confForm.getEnv())) {
            throw new Exception("app is empty");
        }

        Env env = envMgr.getByName(confForm.getEnv());
        if (env == null) {
            throw new Exception("env " + confForm.getEnv()
                    + " doesn't exist in db.");
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

        return new ConfigModel(app.getId(), env.getId(), confForm.getVersion(),
                confForm.getKey());
    }

    /**
     * 校验
     * 
     * @param userId
     */
    public void valideConfigExist(Long id) {

        try {

            Config config = configMgr.getConfigById(id);

            if (config == null) {
                throw new Exception();
            }

        } catch (Exception e) {

            throw new FieldException("configId", "userid.not.exist", e);
        }
    }

    /**
     * 校验更新 配置值
     * 
     * @param userId
     */
    public void validateUpdateItem(Long configId, String value) {

        //
        // config
        //
        valideConfigExist(configId);

        //
        // value
        //
        try {

            if (StringUtils.isEmpty(value)) {
                throw new Exception();
            }

        } catch (Exception e) {

            throw new FieldException("value", "value.null", e);
        }

    }

    /**
     * 判断配置是否更新
     * 
     * @return
     */
    public boolean isValueUpdate(Long configId, String newValue) {

        //
        // 判断值有没有更新
        //
        String oldValue = configMgr.getValue(configId);

        if (newValue.equals(oldValue)) {
            return false;
        }
        return true;
    }
}
