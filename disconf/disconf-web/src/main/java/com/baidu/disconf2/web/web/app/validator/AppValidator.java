package com.baidu.disconf2.web.web.app.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.disconf2.web.service.app.bo.App;
import com.baidu.disconf2.web.service.app.form.AppNewForm;
import com.baidu.disconf2.web.service.app.service.AppMgr;
import com.baidu.dsp.common.exception.FieldException;

/**
 * 权限验证
 * 
 * @author liaoqiqi
 * @version 2014-7-2
 */
@Component
public class AppValidator {

    @Autowired
    private AppMgr appMgr;

    /**
     * 验证登录
     */
    public void validateCreate(AppNewForm appNewForm) {

        App app = appMgr.getByName(appNewForm.getApp());
        if (app != null) {
            throw new FieldException(AppNewForm.APP, "app.exist", null);
        }
    }
}
