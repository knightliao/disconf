package com.baidu.disconf.client.scan.inner.statically.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.baidu.disconf.client.common.model.DisConfCommonModel;
import com.baidu.disconf.client.common.model.DisconfCenterBaseModel;
import com.baidu.disconf.client.common.model.DisconfCenterFile;
import com.baidu.disconf.client.config.DisClientConfig;
import com.baidu.disconf.client.config.DisClientSysConfig;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.core.common.path.DisconfWebPathMgr;

/**
 * @author liaoqiqi
 * @version 2014-9-9
 */
public class StaticScannerMgrImplBase {

    /**
     * env/version 默认是应用整合设置的，但用户可以在配置中更改它
     *
     * @return
     */
    protected static DisConfCommonModel makeDisConfCommonModel(String env, String version) {

        DisConfCommonModel disConfCommonModel = new DisConfCommonModel();

        // app
        disConfCommonModel.setApp(DisClientConfig.getInstance().APP);

        // env
        if (!env.isEmpty()) {
            disConfCommonModel.setEnv(env);
        } else {
            disConfCommonModel.setEnv(DisClientConfig.getInstance().ENV);
        }

        // version
        if (!version.isEmpty()) {
            disConfCommonModel.setVersion(version);
        } else {
            disConfCommonModel.setVersion(DisClientConfig.getInstance().VERSION);
        }

        return disConfCommonModel;
    }

    /**
     *
     */
    public static List<DisconfCenterBaseModel> getDisconfCenterFiles(Set<String> fileNameList) {

        List<DisconfCenterBaseModel> disconfCenterFiles = new ArrayList<DisconfCenterBaseModel>();

        for (String fileName : fileNameList) {

            fileName = fileName.trim();

            DisconfCenterFile disconfCenterFile = new DisconfCenterFile();

            //
            // file name
            disconfCenterFile.setFileName(fileName);

            //
            // disConfCommonModel
            DisConfCommonModel disConfCommonModel = makeDisConfCommonModel("", "");
            disconfCenterFile.setDisConfCommonModel(disConfCommonModel);

            // Remote URL
            String url = DisconfWebPathMgr
                             .getRemoteUrlParameter(DisClientSysConfig.getInstance().CONF_SERVER_STORE_ACTION,
                                                       disConfCommonModel.getApp(), disConfCommonModel.getVersion(),
                                                       disConfCommonModel.getEnv(), disconfCenterFile.getFileName(),
                                                       DisConfigTypeEnum.FILE);
            disconfCenterFile.setRemoteServerUrl(url);

            disconfCenterFiles.add(disconfCenterFile);
        }

        return disconfCenterFiles;
    }

}
