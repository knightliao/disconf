package com.baidu.disconf.web.service.config.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.disconf.core.common.constants.Constants;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.core.common.json.ValueVo;
import com.baidu.disconf.web.innerapi.zookeeper.ZooKeeperDriver;
import com.baidu.disconf.web.service.app.bo.App;
import com.baidu.disconf.web.service.app.service.AppMgr;
import com.baidu.disconf.web.service.config.bo.Config;
import com.baidu.disconf.web.service.config.dao.ConfigDao;
import com.baidu.disconf.web.service.config.form.ConfListForm;
import com.baidu.disconf.web.service.config.form.ConfNewItemForm;
import com.baidu.disconf.web.service.config.service.ConfigMgr;
import com.baidu.disconf.web.service.config.utils.ConfigUtils;
import com.baidu.disconf.web.service.config.vo.ConfListVo;
import com.baidu.disconf.web.service.env.bo.Env;
import com.baidu.disconf.web.service.env.service.EnvMgr;
import com.baidu.disconf.web.service.zookeeper.config.ZooConfig;
import com.baidu.disconf.web.service.zookeeper.dto.ZkDisconfData;
import com.baidu.disconf.web.service.zookeeper.dto.ZkDisconfData.ZkDisconfDataItem;
import com.baidu.disconf.web.service.zookeeper.service.ZkDeployMgr;
import com.baidu.dsp.common.constant.DataFormatConstants;
import com.baidu.dsp.common.utils.DataTransfer;
import com.baidu.dsp.common.utils.ServiceUtil;
import com.baidu.ub.common.db.DaoPageResult;
import com.github.knightliao.apollo.utils.data.GsonUtils;
import com.github.knightliao.apollo.utils.io.OsUtil;
import com.github.knightliao.apollo.utils.time.DateUtils;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Service
public class ConfigMgrImpl implements ConfigMgr {

    protected static final Logger LOG = LoggerFactory
            .getLogger(ConfigMgrImpl.class);

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private AppMgr appMgr;

    @Autowired
    private EnvMgr envMgr;

    @Autowired
    private ZooKeeperDriver zooKeeperDriver;

    @Autowired
    private ZooConfig zooConfig;

    @Autowired
    private ZkDeployMgr zkDeployMgr;

    /**
     * 根据详细参数获取配置返回
     */
    public ValueVo getConfItemByParameter(Long appId, Long envId,
            String version, String key) {

        Config config = configDao.getByParameter(appId, envId, version, key,
                DisConfigTypeEnum.ITEM);
        if (config == null) {
            return ConfigUtils.getErrorVo("cannot find this config");
        }

        ValueVo valueVo = new ValueVo();
        valueVo.setValue(config.getValue());
        valueVo.setStatus(Constants.OK);

        return valueVo;
    }

    /**
     * 根据详细参数获取配置
     */
    @Override
    public Config getConfByParameter(Long appId, Long envId, String env,
            String key, DisConfigTypeEnum disConfigTypeEnum) {

        Config config = configDao.getByParameter(appId, envId, env, key,
                disConfigTypeEnum);
        return config;
    }

    /**
     * 根据APPid获取其版本列表
     */
    @Override
    public List<String> getVersionListByAppEnv(Long appId, Long envId) {

        List<String> versionList = new ArrayList<String>();

        List<Config> configs = configDao.getConfByAppEnv(appId, envId);

        for (Config config : configs) {

            if (!versionList.contains(config.getVersion())) {
                versionList.add(config.getVersion());
            }
        }

        return versionList;
    }

    /**
     * 配置文件的整合
     * 
     * @param confListForm
     * @return
     */
    public List<File> getDisonfFileList(ConfListForm confListForm) {

        List<Config> configList = configDao.getConfigList(
                confListForm.getAppId(), confListForm.getEnvId(),
                confListForm.getVersion());

        // 时间作为当前文件夹
        String curTime = DateUtils.format(new Date(),
                DataFormatConstants.COMMON_TIME_FORMAT);
        curTime = "tmp" + File.separator + curTime;
        OsUtil.makeDirs(curTime);

        List<File> files = new ArrayList<File>();
        for (Config config : configList) {

            if (config.getType().equals(DisConfigTypeEnum.FILE.getType())) {

                File file = new File(curTime, config.getName());
                try {
                    FileUtils.writeByteArrayToFile(file, config.getValue()
                            .getBytes());
                } catch (IOException e) {
                    LOG.warn(e.toString());
                }

                files.add(file);
            }
        }

        return files;
    }

    /**
     * 配置列表 含有ZK信息
     */
    @Override
    public DaoPageResult<ConfListVo> getConfigListWithZk(
            ConfListForm confListForm) {

        //
        // 数据据结果
        //
        DaoPageResult<Config> configList = configDao.getConfigList(
                confListForm.getAppId(), confListForm.getEnvId(),
                confListForm.getVersion(), confListForm.getPage());

        //
        //
        //

        final App app = appMgr.getById(confListForm.getAppId());
        final Env env = envMgr.getById(confListForm.getEnvId());

        //
        //
        //
        final Map<String, ZkDisconfData> zkDataMap = zkDeployMgr
                .getZkDisconfDataMap(app.getName(), env.getName(),
                        confListForm.getVersion());

        //
        // 进行转换
        //
        DaoPageResult<ConfListVo> configListVo = ServiceUtil.getResult(
                configList, new DataTransfer<Config, ConfListVo>() {

                    @Override
                    public ConfListVo transfer(Config input) {

                        String appNameString = app.getName();
                        String envName = env.getName();

                        ConfListVo configListVo = convert(input, appNameString,
                                envName, zkDataMap);

                        return configListVo;
                    }
                });

        return configListVo;
    }

    /**
     * 配置列表
     */
    @Override
    public DaoPageResult<ConfListVo> getConfigList(ConfListForm confListForm) {

        //
        // 数据据结果
        //
        DaoPageResult<Config> configList = configDao.getConfigList(
                confListForm.getAppId(), confListForm.getEnvId(),
                confListForm.getVersion(), confListForm.getPage());

        //
        //
        //

        final App app = appMgr.getById(confListForm.getAppId());
        final Env env = envMgr.getById(confListForm.getEnvId());

        //
        // 进行转换
        //
        DaoPageResult<ConfListVo> configListVo = ServiceUtil.getResult(
                configList, new DataTransfer<Config, ConfListVo>() {

                    @Override
                    public ConfListVo transfer(Config input) {

                        String appNameString = app.getName();
                        String envName = env.getName();

                        ConfListVo configListVo = convert(input, appNameString,
                                envName, null);

                        return configListVo;
                    }
                });

        return configListVo;
    }

    /**
     * 转换成配置返回
     * 
     * @param config
     * @return
     */
    private ConfListVo convert(Config config, String appNameString,
            String envName, Map<String, ZkDisconfData> zkDataMap) {

        ConfListVo confListVo = new ConfListVo();

        confListVo.setConfigId(config.getId());
        confListVo.setAppId(config.getAppId());
        confListVo.setAppName(appNameString);
        confListVo.setEnvName(envName);
        confListVo.setEnvId(config.getEnvId());
        confListVo.setCreateTime(config.getCreateTime());
        confListVo.setModifyTime(config.getUpdateTime().substring(0, 12));
        confListVo.setKey(config.getName());
        confListVo.setValue(config.getValue());
        confListVo.setVersion(config.getVersion());
        confListVo.setType(DisConfigTypeEnum.getByType(config.getType())
                .getModelName());
        confListVo.setTypeId(config.getType());

        //
        //
        //
        if (zkDataMap != null) {

            ZkDisconfData zkDisconfData = zkDataMap.get(config.getName());
            if (zkDisconfData != null) {
                confListVo.setMachineSize(zkDisconfData.getData().size());

                List<ZkDisconfDataItem> datalist = zkDisconfData.getData();
                int errorNum = 0;
                for (ZkDisconfDataItem zkDisconfDataItem : datalist) {

                    if (config.getType().equals(
                            DisConfigTypeEnum.FILE.getType())) {

                        List<String> errorKeyList = compareConifg(
                                zkDisconfDataItem.getValue(), config.getValue());

                        if (errorKeyList.size() != 0) {
                            zkDisconfDataItem.setErrorList(errorKeyList);
                            errorNum++;
                        }
                    } else {

                        //
                        // 配置项
                        //

                        if (zkDisconfDataItem.getValue().trim()
                                .equals(config.getValue().trim())) {

                        } else {
                            List<String> errorKeyList = new ArrayList<String>();
                            errorKeyList.add(config.getValue().trim());
                            zkDisconfDataItem.setErrorList(errorKeyList);
                            errorNum++;
                        }
                    }
                }

                confListVo.setErrorNum(errorNum);
                confListVo.setMachineList(datalist);
            }
        }

        return confListVo;
    }

    /**
     * 
     */
    private List<String> compareConifg(String zkData, String dbData) {

        List<String> errorKeyList = new ArrayList<String>();

        Properties prop = new Properties();
        try {
            prop.load(IOUtils.toInputStream(dbData, "UTF-8"));
        } catch (IOException e) {
            LOG.error(e.toString());
            errorKeyList.add(zkData);
            return errorKeyList;
        }

        Map<String, String> zkMap = GsonUtils.parse2Map(zkData);
        for (String keyInZk : zkMap.keySet()) {

            Object valueInDb = prop.get(keyInZk);
            String zkDataStr = zkMap.get(keyInZk);

            try {

                if ((zkDataStr == null && valueInDb != null)
                        || (zkDataStr != null && valueInDb == null)) {
                    errorKeyList.add(keyInZk);

                } else {

                    if (!zkDataStr.equals(valueInDb.toString().trim())) {
                        errorKeyList.add(keyInZk);
                    }
                }

            } catch (Exception e) {

                LOG.warn(e.toString() + " ; " + keyInZk + " ; "
                        + zkMap.get(keyInZk) + " ; " + valueInDb);
            }
        }

        return errorKeyList;
    }

    /**
     * 根据 配置ID获取配置返回
     */
    @Override
    public ConfListVo getConfVo(Long configId) {
        Config config = configDao.get(configId);

        App app = appMgr.getById(config.getAppId());
        Env env = envMgr.getById(config.getEnvId());

        return convert(config, app.getName(), env.getName(), null);
    }

    /**
     * 根据配置ID获取配置
     */
    @Override
    public Config getConfigById(Long configId) {

        return configDao.get(configId);
    }

    /**
     * 更新 配置项/配置文件 的值
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void updateItemValue(Long configId, String value) {

        //
        // 配置数据库的值
        //
        configDao.updateValue(configId, value);
    }

    /**
     * 通知Zookeeper, 失败时不回滚数据库,通过监控来解决分布式不一致问题
     */
    @Override
    public void notifyZookeeper(Long configId) {

        ConfListVo confListVo = getConfVo(configId);

        if (confListVo.getTypeId().equals(DisConfigTypeEnum.FILE.getType())) {

            zooKeeperDriver.notifyNodeUpdate(confListVo.getAppName(),
                    confListVo.getEnvName(), confListVo.getVersion(),
                    confListVo.getKey(),
                    GsonUtils.toJson(confListVo.getValue()),
                    DisConfigTypeEnum.FILE);

        } else {

            zooKeeperDriver.notifyNodeUpdate(confListVo.getAppName(),
                    confListVo.getEnvName(), confListVo.getVersion(),
                    confListVo.getKey(), confListVo.getValue(),
                    DisConfigTypeEnum.ITEM);
        }

    }

    /**
     * 获取配置值
     */
    @Override
    public String getValue(Long configId) {
        return configDao.getValue(configId);
    }

    /**
     * 新建配置
     */
    @Override
    public void newConfig(ConfNewItemForm confNewForm,
            DisConfigTypeEnum disConfigTypeEnum) {

        Config config = new Config();

        config.setAppId(confNewForm.getAppId());
        config.setEnvId(confNewForm.getEnvId());
        config.setName(confNewForm.getKey());
        config.setType(disConfigTypeEnum.getType());
        config.setVersion(confNewForm.getVersion());
        config.setValue(confNewForm.getValue());

        // 时间
        String curTime = DateUtils.format(new Date(),
                DataFormatConstants.COMMON_TIME_FORMAT);
        config.setCreateTime(curTime);
        config.setUpdateTime(curTime);

        configDao.create(config);
    }

    @Override
    public void delete(Long configId) {

        configDao.delete(configId);
    }

}
