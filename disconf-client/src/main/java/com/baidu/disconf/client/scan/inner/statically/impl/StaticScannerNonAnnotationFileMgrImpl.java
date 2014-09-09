package com.baidu.disconf.client.scan.inner.statically.impl;

import java.util.ArrayList;
import java.util.List;

import com.baidu.disconf.client.common.model.DisConfCommonModel;
import com.baidu.disconf.client.common.model.DisconfCenterBaseModel;
import com.baidu.disconf.client.common.model.DisconfCenterFile;
import com.baidu.disconf.client.config.DisClientSysConfig;
import com.baidu.disconf.client.scan.inner.statically.StaticScannerMgr;
import com.baidu.disconf.client.scan.inner.statically.model.ScanStaticModel;
import com.baidu.disconf.client.store.DisconfStoreProcessorFactory;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.core.common.path.DisconfWebPathMgr;

/**
 * 非注解配置文件的扫描器
 */
public class StaticScannerNonAnnotationFileMgrImpl extends
        StaticScannerMgrImplBase implements StaticScannerMgr {

    /**
     * 
     */
    @Override
    public void scanData2Store(ScanStaticModel scanModel) {

        //
        //
        //
        List<DisconfCenterBaseModel> disconfCenterBaseModels = getDisconfCenterFiles(scanModel);

        DisconfStoreProcessorFactory.getDisconfStoreFileProcessor()
                .transformScanData(disconfCenterBaseModels);
    }

    /**
     * 
     */
    private List<DisconfCenterBaseModel> getDisconfCenterFiles(
            ScanStaticModel scanModel) {

        List<DisconfCenterBaseModel> disconfCenterFiles = new ArrayList<DisconfCenterBaseModel>();

        for (String fileName : scanModel.getNonAnnotationFileSet()) {

            DisconfCenterFile disconfCenterFile = new DisconfCenterFile();

            //
            // file name
            disconfCenterFile.setFileName(fileName);

            //
            // disConfCommonModel
            DisConfCommonModel disConfCommonModel = makeDisConfCommonModel("",
                    "");
            disconfCenterFile.setDisConfCommonModel(disConfCommonModel);

            // Remote URL
            String url = DisconfWebPathMgr.getRemoteUrlParameter(
                    DisClientSysConfig.getInstance().CONF_SERVER_STORE_ACTION,
                    disConfCommonModel.getApp(),
                    disConfCommonModel.getVersion(),
                    disConfCommonModel.getEnv(),
                    disconfCenterFile.getFileName(), DisConfigTypeEnum.FILE);
            disconfCenterFile.setRemoteServerUrl(url);

            disconfCenterFiles.add(disconfCenterFile);
        }

        return disconfCenterFiles;
    }
}
