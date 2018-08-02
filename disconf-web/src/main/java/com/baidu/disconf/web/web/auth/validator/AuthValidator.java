package com.baidu.disconf.web.web.auth.validator;

import com.baidu.disconf.web.service.sign.form.SigninForm;
import com.baidu.disconf.web.service.sign.service.SignMgr;
import com.baidu.disconf.web.service.user.bo.User;
import com.baidu.disconf.web.service.user.dto.Visitor;
import com.baidu.disconf.web.service.user.form.PasswordModifyForm;
import com.baidu.disconf.web.service.user.service.UserMgr;
import com.baidu.disconf.web.utils.SHAUtil;
import com.baidu.dsp.common.exception.FieldException;
import com.baidu.ub.common.commons.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 权限验证
 *
 * @author liaoqiqi
 * @version 2014-7-2
 */
@Component
public class AuthValidator {

    @Autowired
    private UserMgr userMgr;

    @Autowired
    private SignMgr signMgr;

//    @Autowired
//    private AuthenticationProvider ldapAuthProvider;

    /**
     * 验证登录
     */
    public User validateLogin(SigninForm signinForm) {

        //
        // 校验用户是否存在
        //
        User user = userMgr.getUserByName(signinForm.getName());
        if (user == null) {
            throw new FieldException(SigninForm.Name, "user.role.not.exist", null);
        }

        // 校验密码
        try {
            if(user.getPassword().equals(SHAUtil.shaEncode(signinForm.getPassword()))){
                return user;
            }else{
                throw new FieldException(SigninForm.PASSWORD, "password.not.right", null);
            }
        } catch (Exception e) {
            throw new FieldException(SigninForm.PASSWORD, "password.not.right", null);
        }
    //        try {
    //            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signinForm.getName(), signinForm.getPassword());
    //            Authentication authentication = ldapAuthProvider.authenticate(authenticationToken);
    ////            System.out.println(authentication.getAuthorities().size());
    //        }catch(BadCredentialsException e){
    //            throw new FieldException(SigninForm.PASSWORD, "password.not.right", null);
    //        }

    }

    /**
     * 验证密码更新
     */
    public void validatePasswordModify(PasswordModifyForm passwordModifyForm) {

        Visitor visitor = ThreadContext.getSessionVisitor();

        User user = userMgr.getUserByName(visitor.getLoginUserName());

        // 校验密码
        if (!signMgr.validate(user.getPassword(), passwordModifyForm.getOld_password())) {
            throw new FieldException(PasswordModifyForm.OLD_PASSWORD, "password.not.right", null);
        }

        if (!passwordModifyForm.getNew_password().equals(passwordModifyForm.getNew_password_2())) {
            throw new FieldException(PasswordModifyForm.NEW_PASSWORD, "two.password.not.equal", null);
        }
    }

}
