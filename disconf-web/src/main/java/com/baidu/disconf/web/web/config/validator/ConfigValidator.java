package com.baidu.disconf.web.web.config.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.web.service.app.bo.App;
import com.baidu.disconf.web.service.app.service.AppMgr;
import com.baidu.disconf.web.service.config.bo.Config;
import com.baidu.disconf.web.service.config.form.ConfForm;
import com.baidu.disconf.web.service.config.form.ConfNewForm;
import com.baidu.disconf.web.service.config.form.ConfNewItemForm;
import com.baidu.disconf.web.service.config.service.ConfigMgr;
import com.baidu.disconf.web.service.env.bo.Env;
import com.baidu.disconf.web.service.env.service.EnvMgr;
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
    public Config valideConfigExist(Long id) {

        try {

            Config config = configMgr.getConfigById(id);

            if (config == null) {
                throw new Exception();
            }

            return config;

        } catch (Exception e) {

            throw new FieldException("configId", "config.id.not.exist", e);
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

            throw new FieldException("value", "conf.item.value.null", e);
        }

    }

    /**
     * 校验更新 配置文件
     * 
     * @param userId
     */
    public void validateUpdateFile(Long configId, String fileName) {

        //
        // config
        //
        Config config = valideConfigExist(configId);

        //
        // value
        //
        try {

            if (!config.getName().equals(fileName)) {
                throw new Exception();
            }

        } catch (Exception e) {

            throw new FieldException("value", "conf.file.name.not.equal", e);
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

    /**
     * 校验新建 配置
     * 
     * @param userId
     */
    public void validateNew(ConfNewItemForm confNewForm,
            DisConfigTypeEnum disConfigTypeEnum) {

        //
        // app
        //
        App app = appMgr.getById(confNewForm.getAppId());
        if (app == null) {
            throw new FieldException(ConfNewForm.APPID, "app.not.exist", null);
        }

        //
        // env
        //
        Env env = envMgr.getById(confNewForm.getEnvId());
        if (env == null) {
            throw new FieldException(ConfNewForm.ENVID, "env.not.exist", null);
        }

        //
        // key
        //
        Config config = configMgr.getConfByParameter(app.getId(), env.getId(),
                confNewForm.getVersion(), confNewForm.getKey(),
                disConfigTypeEnum);
        if (config != null) {
            throw new FieldException(ConfNewItemForm.KEY, "key.exist", null);
        }

    }

    /**
     * 验证删除
     */
    public void validateDelete(Long configId) {

        Config config = configMgr.getConfigById(configId);
        if (config == null) {
            throw new FieldException("configId", "config.not.exist", null);
        }
    }

}
