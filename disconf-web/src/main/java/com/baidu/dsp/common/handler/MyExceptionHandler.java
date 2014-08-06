package com.baidu.dsp.common.handler;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.baidu.disconf.ub.common.utils.FileUtils;
import com.baidu.dsp.common.constant.ErrorCode;
import com.baidu.dsp.common.exception.DocumentNotFoundException;
import com.baidu.dsp.common.exception.FieldException;
import com.baidu.dsp.common.exception.base.GlobalExceptionAware;
import com.baidu.dsp.common.vo.JsonObjectBase;
import com.baidu.dsp.common.vo.JsonObjectError;
import com.baidu.dsp.common.vo.JsonObjectUtils;
import com.baidu.ub.common.log.AopLogFactory;

/**
 * 
 * @author liaoqiqi
 * @version 2013-12-2
 */
public class MyExceptionHandler extends SimpleMappingExceptionResolver
        implements ApplicationContextAware {

    private final static Logger LOG = AopLogFactory
            .getLogger(MyExceptionHandler.class);

    protected ApplicationContext context;

    public void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        this.context = arg0;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
            HttpServletResponse response, Object o, Exception e) {

        LOG.warn("ExceptionHandler FOUND. " + e.toString() + "\t"
                + e.toString());

        // PathVariable 出错
        if (e instanceof TypeMismatchException) {
            return getParamErrors((TypeMismatchException) e);

            // Bean 参数无法映射错误
        } else if (e instanceof InvalidPropertyException) {
            return getParamErrors((InvalidPropertyException) e);

            // @Valid 出错
        } else if (e instanceof BindException) {
            return getParamErrors((BindException) e);

            // 业务校验处理
        } else if (e instanceof FieldException) {
            return getParamErrors((FieldException) e);

            // 下载文件不存在
        } else if (e instanceof DocumentNotFoundException) {

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            try {
                FileUtils.closeWriter(response.getWriter());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;

        } else if (e instanceof HttpRequestMethodNotSupportedException) {

            return buildError("syserror.httpmethod",
                    ErrorCode.HttpRequestMethodNotSupportedException);

        } else if (e instanceof MissingServletRequestParameterException) {

            return buildError("syserror.param.miss",
                    ErrorCode.MissingServletRequestParameterException);

        } else if (e instanceof GlobalExceptionAware) {

            LOG.warn("details: ", e);
            GlobalExceptionAware g = (GlobalExceptionAware) e;
            return buildError(g.getErrorMessage(), g.getErrorCode());

        } else {

            LOG.warn("details: ", e);
            return buildError("syserror.inner", ErrorCode.GLOBAL_ERROR);
        }
    }

    /**
     * 参数转换出错
     * 
     * @param e
     * @return
     */
    private ModelAndView getParamErrors(InvalidPropertyException e) {

        Map<String, String> errorMap = new HashMap<String, String>();
        errorMap.put(e.getPropertyName(), "参数错误");
        JsonObjectBase jsonObject = JsonObjectUtils.buildFieldError(errorMap,
                ErrorCode.TYPE_MIS_MATCH);
        return JsonObjectUtils
                .JsonObjectError2ModelView((JsonObjectError) jsonObject);
    }

    /**
     * TypeMismatchException中获取到参数错误类型
     * 
     * @param e
     * @param model
     * @return 下午2:19:51 created by Darwin(Tianxin)
     */
    private ModelAndView getParamErrors(TypeMismatchException e) {

        Throwable t = e.getCause();

        if (t instanceof ConversionFailedException) {

            ConversionFailedException x = (ConversionFailedException) t;
            TypeDescriptor type = x.getTargetType();
            Annotation[] annotations = type != null ? type.getAnnotations()
                    : new Annotation[0];
            Map<String, String> errors = new HashMap<String, String>();
            for (Annotation a : annotations) {
                if (a instanceof RequestParam) {
                    errors.put(((RequestParam) a).value(), "参数类型错误!");
                }
            }
            if (errors.size() > 0) {
                return paramError(errors, ErrorCode.TYPE_MIS_MATCH);
            }
        }

        JsonObjectBase jsonObject = JsonObjectUtils.buildGlobalError("参数类型错误!",
                ErrorCode.TYPE_MIS_MATCH);
        return JsonObjectUtils
                .JsonObjectError2ModelView((JsonObjectError) jsonObject);
    }

    /**
     * 业务字段错误校验，参数错误
     * 
     * @param fe
     * @param mvc
     * @return
     */
    private ModelAndView getParamErrors(FieldException fe) {

        if (fe.isGlobal()) {

            return buildError(fe.getGlobalErrorMsg(), ErrorCode.GLOBAL_ERROR);

        } else {

            // 构造error的映射
            Map<String, String> paramErrors = new HashMap<String, String>();
            Map<String, String> errorMap = fe.getMessageErrorMap();
            if (errorMap != null) {
                for (String field : errorMap.keySet()) {

                    String message = errorMap.get(field);
                    paramErrors.put(field, message);
                }

                return paramError(paramErrors, ErrorCode.FIELD_ERROR);
            }

            return paramError(new HashMap<String, String>(),
                    ErrorCode.FIELD_ERROR);
        }
    }

    /**
     * 从bindException中获取到参数错误类型，参数错误
     * 
     * @param be
     *            下午2:07:29 created by Darwin(Tianxin)
     */
    private ModelAndView getParamErrors(BindException be) {

        // 构造error的映射
        Map<String, String> paramErrors = new HashMap<String, String>();
        for (Object error : be.getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError fe = (FieldError) error;
                String field = fe.getField();
                // 默认错误
                String errorMessage = fe.getDefaultMessage();

                // 如果有code,则从前往后遍历Code(特殊到一般),修改message为code所对应
                for (String code : fe.getCodes()) {
                    try {
                        context.getMessage(code, null,
                                Locale.SIMPLIFIED_CHINESE);
                        errorMessage = code;
                    } catch (Exception e) {
                    }
                }

                paramErrors.put(field, errorMessage);
            }
        }

        return paramError(paramErrors, ErrorCode.FIELD_ERROR);
    }

    /**
     * 参数错误
     * 
     * @param mvc
     * @param paramErrors
     * @return
     */
    private ModelAndView paramError(Map<String, String> paramErrors,
            ErrorCode errorCode) {

        JsonObjectBase jsonObject = JsonObjectUtils.buildFieldError(
                paramErrors, errorCode);
        return JsonObjectUtils
                .JsonObjectError2ModelView((JsonObjectError) jsonObject);
    }

    /**
     * 全局的错误
     * 
     * @param mvc
     * @param errorMsg
     * @param errorCode
     * @return
     */
    private ModelAndView buildError(String errorMsg, ErrorCode errorCode) {

        JsonObjectBase jsonObject = JsonObjectUtils.buildGlobalError(errorMsg,
                errorCode);
        return JsonObjectUtils
                .JsonObjectError2ModelView((JsonObjectError) jsonObject);
    }

}
