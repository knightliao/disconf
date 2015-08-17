package com.baidu.dsp.common.handler;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.baidu.dsp.common.constant.ErrorCode;
import com.baidu.dsp.common.exception.AccessDeniedException;
import com.baidu.dsp.common.exception.DocumentNotFoundException;
import com.baidu.dsp.common.exception.FieldException;
import com.baidu.dsp.common.exception.base.GlobalExceptionAware;
import com.baidu.dsp.common.utils.ParamValidateUtils;
import com.baidu.dsp.common.vo.JsonObjectBase;
import com.baidu.dsp.common.vo.JsonObjectError;
import com.baidu.dsp.common.vo.JsonObjectUtils;
import com.github.knightliao.apollo.utils.io.FileUtils;

/**
 * @author liaoqiqi
 * @version 2013-12-2
 */
@Service
public class MyExceptionHandler extends SimpleMappingExceptionResolver implements ApplicationContextAware {

    protected static final Logger LOG = LoggerFactory.getLogger(MyExceptionHandler.class);

    protected ApplicationContext context;

    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        this.context = arg0;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o,
                                         Exception e) {

        LOG.warn(request.getRequestURI() + " ExceptionHandler FOUND. " + e.toString() + "\t" + e.getCause());

        // PathVariable 出错
        if (e instanceof TypeMismatchException) {
            return getParamErrors((TypeMismatchException) e);

            // Bean 参数无法映射错误
        } else if (e instanceof InvalidPropertyException) {
            return getParamErrors((InvalidPropertyException) e);

            // @Valid 出错
        } else if (e instanceof BindException) {
            return ParamValidateUtils.getParamErrors((BindException) e);

            // 业务校验处理
        } else if (e instanceof FieldException) {
            return getParamErrors((FieldException) e);

        } else if (e instanceof DocumentNotFoundException) {

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            try {
                FileUtils.closeWriter(response.getWriter());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;

            // 用户没有请求方法的访问权限
        } else if (e instanceof AccessDeniedException) {
            LOG.warn("details: " + ((AccessDeniedException) e).getErrorMessage());
            return buildError("auth.access.denied", ErrorCode.ACCESS_NOAUTH_ERROR);

        } else if (e instanceof HttpRequestMethodNotSupportedException) {

            return buildError("syserror.httpmethod", ErrorCode.HttpRequestMethodNotSupportedException);

        } else if (e instanceof MissingServletRequestParameterException) {

            return buildError("syserror.param.miss", ErrorCode.MissingServletRequestParameterException);

        } else if (e instanceof GlobalExceptionAware) {

            LOG.error("details: ", e);
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
     *
     * @return
     */
    private ModelAndView getParamErrors(InvalidPropertyException e) {

        Map<String, String> errorMap = new HashMap<String, String>();
        errorMap.put(e.getPropertyName(), " parameter cannot find");
        JsonObjectBase jsonObject = JsonObjectUtils.buildFieldError(errorMap, ErrorCode.TYPE_MIS_MATCH);
        return JsonObjectUtils.JsonObjectError2ModelView((JsonObjectError) jsonObject);
    }

    /**
     * TypeMismatchException中获取到参数错误类型
     *
     * @param e
     */
    private ModelAndView getParamErrors(TypeMismatchException e) {

        Throwable t = e.getCause();

        if (t instanceof ConversionFailedException) {

            ConversionFailedException x = (ConversionFailedException) t;
            TypeDescriptor type = x.getTargetType();
            Annotation[] annotations = type != null ? type.getAnnotations() : new Annotation[0];
            Map<String, String> errors = new HashMap<String, String>();
            for (Annotation a : annotations) {
                if (a instanceof RequestParam) {
                    errors.put(((RequestParam) a).value(), "parameter type error!");
                }
            }
            if (errors.size() > 0) {
                return paramError(errors, ErrorCode.TYPE_MIS_MATCH);
            }
        }

        JsonObjectBase jsonObject = JsonObjectUtils.buildGlobalError("parameter type error!", ErrorCode.TYPE_MIS_MATCH);
        return JsonObjectUtils.JsonObjectError2ModelView((JsonObjectError) jsonObject);
    }

    /**
     * 业务字段错误校验，参数错误
     *
     * @param fe
     *
     * @return
     */
    private ModelAndView getParamErrors(FieldException fe) {

        if (fe.isGlobal()) {
            if (fe.getGlobalErrorArgs() == null || fe.getGlobalErrorArgs().length <= 0) {
                return buildError(fe.getGlobalErrorMsg(), ErrorCode.GLOBAL_ERROR);
            } else {
                return buildError(fe.getGlobalErrorMsg(), fe.getGlobalErrorArgs(), ErrorCode.GLOBAL_ERROR);
            }

        } else {

            // 构造error的映射
            Map<String, String> errorMap = fe.getMessageErrorMap();
            Map<String, Object[]> errorArgs = fe.getMessageErrorArgs();
            if (errorMap != null) {
                if (errorArgs != null) {
                    return paramError(errorMap, errorArgs, ErrorCode.FIELD_ERROR);
                }
                return paramError(errorMap, ErrorCode.FIELD_ERROR);
            }

            return paramError(new HashMap<String, String>(), ErrorCode.FIELD_ERROR);
        }
    }

    /**
     * 参数错误
     *
     * @param paramErrors
     * @param paramErrorArgs
     * @param errorCode
     *
     * @return
     */
    private ModelAndView paramError(Map<String, String> paramErrors, Map<String, Object[]> paramErrorArgs,
                                    ErrorCode errorCode) {

        JsonObjectBase jsonObject = JsonObjectUtils.buildFieldError(paramErrors, paramErrorArgs, errorCode);
        return JsonObjectUtils.JsonObjectError2ModelView((JsonObjectError) jsonObject);
    }

    /**
     * 参数错误
     *
     * @param paramErrors
     * @param errorCode
     *
     * @return
     */
    private ModelAndView paramError(Map<String, String> paramErrors, ErrorCode errorCode) {

        JsonObjectBase jsonObject = JsonObjectUtils.buildFieldError(paramErrors, errorCode);
        LOG.warn(jsonObject.toString());
        return JsonObjectUtils.JsonObjectError2ModelView((JsonObjectError) jsonObject);
    }

    /**
     * 全局的错误
     *
     * @param errorMsg
     * @param errorCode
     *
     * @return
     */
    private ModelAndView buildError(String errorMsg, ErrorCode errorCode) {

        JsonObjectBase jsonObject = JsonObjectUtils.buildGlobalError(errorMsg, errorCode);
        return JsonObjectUtils.JsonObjectError2ModelView((JsonObjectError) jsonObject);
    }

    /**
     * 全局错误
     *
     * @param errorMsg
     * @param args
     * @param errorCode
     *
     * @return
     */
    private ModelAndView buildError(String errorMsg, Object[] args, ErrorCode errorCode) {

        JsonObjectBase jsonObject = JsonObjectUtils.buildGlobalError(errorMsg, errorCode);
        return JsonObjectUtils.JsonObjectError2ModelView((JsonObjectError) jsonObject);
    }

}
