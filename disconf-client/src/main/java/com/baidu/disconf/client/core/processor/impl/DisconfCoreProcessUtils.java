package com.baidu.disconf.client.core.processor.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.model.DisConfCommonModel;
import com.baidu.disconf.client.common.model.DisconfCenterBaseModel;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.baidu.disconf.client.config.DisClientConfig;
import com.baidu.disconf.client.store.DisconfStoreProcessor;
import com.baidu.disconf.core.common.constants.Constants;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.core.common.restful.core.RestUtil;

/**
 * @author liaoqiqi
 * @version 2014-8-4
 */
public class DisconfCoreProcessUtils {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DisconfCoreProcessUtils.class);

    /**
     * 调用此配置影响的回调函数
     */
    public static void callOneConf(DisconfStoreProcessor disconfStoreProcessor,
                                   String key) throws Exception {

        List<IDisconfUpdate> iDisconfUpdates = disconfStoreProcessor.getUpdateCallbackList(key);

        //
        // 获取回调函数列表
        //

        // CALL
        for (IDisconfUpdate iDisconfUpdate : iDisconfUpdates) {

            if (iDisconfUpdate != null) {

                LOGGER.info("start to call " + iDisconfUpdate.getClass());

                // set defined
                try {

                    iDisconfUpdate.reload();

                } catch (Exception e) {

                    LOGGER.error(e.toString(), e);
                }
            }
        }
    }
    
    /**
	 * 
	 * 校验disconf.properties配置是否正确
	 * @author 周宁
	 * @date 2016年12月8日
	 * @param appLength
	 * @param versionLength
	 * @param envLength
	 * @return boolean
	 * @throws
	 */
	public static boolean verifyDisconfConfig(int appLength,int versionLength,int envLength){
		if(appLength==versionLength&&appLength==envLength){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * 获取接口调用地址
	 * @author 周宁
	 * @date 2016年12月8日
	 * @param obj
	 * @param fileName
	 * @return String
	 * @throws
	 */
	public static List<String> requireRestUrl(DisConfCommonModel obj,String fileName){
		List<String> restUrls = new ArrayList<String>();
		String app = obj.getApp();
		String version = obj.getVersion();
		String env = obj.getEnv();
		StringBuffer rest = new StringBuffer();
		List<String> hostList = DisClientConfig.getInstance().getHostList();
		for(String str : hostList){
			rest.append("http://");
			rest.append(str);
			rest.append("/api/web/config/list/configs?");
			rest.append(Constants.APP+"="+app);
			rest.append("&"+Constants.ENV+"="+env);
			rest.append("&"+Constants.VERSION+"="+version);
			rest.append("&"+Constants.KEY+"="+fileName);
			restUrls.add(rest.toString());
			rest.setLength(0);
		}
		return restUrls;
	}
	
	/**
	 * 
	 * 获取文件下载地址
	 * @author 周宁
	 * @date 2016年12月8日
	 * @param obj
	 * @param fileName
	 * @param disConfigTypeEnum
	 * @return String
	 * @throws
	 */
	public static String requireRemoteUrl(DisConfCommonModel obj,String fileName,DisConfigTypeEnum disConfigTypeEnum){
		String app = obj.getApp();
		String version = obj.getVersion();
		String env = obj.getEnv();
		String type = String.valueOf(disConfigTypeEnum.getType());
		StringBuffer remote = new StringBuffer();
		
		if(type.equals("0")){
			remote.append("/api/config/file?");
		}else{
			remote.append("/api/config/item?");
		}
		remote.append("version="+version);
		remote.append("&"+Constants.APP+"="+app);
		remote.append("&"+Constants.ENV+"="+env);
		remote.append("&"+Constants.KEY+"="+fileName);
		remote.append("&"+Constants.TYPE+"=");
		remote.append(type);
		return remote.toString();
	}
	
	/**
	 * 将一个disConfCommonModel对象拆分为一个或者多个
	 * @author 周宁
	 * @date 2016年12月8日
	 * @param disConfCommonModel
	 * @return	List<DisConfCommonModel>
	 * @throws 
	 */
	public static List<DisConfCommonModel> splitDisConfCommonModel(
			DisConfCommonModel disConfCommonModel) {
		String[] apps = disConfCommonModel.getApp().split(",");
		String[] versions = disConfCommonModel.getVersion().split(",");
		String[] envs = disConfCommonModel.getEnv().split(",");
		List<DisConfCommonModel> disConfCommonModels = new ArrayList<DisConfCommonModel>();
		if(!DisconfCoreProcessUtils.verifyDisconfConfig(apps.length,versions.length,envs.length)){
			throw new RuntimeException("disconf.properties配置出错，请检查app、version、env配置是否正确");
		}
		for(int i = 0;i<apps.length;i++){
			DisConfCommonModel obj = new DisConfCommonModel();
			obj.setApp(apps[i]);
			obj.setEnv(envs[i]);
			obj.setVersion(versions[i]);
			disConfCommonModels.add(obj);
		}
		return disConfCommonModels;
	}
	
	/**
	 * @throws Exception 
	 * 获取url与模型的map
	 * @author 周宁
	 * @date 2016年12月8日
	 * @param fileName
	 * @param disconfCenterFile
	 * @return Map<String, DisConfCommonModel>
	 * @throws 
	 */
	public static <T> Map<String, DisConfCommonModel> getUrlModelMap(String fileName,
			DisconfCenterBaseModel disconfCenterFile,DisConfigTypeEnum disConfigTypeEnum) throws Exception {
		Map<String,DisConfCommonModel> map = new HashMap<String,DisConfCommonModel>();
		DisConfCommonModel disConfCommonModel = disconfCenterFile.getDisConfCommonModel();
		List<DisConfCommonModel> disConfCommonModels = splitDisConfCommonModel(disConfCommonModel);
		for(DisConfCommonModel obj : disConfCommonModels){
			//接口地址
			List<String> rests = DisconfCoreProcessUtils.requireRestUrl(obj,fileName);
			for(String rest :rests){
				String jsonStr = RestUtil.restGet(rest);
				if(jsonStr.contains(fileName)){
					map.put(DisconfCoreProcessUtils.requireRemoteUrl(obj, fileName,disConfigTypeEnum), obj);
				}
			}
			
		}
		return map;
	}
	
}
