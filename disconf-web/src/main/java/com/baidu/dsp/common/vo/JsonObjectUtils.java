package com.baidu.dsp.common.vo;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.baidu.dsp.common.constant.ErrorCode;
import com.baidu.dsp.common.constant.FrontEndInterfaceConstant;
import com.baidu.dsp.common.context.ContextReader;

/**
 * 通用的JSON返回器
 *
 * @author liaoqiqi
 * @version 2013-12-3
 */
@Component
public class JsonObjectUtils {

    protected static final Logger LOG = LoggerFactory.getLogger(JsonObjectUtils.class);

    private static ContextReader contextReader;

    @Autowired(required = true)
    public JsonObjectUtils(@Qualifier("contextReaderImpl") ContextReader contextReader) {
        JsonObjectUtils.contextReader = contextReader;
    }

    /**
     * 返回正确(多层结构), 非列表请求
     *
     * @param key
     * @param value
     *
     * @return
     */
    public static <T> JsonObjectBase buildObjectSuccess(String key, T value) {

        JsonObject json = new JsonObject();
        json.addData(key, value);

        LOG.info(json.toString());

        return json;
    }

    /**
     * 返回正确(顶层结构), 非列表请求
     *
     * @param value
     *
     * @return
     */
    public static <T> JsonObjectBase buildSimpleObjectSuccess(T value) {

        JsonSimpleObject json = new JsonSimpleObject();
        json.setResult(value);

        LOG.info(json.toString());

        return json;
    }

    /**
     * 返回正确, 列表请求
     *
     * @param value
     *
     * @return
     */
    public static <T> JsonObjectBase buildListSuccess(List<?> value, int totalCount, T footResult) {

        JsonObjectList json = new JsonObjectList();

        json.setPage(totalCount);
        json.addData(value);
        json.addFootData(footResult);

        LOG.info(json.toString());

        return json;
    }

    /**
     * 参数错误: Field
     *
     * @param errors
     *
     * @return
     */
    public static JsonObjectBase buildFieldError(Map<String, String> errors, ErrorCode statusCode) {

        JsonObjectError json = new JsonObjectError();
        json.setStatus(statusCode.getCode());

        for (String str : errors.keySet()) {
            json.addFieldError(str, contextReader.getMessage(errors.get(str)));
        }

        LOG.info(json.toString());

        return json;
    }

    /**
     * 参数错误: Field
     *
     * @param errors
     *
     * @return
     */
    public static JsonObjectBase buildFieldError(Map<String, String> errors, Map<String, Object[]> argsMap,
                                                 ErrorCode statusCode) {

        JsonObjectError json = new JsonObjectError();
        json.setStatus(statusCode.getCode());

        for (String str : errors.keySet()) {

            try {
                json.addFieldError(str, contextReader.getMessage(errors.get(str), argsMap.get(str)));
            } catch (NoSuchMessageException e) {
                json.addFieldError(str, errors.get(str));
            }
        }

        LOG.info(json.toString());

        return json;
    }

    /**
     * 参数错误: global
     */
    public static JsonObjectBase buildGlobalError(String error, ErrorCode errorCode) {

        JsonObjectError json = new JsonObjectError();
        json.setStatus(errorCode.getCode());

        json.addGlobalError(contextReader.getMessage(error));

        LOG.info(json.toString());

        return json;
    }

    /**
     */
    public static ModelAndView JsonObjectError2ModelView(JsonObjectError json) {

        ModelAndView model = new ModelAndView(new MappingJackson2JsonView());
        model.addObject(FrontEndInterfaceConstant.RETURN_SUCCESS, json.getSuccess());
        model.addObject(FrontEndInterfaceConstant.RETURN_MESSAGE, json.getMessage());
        model.addObject(FrontEndInterfaceConstant.STATUS_CODE_STRING, json.getStatus());
        model.addObject(FrontEndInterfaceConstant.SESSION_ID, json.getSessionId());

        return model;
    }

    /**
     * @param error
     *
     * @return
     */
    public static String getMessage(String error) {
        return contextReader.getMessage(error);
    }
}
