package com.baidu.disconf.web.service.config.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.web.config.ApplicationPropertyConfig;
import com.baidu.disconf.web.innerapi.zookeeper.ZooKeeperDriver;
import com.baidu.disconf.web.service.app.bo.App;
import com.baidu.disconf.web.service.app.service.AppMgr;
import com.baidu.disconf.web.service.config.bo.Config;
import com.baidu.disconf.web.service.config.dao.ConfigDao;
import com.baidu.disconf.web.service.config.form.ConfListForm;
import com.baidu.disconf.web.service.config.form.ConfNewItemForm;
import com.baidu.disconf.web.service.config.service.ConfigMgr;
import com.baidu.disconf.web.service.config.vo.ConfListVo;
import com.baidu.disconf.web.service.config.vo.MachineListVo;
import com.baidu.disconf.web.service.env.bo.Env;
import com.baidu.disconf.web.service.env.service.EnvMgr;
import com.baidu.disconf.web.service.zookeeper.config.ZooConfig;
import com.baidu.disconf.web.service.zookeeper.dto.ZkDisconfData;
import com.baidu.disconf.web.service.zookeeper.dto.ZkDisconfData.ZkDisconfDataItem;
import com.baidu.disconf.web.service.zookeeper.service.ZkDeployMgr;
import com.baidu.disconf.web.utils.CodeUtils;
import com.baidu.disconf.web.utils.DiffUtils;
import com.baidu.disconf.web.utils.MyStringUtils;
import com.baidu.dsp.common.constant.DataFormatConstants;
import com.baidu.dsp.common.utils.DataTransfer;
import com.baidu.dsp.common.utils.ServiceUtil;
import com.baidu.dsp.common.utils.email.LogMailBean;
import com.baidu.ub.common.db.DaoPageResult;
import com.github.knightliao.apollo.utils.data.GsonUtils;
import com.github.knightliao.apollo.utils.io.OsUtil;
import com.github.knightliao.apollo.utils.time.DateUtils;

/**
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

	@Autowired
	private LogMailBean logMailBean;

	@Autowired
	private ApplicationPropertyConfig applicationPropertyConfig;

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
	 *
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
	 * 配置列表
	 */
	@Override
	public DaoPageResult<ConfListVo> getConfigList(ConfListForm confListForm,
			boolean fetchZk, final boolean getErrorMessage) {

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
		final boolean myFetchZk = fetchZk;
		Map<String, ZkDisconfData> zkDataMap = new HashMap<String, ZkDisconfData>();
		if (myFetchZk) {
			zkDataMap = zkDeployMgr.getZkDisconfDataMap(app.getName(),
					env.getName(), confListForm.getVersion());
		}
		final Map<String, ZkDisconfData> myzkDataMap = zkDataMap;

		//
		// 进行转换
		//
		DaoPageResult<ConfListVo> configListVo = ServiceUtil.getResult(
				configList, new DataTransfer<Config, ConfListVo>() {

					@Override
					public ConfListVo transfer(Config input) {

						String appNameString = app.getName();
						String envName = env.getName();

						ZkDisconfData zkDisconfData = null;
						if (myzkDataMap != null
								&& myzkDataMap.keySet().contains(
										input.getName())) {
							zkDisconfData = myzkDataMap.get(input.getName());
						}
						ConfListVo configListVo = convert(input, appNameString,
								envName, zkDisconfData);

						// 列表操作不要显示值, 为了前端显示快速(只是内存里操作)
						if (!myFetchZk || !getErrorMessage) {

							// 列表 value 设置为 ""
							configListVo.setValue("");
							configListVo
									.setMachineList(new ArrayList<ZkDisconfData.ZkDisconfDataItem>());
						}

						return configListVo;
					}
				});

		return configListVo;
	}

	/**
     *
     */
	private MachineListVo getZkData(List<ZkDisconfDataItem> datalist,
			Config config) {

		int errorNum = 0;
		for (ZkDisconfDataItem zkDisconfDataItem : datalist) {

			if (config.getType().equals(DisConfigTypeEnum.FILE.getType())) {

				List<String> errorKeyList = compareConfig(
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

		MachineListVo machineListVo = new MachineListVo();
		machineListVo.setDatalist(datalist);
		machineListVo.setErrorNum(errorNum);
		machineListVo.setMachineSize(datalist.size());

		return machineListVo;
	}

	/**
	 * 转换成配置返回
	 *
	 * @param config
	 *
	 * @return
	 */
	private ConfListVo convert(Config config, String appNameString,
			String envName, ZkDisconfData zkDisconfData) {

		ConfListVo confListVo = new ConfListVo();

		confListVo.setConfigId(config.getId());
		confListVo.setAppId(config.getAppId());
		confListVo.setAppName(appNameString);
		confListVo.setEnvName(envName);
		confListVo.setEnvId(config.getEnvId());
		confListVo.setCreateTime(config.getCreateTime());
		confListVo.setModifyTime(config.getUpdateTime().substring(0, 12));
		confListVo.setKey(config.getName());
		// StringEscapeUtils.escapeHtml escape
		confListVo.setValue(CodeUtils.unicodeToUtf8(config.getValue()));
		confListVo.setVersion(config.getVersion());
		confListVo.setType(DisConfigTypeEnum.getByType(config.getType())
				.getModelName());
		confListVo.setTypeId(config.getType());

		//
		//
		//
		if (zkDisconfData != null) {

			confListVo.setMachineSize(zkDisconfData.getData().size());

			List<ZkDisconfDataItem> datalist = zkDisconfData.getData();

			MachineListVo machineListVo = getZkData(datalist, config);

			confListVo.setErrorNum(machineListVo.getErrorNum());
			confListVo.setMachineList(machineListVo.getDatalist());
			confListVo.setMachineSize(machineListVo.getMachineSize());
		}

		// get machine list size for config which name contins "/"
		if (config.getName().contains("/")) {
			MachineListVo v = getConfVoWithZk(config.getId());
			if (v != null) {
				confListVo.setErrorNum(v.getErrorNum());
				confListVo.setMachineList(v.getDatalist());
				confListVo.setMachineSize(v.getMachineSize());
			}
		}

		return confListVo;
	}

	/**
     *
     */
	private List<String> compareConfig(String zkData, String dbData) {

		List<String> errorKeyList = new ArrayList<String>();

		Properties prop = new Properties();
		try {
			prop.load(IOUtils.toInputStream(dbData));
		} catch (Exception e) {
			LOG.error(e.toString());
			errorKeyList.add(zkData);
			return errorKeyList;
		}

		Map<String, String> zkMap = GsonUtils.parse2Map(zkData);
		for (String keyInZk : zkMap.keySet()) {

			Object valueInDb = prop.get(keyInZk);
			String zkDataStr = zkMap.get(keyInZk);

			// convert zk data to utf-8
			// zkMap.put(keyInZk, CodeUtils.unicodeToUtf8(zkDataStr));

			try {

				if ((zkDataStr == null && valueInDb != null)
						|| (zkDataStr != null && valueInDb == null)) {
					errorKeyList.add(keyInZk);

				} else {

					boolean isEqual = true;

					if (MyStringUtils.isDouble(zkDataStr)
							&& MyStringUtils.isDouble(valueInDb.toString())) {

						if (Math.abs(Double.parseDouble(zkDataStr)
								- Double.parseDouble(valueInDb.toString())) > 0.001d) {
							isEqual = false;
						}

					} else {
						if (!zkDataStr.equals(valueInDb.toString().trim())) {
							isEqual = false;
						}
					}

					if (!isEqual) {
						errorKeyList.add(keyInZk
								+ "\t"
								+ DiffUtils.getDiffSimple(zkDataStr, valueInDb
										.toString().trim()));
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
	 * 根据 配置ID获取ZK对比数据
	 */
	@Override
	public MachineListVo getConfVoWithZk(Long configId) {

		Config config = configDao.get(configId);

		App app = appMgr.getById(config.getAppId());
		Env env = envMgr.getById(config.getEnvId());

		//
		//
		//

		DisConfigTypeEnum disConfigTypeEnum = DisConfigTypeEnum.FILE;
		if (config.getType().equals(DisConfigTypeEnum.ITEM.getType())) {
			disConfigTypeEnum = DisConfigTypeEnum.ITEM;
		}

		ZkDisconfData zkDisconfData = zkDeployMgr.getZkDisconfData(
				app.getName(), env.getName(), config.getVersion(),
				disConfigTypeEnum, config.getName());

		if (zkDisconfData == null) {
			return new MachineListVo();
		}

		MachineListVo machineListVo = getZkData(zkDisconfData.getData(), config);
		return machineListVo;
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
	public String updateItemValue(Long configId, String value) {

		Config config = getConfigById(configId); 
		String oldValue = config.getValue();

		//
		// 配置数据库的值 encode to db
		//
		configDao.updateValue(configId, CodeUtils.utf8ToUnicode(value));

		// if common app then update all app's config
		updateAllConfigByCommonApp(config, value);
		//
		// 发送邮件通知
		//
		String toEmails = appMgr.getEmails(config.getAppId());

		if (applicationPropertyConfig.isEmailMonitorOn() == true) {
			boolean isSendSuccess = logMailBean.sendHtmlEmail(toEmails,
					" config update", DiffUtils.getDiff(
							CodeUtils.unicodeToUtf8(oldValue), value,
							config.toString(), getConfigUrlHtml(config)));
			if (isSendSuccess) {
				return "修改成功，邮件通知成功";
			} else {
				return "修改成功，邮件发送失败，请检查邮箱配置";
			}
		}

		return "修改成功";
	}

	/**
	 * @return
	 */
	private String getConfigUrlHtml(Config config) {

		// return "<br/>点击<a href='http://"
		// + applicationPropertyConfig.getDomain()
		// + "/modifyFile.html?configId=" + config.getId()
		// + "'> 这里 </a> 进入查看<br/>";
		return "";
	}

	/**
	 * @param newValue
	 * @param identify
	 *
	 * @return
	 */
	private String getNewValue(String newValue, String identify,
			String htmlClick) {

		String contentString = StringEscapeUtils.escapeHtml(identify) + "<br/>"
				+ htmlClick + "<br/><br/> ";

		String data = "<br/><br/><br/><span style='color:#FF0000'>New value:</span><br/>";
		contentString = contentString + data
				+ StringEscapeUtils.escapeHtml(newValue);

		return contentString;
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
		config.setValue(CodeUtils.utf8ToUnicode(confNewForm.getValue()));

		// 时间
		String curTime = DateUtils.format(new Date(),
				DataFormatConstants.COMMON_TIME_FORMAT);
		config.setCreateTime(curTime);
		config.setUpdateTime(curTime);

		configDao.create(config);

		// create zk node
		App app = appMgr.getById(confNewForm.getAppId());
		Env env = envMgr.getById(confNewForm.getEnvId());
		if (app != null && env != null) {
			zooKeeperDriver.createConfigKeyNode(app.getName(), env.getName(),
					confNewForm.getVersion(), confNewForm.getKey(),
					disConfigTypeEnum);
		}
		// 发送邮件通知
		//
		String toEmails = appMgr.getEmails(config.getAppId());

		if (applicationPropertyConfig.isEmailMonitorOn() == true) {
			logMailBean.sendHtmlEmail(
					toEmails,
					" config new",
					getNewValue(confNewForm.getValue(), config.toString(),
							getConfigUrlHtml(config)));
		}

	}

	@Override
	public void delete(Long configId) {

		configDao.delete(configId);
	}

	/**
	 * 根据当前更新的config是否为公用配置，判断是否级联更新其他app中引用的公用配置
	 * 
	 * @param config
	 * @return
	 */
	private boolean updateAllConfigByCommonApp(Config config, String value) {
		boolean r = false;
		if (config != null && config.getValue() != null) {
			App app = appMgr.getById(config.getAppId());
			// is common app
			if (app != null
					&& app.getName().equals(
							applicationPropertyConfig.getCommon_base_app())) {
				r = true;
				Properties tarP = String2Properties(value);
				if (tarP != null) {
					List<Config> list = configDao.findAll();
					if (list != null && list.size() > 0) {
						for (Config c : list) {
							// config is prpperties and ignore common app's
							// config
							if (c != null && c.getAppId() != app.getId()
									&& c.getType() == 0) {
								String src = c.getValue();
								if (src != null && !src.isEmpty()) {
									Properties local = String2Properties(src);
									if (isNeedToUpdate(tarP, local)) {
										String newStr = properties2String(local);
										if (newStr != null && !newStr.isEmpty()) {
											updateItemValue(c.getId(), newStr);
											notifyZookeeper(c.getId());
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return r;
	}

	/**
	 * 判断应用配置中是否包含公用配置，包含则更新，并返回
	 * 
	 * @param common
	 * @param local
	 * @return
	 */
	public boolean isNeedToUpdate(Properties common, Properties local) {
		boolean r = false;
		if (common != null && local != null) {
			Enumeration<?> enu = common.propertyNames();
			while (enu.hasMoreElements()) {
				String key = (String) enu.nextElement();
				if (local.containsKey(key)) {
					r = true;
					local.setProperty(key, common.getProperty(key));
				}
			}
		}
		return r;
	}

	public static Properties String2Properties(String str) {
		Properties props = null;
		if (str != null && !str.isEmpty()) {
			props = new Properties();
			InputStream in = null;
			try {
				in = new ByteArrayInputStream(str.getBytes("utf-8"));
				props.load(in);
				in.close();
			} catch (UnsupportedEncodingException e) {
				LOG.error(e.getMessage());
			} catch (IOException e) {
				LOG.error(e.getMessage());
			} finally {
				in = null;
			}
		}
		return props;
	}

	public static String properties2String(Properties prop) {
		String str = null;
		if (prop != null) {
			StringBuffer sf = new StringBuffer("#auto update\r\n");
			Enumeration<?> enu = prop.propertyNames();
			while (enu.hasMoreElements()) {
				String key = (String) enu.nextElement();
				sf.append(key + "=" + prop.getProperty(key) + "\r\n");
			}
			str = sf.toString();
		}

		return str;
	}
}
