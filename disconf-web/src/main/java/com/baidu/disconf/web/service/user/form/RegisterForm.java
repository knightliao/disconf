package com.baidu.disconf.web.service.user.form;

import com.baidu.dsp.common.form.RequestFormBase;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by liaomengge on 16/8/31.
 */
@Data
public class RegisterForm extends RequestFormBase {

    /**
     * 姓名
     */
    @NotNull(message = "name.empty")
    @NotEmpty(message = "name.empty")
    private String name;

    /**
     * 密码
     */
    @NotEmpty(message = "password.empty")
    @NotNull(message = "password.empty")
    private String password;
}
