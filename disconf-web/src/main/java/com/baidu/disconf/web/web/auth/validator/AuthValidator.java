package com.baidu.disconf.web.web.auth.validator;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.disconf.web.service.role.bo.RoleEnum;
import com.baidu.disconf.web.service.sign.form.SigninForm;
import com.baidu.disconf.web.service.sign.service.SignMgr;
import com.baidu.disconf.web.service.user.bo.User;
import com.baidu.disconf.web.service.user.dto.Visitor;
import com.baidu.disconf.web.service.user.form.PasswordModifyForm;
import com.baidu.disconf.web.service.user.service.UserMgr;
import com.baidu.disconf.web.tools.LDAPUtil;
import com.baidu.dsp.common.exception.FieldException;
import com.baidu.ub.common.commons.ThreadContext;

/**
 * 权限验证
 *
 * @author liaoqiqi
 * @version 2014-7-2
 */
@Component
public class AuthValidator {

    @Autowired
    private SignMgr signMgr;

    @Autowired
    private UserMgr userMgr;

    /**
     * 验证登录
     */
    public void validateLogin(SigninForm signinForm) {

        //
        // 校验用户是否存在
        //
        User user = signMgr.getUserByName(signinForm.getName());
        if (user == null) {
            throw new FieldException(SigninForm.Name, "user.not.exist", null);
        }

        // 校验密码
        if (!signMgr.validate(user.getPassword(), signinForm.getPassword())) {
            throw new FieldException(SigninForm.PASSWORD, "password.not.right", null);
        }
    }

    public boolean validateLADPLogin(SigninForm signinForm) {
        // 校验用户是否存在
        if (StringUtils.containsIgnoreCase(signinForm.getName(), "BG") || StringUtils
                .containsIgnoreCase(signinForm.getName(), "BL")) {
            if (!LDAPUtil.validateByLdap(signinForm.getName(), signinForm.getPassword())) {
                throw new FieldException("username.or.password.not.exist", null);
            }

            User user = signMgr.getUserByName(signinForm.getName());
            if (user == null) {
                user = new User();
                user.setName(signinForm.getName());
                user.setToken(UUID.randomUUID().toString());
                user.setPassword(UUID.randomUUID().toString());
                user.setRoleId(RoleEnum.NORMAL.getValue());
                user.setOwnApps("2");
                userMgr.create(user);
            }
            return true;
        }
        return false;
    }

    /**
     * 验证密码更新
     */
    public void validatePasswordModify(PasswordModifyForm passwordModifyForm) {

        Visitor visitor = ThreadContext.getSessionVisitor();

        User user = userMgr.getUser(visitor.getLoginUserId());

        // 校验密码
        if (!signMgr.validate(user.getPassword(), passwordModifyForm.getOld_password())) {
            throw new FieldException(PasswordModifyForm.OLD_PASSWORD, "password.not.right", null);
        }

        if (!passwordModifyForm.getNew_password().equals(passwordModifyForm.getNew_password_2())) {
            throw new FieldException(PasswordModifyForm.NEW_PASSWORD, "two.password.not.equal",
                    null);
        }
    }
}
