package com.baidu.dsp.common.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

import com.baidu.dsp.common.constant.ErrorCode;
import com.baidu.dsp.common.constant.WebConstants;
import com.baidu.dsp.common.utils.ParamValidateUtils;
import com.baidu.dsp.common.vo.JsonObjectBase;
import com.baidu.dsp.common.vo.JsonObjectUtils;
import com.baidu.ub.common.db.DaoPageResult;

/**
 * @author liaoqiqi
 * @version 2013-11-26
 */
public class BaseController implements ApplicationContextAware {

    protected ApplicationContext context;

    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        this.context = arg0;
    }

    /**
     * OK：非列表数据
     *
     * @param key
     * @param value
     *
     * @return
     */
    protected <T> JsonObjectBase buildSuccess(String key, T value) {
        return JsonObjectUtils.buildObjectSuccess(key, value);
    }

    /**
     * OK：非列表数据
     *
     * @param value
     *
     * @return
     */
    protected <T> JsonObjectBase buildSuccess(T value) {
        return JsonObjectUtils.buildSimpleObjectSuccess(value);
    }

    /**
     * OK: 列表数据
     *
     * @param value
     * @param totalCount
     * @param <T>
     *
     * @return
     */
    protected <T> JsonObjectBase buildListSuccess(List<?> value, int totalCount) {

        return JsonObjectUtils.buildListSuccess(value, totalCount, null);
    }

    /**
     * OK: 列表数据
     *
     * @param data
     * @param <T>
     *
     * @return
     */
    protected <T> JsonObjectBase buildListSuccess(DaoPageResult<T> data) {

        return JsonObjectUtils.buildListSuccess(data.getResult(), data.getTotalCount(), data.getFootResult());
    }

    /**
     * 错误：参数错误
     *
     * @param field
     * @param message
     *
     * @return
     */
    protected JsonObjectBase buildParamError(String field, String message, ErrorCode errorCode) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(field, message);
        if (errorCode == null) {
            return JsonObjectUtils.buildFieldError(map, ErrorCode.FIELD_ERROR);
        }
        return JsonObjectUtils.buildFieldError(map, errorCode);
    }

    /**
     * 错误：参数错误
     *
     * @param bindingResult
     *
     * @return
     */
    protected JsonObjectBase buildFieldError(BindingResult bindingResult, ErrorCode errorCode) {

        Map<String, String> errors = new HashMap<String, String>();
        for (Object object : bindingResult.getAllErrors()) {
            if (object instanceof FieldError) {
                FieldError fieldError = (FieldError) object;
                String message = fieldError.getDefaultMessage();
                errors.put(fieldError.getField(), message);
            }
        }

        if (errorCode == null) {
            return JsonObjectUtils.buildFieldError(errors, ErrorCode.FIELD_ERROR);
        }
        return JsonObjectUtils.buildFieldError(errors, errorCode);
    }

    /**
     * 错误：全局的
     *
     * @param message
     *
     * @return
     */
    protected JsonObjectBase buildGlobalError(String message, ErrorCode errorCode) {

        return JsonObjectUtils.buildGlobalError(message, errorCode);
    }

    /**
     * 绑定时间
     *
     * @param binder
     */
    @InitBinder
    protected void dateBinder(WebDataBinder binder) {
        // The date format to parse or output your dates
        SimpleDateFormat dateFormat = new SimpleDateFormat(WebConstants.TIME_FORMAT);
        // Create a new CustomDateEditor
        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
        // Register it as custom editor for the Date type
        binder.registerCustomEditor(Date.class, editor);
    }

    /*
     * Bind出错，这里是最高优先级的处理
     */
    @ExceptionHandler({BindException.class})
    public ModelAndView handleBindException(final BindException e) {

        return ParamValidateUtils.getParamErrors(e);
    }

}
