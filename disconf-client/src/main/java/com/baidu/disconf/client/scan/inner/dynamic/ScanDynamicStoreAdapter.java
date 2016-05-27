package com.baidu.disconf.client.scan.inner.dynamic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.model.DisconfKey;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.baidu.disconf.client.common.update.IDisconfUpdatePipeline;
import com.baidu.disconf.client.config.DisClientConfig;
import com.baidu.disconf.client.scan.inner.common.ScanVerify;
import com.baidu.disconf.client.scan.inner.dynamic.model.ScanDynamicModel;
import com.baidu.disconf.client.scan.inner.statically.model.ScanStaticModel;
import com.baidu.disconf.client.store.DisconfStorePipelineProcessor;
import com.baidu.disconf.client.store.DisconfStoreProcessor;
import com.baidu.disconf.client.store.DisconfStoreProcessorFactory;
import com.baidu.disconf.client.support.registry.Registry;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;

/**
 * 动态扫描 与 Store模块的转换器
 *
 * @author liaoqiqi
 * @version 2014-6-18
 */
public class ScanDynamicStoreAdapter {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ScanDynamicStoreAdapter.class);

    /**
     * 扫描更新回调函数
     */
    public static void scanUpdateCallbacks(ScanStaticModel scanModel, Registry registry) {

        // 扫描出来
        ScanDynamicModel scanDynamicModel = analysis4DisconfUpdate(scanModel, registry);

        // 写到仓库中
        transformUpdateService(scanDynamicModel.getDisconfUpdateServiceInverseIndexMap());
        transformPipelineService(scanDynamicModel.getDisconfUpdatePipeline());
    }

    /**
     * 第二次扫描, 获取更新 回调的实例<br/>
     * <p/>
     * 分析出更新操作的相关配置文件内容
     */
    private static ScanDynamicModel analysis4DisconfUpdate(ScanStaticModel scanModel, Registry registry) {

        // 配置项或文件
        Map<DisconfKey, List<IDisconfUpdate>> inverseMap = new HashMap<DisconfKey, List<IDisconfUpdate>>();

        Set<Class<?>> disconfUpdateServiceSet = scanModel.getDisconfUpdateService();
        for (Class<?> disconfUpdateServiceClass : disconfUpdateServiceSet) {

            // 回调对应的参数
            DisconfUpdateService disconfUpdateService =
                    disconfUpdateServiceClass.getAnnotation(DisconfUpdateService.class);

            //
            // 校验是否有继承正确,是否继承IDisconfUpdate
            if (!ScanVerify.hasIDisconfUpdate(disconfUpdateServiceClass)) {
                continue;
            }

            //
            // 获取回调接口实例
            IDisconfUpdate iDisconfUpdate = getIDisconfUpdateInstance(disconfUpdateServiceClass, registry);
            if (iDisconfUpdate == null) {
                continue;
            }

            //
            // 配置项
            processItems(inverseMap, disconfUpdateService, iDisconfUpdate);

            //
            // 配置文件
            processFiles(inverseMap, disconfUpdateService, iDisconfUpdate);

        }

        // set data
        ScanDynamicModel scanDynamicModel = new ScanDynamicModel();
        scanDynamicModel.setDisconfUpdateServiceInverseIndexMap(inverseMap);

        //
        // set update pipeline
        //
        if (scanModel.getiDisconfUpdatePipeline() != null) {
            IDisconfUpdatePipeline iDisconfUpdatePipeline = getIDisconfUpdatePipelineInstance(scanModel
                    .getiDisconfUpdatePipeline(), registry);
            if (iDisconfUpdatePipeline != null) {
                scanDynamicModel.setDisconfUpdatePipeline(iDisconfUpdatePipeline);
            }
        }

        return scanDynamicModel;
    }

    /**
     * 获取回调对应配置项列表
     */
    private static void processItems(Map<DisconfKey, List<IDisconfUpdate>> inverseMap,
                                     DisconfUpdateService disconfUpdateService, IDisconfUpdate iDisconfUpdate) {

        List<String> itemKeys = Arrays.asList(disconfUpdateService.itemKeys());

        // 反索引
        for (String key : itemKeys) {

            DisconfKey disconfKey = new DisconfKey(DisConfigTypeEnum.ITEM, key);
            addOne2InverseMap(disconfKey, inverseMap, iDisconfUpdate);
        }
    }

    /**
     * 获取回调对应的配置文件列表
     */
    private static void processFiles(Map<DisconfKey, List<IDisconfUpdate>> inverseMap,
                                     DisconfUpdateService disconfUpdateService, IDisconfUpdate iDisconfUpdate) {

        // 反索引
        List<Class<?>> classes = Arrays.asList(disconfUpdateService.classes());
        for (Class<?> curClass : classes) {

            // 获取其注解
            DisconfFile disconfFile = curClass.getAnnotation(DisconfFile.class);
            if (disconfFile == null) {

                LOGGER.error("cannot find DisconfFile annotation for class when set callback: {} ",
                        curClass.toString());
                continue;
            }

            DisconfKey disconfKey = new DisconfKey(DisConfigTypeEnum.FILE, disconfFile.filename());
            addOne2InverseMap(disconfKey, inverseMap, iDisconfUpdate);
        }

        // 反索引
        List<String> fileKeyList = Arrays.asList(disconfUpdateService.confFileKeys());
        for (String fileKey : fileKeyList) {

            DisconfKey disconfKey = new DisconfKey(DisConfigTypeEnum.FILE, fileKey);
            addOne2InverseMap(disconfKey, inverseMap, iDisconfUpdate);
        }

    }

    /**
     * 获取回调接口的实现
     * <p/>
     * // 回调函数需要实例化出来,这里
     * // 非Spring直接New
     * // Spring要GetBean
     * //
     */
    private static IDisconfUpdate getIDisconfUpdateInstance(Class<?> disconfUpdateServiceClass, Registry registry) {

        Object iDisconfUpdate = registry.getFirstByType(disconfUpdateServiceClass, true);
        if (iDisconfUpdate == null) {
            return null;
        }
        return (IDisconfUpdate) iDisconfUpdate;

    }

    /**
     * 获取回调接口的实现
     * //
     */
    private static IDisconfUpdatePipeline getIDisconfUpdatePipelineInstance(
            Class<IDisconfUpdatePipeline> disconfUpdateServiceClass,
            Registry registry) {

        Object iDisconfUpdate = registry.getFirstByType(disconfUpdateServiceClass, true);
        if (iDisconfUpdate == null) {
            return null;
        }
        return (IDisconfUpdatePipeline) iDisconfUpdate;

    }

    /**
     * 将一个配置回调item写到map里
     */
    private static void addOne2InverseMap(DisconfKey disconfKey, Map<DisconfKey, List<IDisconfUpdate>> inverseMap,
                                          IDisconfUpdate iDisconfUpdate) {

        // 忽略的key 应该忽略掉
        if (DisClientConfig.getInstance().getIgnoreDisconfKeySet().contains(disconfKey.getKey())) {
            return;
        }

        List<IDisconfUpdate> serviceList;

        if (inverseMap.containsKey(disconfKey)) {
            inverseMap.get(disconfKey).add(iDisconfUpdate);
        } else {
            serviceList = new ArrayList<IDisconfUpdate>();
            serviceList.add(iDisconfUpdate);
            inverseMap.put(disconfKey, serviceList);
        }
    }

    /**
     * 第二次扫描<br/>
     * 转换 pipeline 回调函数，将其写到 仓库中
     */
    private static void transformPipelineService(IDisconfUpdatePipeline iDisconfUpdatePipeline) {

        DisconfStorePipelineProcessor disconfStorePipelineProcessor = DisconfStoreProcessorFactory
                .getDisconfStorePipelineProcessor();
        disconfStorePipelineProcessor.setDisconfUpdatePipeline(iDisconfUpdatePipeline);
    }

    /**
     * 第二次扫描<br/>
     * 转换 更新 回调函数，将其写到 仓库中
     */
    private static void transformUpdateService(Map<DisconfKey,
            List<IDisconfUpdate>> disconfUpdateServiceInverseIndexMap) {

        DisconfStoreProcessor disconfStoreProcessorFile = DisconfStoreProcessorFactory.getDisconfStoreFileProcessor();
        DisconfStoreProcessor disconfStoreProcessorItem = DisconfStoreProcessorFactory.getDisconfStoreItemProcessor();

        for (DisconfKey disconfKey : disconfUpdateServiceInverseIndexMap.keySet()) {

            //
            //
            //

            try {
                if (disconfKey.getDisConfigTypeEnum().equals(DisConfigTypeEnum.FILE)) {

                    if (!disconfStoreProcessorFile.hasThisConf(disconfKey.getKey())) {
                        throw new Exception();
                    }

                    disconfStoreProcessorFile.addUpdateCallbackList(disconfKey.getKey(),
                            disconfUpdateServiceInverseIndexMap
                                    .get(disconfKey));

                } else if (disconfKey.getDisConfigTypeEnum().equals(DisConfigTypeEnum.ITEM)) {

                    if (!disconfStoreProcessorItem.hasThisConf(disconfKey.getKey())) {
                        throw new Exception();
                    }

                    disconfStoreProcessorItem.addUpdateCallbackList(disconfKey.getKey(),
                            disconfUpdateServiceInverseIndexMap
                                    .get(disconfKey));
                }

            } catch (Exception e) {
                // 找不到回调对应的配置，这是用户配置 错误了
                StringBuffer sb = new StringBuffer();
                sb.append("cannot find " + disconfKey + "for: ");
                for (IDisconfUpdate serClass : disconfUpdateServiceInverseIndexMap.get(disconfKey)) {
                    sb.append(serClass.toString() + "\t");
                }
                LOGGER.error(sb.toString());
            }
        }
    }

}
