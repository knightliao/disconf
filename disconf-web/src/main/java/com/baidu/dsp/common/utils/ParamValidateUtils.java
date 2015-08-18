package com.baidu.dsp.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import com.baidu.dsp.common.constant.ErrorCode;
import com.baidu.dsp.common.context.ContextReader;
import com.baidu.dsp.common.vo.JsonObjectBase;
import com.baidu.dsp.common.vo.JsonObjectError;
import com.baidu.dsp.common.vo.JsonObjectUtils;

/**
 * 参数错误的工具
 *
 * @author liaoqiqi
 * @version 2014-3-3
 */
@Component
public class ParamValidateUtils {

    protected static final Logger LOG = LoggerFactory.getLogger(ParamValidateUtils.class);

    private static ContextReader contextReader;

    @Autowired(required = true)
    public ParamValidateUtils(@Qualifier("contextReaderImpl") ContextReader contextReader) {
        ParamValidateUtils.contextReader = contextReader;
    }

    /**
     * 参数错误
     *
     * @param paramErrors
     *
     * @return
     */
    private static ModelAndView paramError(Map<String, String> paramErrors, Map<String, Object[]> paramArgusErrors,
                                           ErrorCode errorCode) {

        JsonObjectBase jsonObject = JsonObjectUtils.buildFieldError(paramErrors, paramArgusErrors, errorCode);
        return JsonObjectUtils.JsonObjectError2ModelView((JsonObjectError) jsonObject);
    }

    /**
     * 从bindException中获取到参数错误类型，参数错误
     */
    public static ModelAndView getParamErrors(BindException be) {

        // 构造error的映射
        Map<String, String> paramErrors = new HashMap<String, String>();
        Map<String, Object[]> paramArgusErrors = new HashMap<String, Object[]>();
        for (Object error : be.getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError fe = (FieldError) error;
                String field = fe.getField();

                // 默认的message
                String message = fe.getDefaultMessage();
                try {

                    contextReader.getMessage(message, fe.getArguments());

                } catch (NoSuchMessageException e) {

                    // 如果有code,则从前往后遍历Code(特殊到一般),修改message为code所对应
                    for (int i = fe.getCodes().length - 1; i >= 0; i--) {
                        try {
                            String code = fe.getCodes()[i];
                            String info = contextReader.getMessage(code, fe.getArguments());
                            LOG.debug(code + "\t" + info);
                            message = code;
                        } catch (NoSuchMessageException e2) {
                            LOG.debug("");
                        }
                    }
                }

                // 最终的消息
                paramErrors.put(field, message);
                paramArgusErrors.put(field, fe.getArguments());
            }
        }

        return ParamValidateUtils.paramError(paramErrors, paramArgusErrors, ErrorCode.FIELD_ERROR);
    }

}
