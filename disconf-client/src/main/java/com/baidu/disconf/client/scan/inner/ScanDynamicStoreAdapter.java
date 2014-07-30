package com.baidu.disconf.client.scan.inner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.inter.IDisconfUpdate;
import com.baidu.disconf.client.scan.inner.model.ScanDynamicModel;
import com.baidu.disconf.client.scan.inner.model.ScanStaticModel;
import com.baidu.disconf.client.store.DisconfStoreMgr;
import com.baidu.disconf.client.utils.SpringContextUtil;

/**
 * 动态扫描 与 Store模块的转换器
 * 
 * @author liaoqiqi
 * @version 2014-6-18
 */
public class ScanDynamicStoreAdapter {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ScanDynamicStoreAdapter.class);

    /**
     * 扫描更新回调函数
     */
    public static void scanUpdateCallbacks(ScanStaticModel scanModel) {

        // 扫描出来
        ScanDynamicModel scanDynamicModel = analysis4DisconfUpdate(scanModel);

        // 写到仓库中
        transformUpdateService(scanDynamicModel
                .getDisconfUpdateServiceInverseIndexMap());
    }

    /**
     * 
     * 第二次扫描, 获取更新 回调的实例<br/>
     * 
     * 分析出更新操作的相关配置文件内容
     */
    private static ScanDynamicModel analysis4DisconfUpdate(
            ScanStaticModel scanModel) {

        Map<String, List<IDisconfUpdate>> inverseMap = new HashMap<String, List<IDisconfUpdate>>();
        Set<Class<?>> disconfUpdateServiceSet = scanModel
                .getDisconfUpdateService();
        for (Class<?> disconfUpdateServiceClass : disconfUpdateServiceSet) {

            DisconfUpdateService disconfUpdateService = disconfUpdateServiceClass
                    .getAnnotation(DisconfUpdateService.class);
            List<String> keysList = Arrays.asList(disconfUpdateService.keys());

            //
            // 校验是否有继承正确,是否继承IDisconfUpdate
            //
            if (!ScanVerify.hasIDisconfUpdate(disconfUpdateServiceClass)) {
                continue;
            }

            //
            // 回调函数需要实例化出来,这里
            // 非Spring直接New
            // Spring要GetBean
            //

            IDisconfUpdate iDisconfUpdate = null;

            //
            // Spring方式
            try {

                iDisconfUpdate = getSpringBean(disconfUpdateServiceClass);

            } catch (Exception e) {
                LOGGER.warn(e.toString());
            }

            //
            // 非Spring方式
            if (iDisconfUpdate == null) {
                try {

                    iDisconfUpdate = (IDisconfUpdate) disconfUpdateServiceClass
                            .newInstance();

                } catch (Exception e) {

                    LOGGER.error("Your class "
                            + disconfUpdateServiceClass.toString()
                            + " cannot new instance. " + e.toString());
                    continue;
                }
            }

            // 反索引
            for (String key : keysList) {
                List<IDisconfUpdate> serviceList = null;
                if (inverseMap.containsKey(key)) {
                    inverseMap.get(key).add(iDisconfUpdate);
                } else {
                    serviceList = new ArrayList<IDisconfUpdate>();
                    serviceList.add(iDisconfUpdate);
                    inverseMap.put(key, serviceList);
                }
            }
        }

        ScanDynamicModel scanDynamicModel = new ScanDynamicModel();
        scanDynamicModel.setDisconfUpdateServiceInverseIndexMap(inverseMap);

        return scanDynamicModel;
    }

    /**
     * 第二次扫描<br/>
     * 转换 更新 回调函数，将其写到 仓库中
     * 
     * @return
     */
    private static void transformUpdateService(
            Map<String, List<IDisconfUpdate>> disconfUpdateServiceInverseIndexMap) {

        for (String key : disconfUpdateServiceInverseIndexMap.keySet()) {

            // 找不到回调对应的配置，这是用户配置 错误了
            if (!DisconfStoreMgr.getInstance().hasThisConf(key)) {

                StringBuffer sb = new StringBuffer();
                sb.append("cannot find " + key + "for: ");
                for (IDisconfUpdate serClass : disconfUpdateServiceInverseIndexMap
                        .get(key)) {
                    sb.append(serClass.toString() + "\t");
                }
                LOGGER.error(sb.toString());

            } else {

                // 配置正常
                DisconfStoreMgr.getInstance().addUpdateCallbackList(key,
                        disconfUpdateServiceInverseIndexMap.get(key));
            }
        }
    }

    /**
     * 获取Spring Bean
     * 
     * @return
     */
    private static IDisconfUpdate getSpringBean(Class<?> cls) throws Exception {

        if (SpringContextUtil.getApplicationContext() == null) {
            LOGGER.error("Spring Context is null. Cannot autowire "
                    + cls.getCanonicalName());
            return null;
        }

        // spring 方式
        return (IDisconfUpdate) SpringContextUtil.getBean(cls);
    }
}
