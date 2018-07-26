package com.baidu.disconf.client.scan.inner.statically.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.baidu.disconf.client.common.constants.SupportFileTypeEnum;
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
public class StaticScannerNonAnnotationFileMgrImpl extends StaticScannerMgrImplBase implements StaticScannerMgr {

    /**
     *
     */
    @Override
    public void scanData2Store(ScanStaticModel scanModel) {

        //
        //
        //
        List<DisconfCenterBaseModel> disconfCenterBaseModels = getDisconfCenterFiles(scanModel.getJustHostFiles());

        DisconfStoreProcessorFactory.getDisconfStoreFileProcessor().transformScanData(disconfCenterBaseModels);
    }

    /**
     *
     */
    public static void scanData2Store(String fileName) {

        DisconfCenterBaseModel disconfCenterBaseModel =
                StaticScannerNonAnnotationFileMgrImpl.getDisconfCenterFile(fileName);

        DisconfStoreProcessorFactory.getDisconfStoreFileProcessor().transformScanData(disconfCenterBaseModel);
    }

    /**
     *
     */
    @Override
    public void exclude(Set<String> keySet) {
        DisconfStoreProcessorFactory.getDisconfStoreFileProcessor().exclude(keySet);
    }

    /**
     *
     */
    public static List<DisconfCenterBaseModel> getDisconfCenterFiles(Set<String> fileNameList) {

        List<DisconfCenterBaseModel> disconfCenterFiles = new ArrayList<DisconfCenterBaseModel>();

        for (String fileName : fileNameList) {

            disconfCenterFiles.add(getDisconfCenterFile(fileName));
        }

        return disconfCenterFiles;
    }

    /**
     *
     */
    public static DisconfCenterBaseModel getDisconfCenterFile(String fileName) {

        DisconfCenterFile disconfCenterFile = new DisconfCenterFile();

        fileName = fileName.trim();

        //
        // file name
        disconfCenterFile.setFileName(fileName);

        // 非注解式
        disconfCenterFile.setIsTaggedWithNonAnnotationFile(true);

        // file type
        disconfCenterFile.setSupportFileTypeEnum(SupportFileTypeEnum.getByFileName(fileName));

        //
        // disConfCommonModel
        DisConfCommonModel disConfCommonModel = makeDisConfCommonModel("", "", "");
        disconfCenterFile.setDisConfCommonModel(disConfCommonModel);

        // Remote URL
        String url = DisconfWebPathMgr.getRemoteUrlParameter(DisClientSysConfig.getInstance().CONF_SERVER_STORE_ACTION,
                disConfCommonModel.getApp(),
                disConfCommonModel.getVersion(),
                disConfCommonModel.getEnv(),
                disconfCenterFile.getFileName(),
                DisConfigTypeEnum.FILE);
        disconfCenterFile.setRemoteServerUrl(url);

        return disconfCenterFile;
    }

}
