package com.baidu.disconf.web.web.config.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.baidu.disconf.core.common.utils.FileUtils;
import com.baidu.disconf.web.utils.ZipUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.web.service.config.form.ConfNewForm;
import com.baidu.disconf.web.service.config.form.ConfNewItemForm;
import com.baidu.disconf.web.service.config.service.ConfigMgr;
import com.baidu.disconf.web.web.config.validator.ConfigValidator;
import com.baidu.disconf.web.web.config.validator.FileUploadValidator;
import com.baidu.dsp.common.constant.WebConstants;
import com.baidu.dsp.common.controller.BaseController;
import com.baidu.dsp.common.exception.FileUploadException;
import com.baidu.dsp.common.vo.JsonObjectBase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 专用于配置新建
 *
 * @author liaoqiqi
 * @version 2014-6-24
 */
@Controller
@RequestMapping(WebConstants.API_PREFIX + "/web/config")
public class ConfigNewController extends BaseController {

    protected static final Logger LOG = LoggerFactory.getLogger(ConfigUpdateController.class);
    private static final String TMP_FILE_PATH = "/tmp";

    @Autowired
    private ConfigMgr configMgr;

    @Autowired
    private ConfigValidator configValidator;

    @Autowired
    private FileUploadValidator fileUploadValidator;

    /**
     * 配置项的新建
     *
     * @param confNewForm
     *
     * @return
     */
    @RequestMapping(value = "/item", method = RequestMethod.POST)
    @ResponseBody
    public JsonObjectBase newItem(@Valid ConfNewItemForm confNewForm) {

        // 业务校验
        configValidator.validateNew(confNewForm, DisConfigTypeEnum.ITEM);

        //
        configMgr.newConfig(confNewForm, DisConfigTypeEnum.ITEM);

        return buildSuccess("创建成功");
    }

    /**
     * 配置文件的新建(使用上传配置文件)
     *
     * @param confNewForm
     * @param file
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public JsonObjectBase updateFile(@Valid ConfNewForm confNewForm, @RequestParam("myfilerar") MultipartFile file) throws IOException {

        LOG.info(confNewForm.toString());

        //
        // 校验
        //
        int fileSize = 1024 * 1024 * 4;
        String[] allowExtName = {".properties", ".xml",".zip"};
        fileUploadValidator.validateFile(file, fileSize, allowExtName);

        // 处理(新增压缩包)
        String retMessage = handleAndSaveFileContent(confNewForm,file);

        return buildSuccess(retMessage);
    }

    /**
     * 配置文件的新建(使用文本)
     *
     * @param confNewForm
     * @param fileContent
     * @param fileName
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/filetext", method = RequestMethod.POST)
    public JsonObjectBase updateFileWithText(@Valid ConfNewForm confNewForm, @NotNull String fileContent,
                                             @NotNull String fileName) {

        LOG.info(confNewForm.toString());

        // 创建配置文件表格
        ConfNewItemForm confNewItemForm = new ConfNewItemForm(confNewForm);
        confNewItemForm.setKey(fileName);
        confNewItemForm.setValue(fileContent);

        // 业务校验
        configValidator.validateNew(confNewItemForm, DisConfigTypeEnum.FILE);

        //
        configMgr.newConfig(confNewItemForm, DisConfigTypeEnum.FILE);

        return buildSuccess("创建成功");
    }


    /**
     * 配置文件，统一处理方法
     */
    private String handleAndSaveFileContent(ConfNewForm confNewForm, MultipartFile file) throws IOException {
        //step 1: 获取文件后缀信息
        String filename = file.getOriginalFilename();
        String extName = filename.substring(filename.lastIndexOf(".")).toLowerCase();

        //step 2: 定义待处理的文件列表数据
        List<File> configFiles = new ArrayList<File>();

        //step 3: 处理文件列表
        File tmpConfigFile = new File(TMP_FILE_PATH,file.getOriginalFilename());
        FileUtils.copyInputStreamToFile(file.getInputStream(), tmpConfigFile);
        if(StringUtils.equals(extName,"properties")){
            configFiles.add(tmpConfigFile);
        }else if(StringUtils.equals(extName,"zip")){
            configFiles = ZipUtil.upzipFile(tmpConfigFile,TMP_FILE_PATH);
        }else{
            //Nothing
        }

        //step 4: 保存文件信息
        saveFileContent(configFiles,confNewForm);

        return "处理成功";

    }

    private void saveFileContent(List<File> configFiles,ConfNewForm confNewForm){

        for(File file : configFiles){
            String fileContent = "";
            try {
                fileContent = new String(FileUtils.readFileToString(file,"UTF-8"));
                LOG.info("receive file: " + fileContent);

            } catch (Exception e) {
                LOG.error(e.toString());
                throw new FileUploadException("upload file error", e);
            }


            // 创建配置文件表格
            ConfNewItemForm confNewItemForm = new ConfNewItemForm(confNewForm);
            confNewItemForm.setKey(file.getName());
            confNewItemForm.setValue(fileContent);

            // 业务校验
            configValidator.validateNew(confNewItemForm, DisConfigTypeEnum.FILE);

            // 目前都是没有事务支持的,@jack_lcz
            configMgr.newConfig(confNewItemForm, DisConfigTypeEnum.FILE);
        }
    }

}
