<div class="foot clearfix">
    <p>邮箱：x-one@x-one.cc</p>
    <p>Copyright www.x-one.cc    All Rights Reserved</p>
</div>
<div id="registerDialog" title="注册" class="hide">
    <form class="registerWrap">
        <p class="register-item">
            <span class="register-item-key">商户名：</span>
            <input type="text" id="regCompanyName" class="register-item-value"/>
        </p>
        <p class="register-item">
            <span class="register-item-key">商户类型：</span>
            <input type="text" id="regCompanyType" class="register-item-value"/>
        </p>
        <p class="register-item">
            <span class="register-item-key">手机：</span>
            <input type="text" id="regPhone" class="register-item-value"/>
        </p>
        <p class="register-item">
            <span class="register-item-key">邮箱：</span>
            <input type="text" id="regEmail" class="register-item-value"/>
        </p>
        <p class="register-item">
            <span class="register-item-key">城市：</span>
            <input type="text" id="regCity" class="register-item-value"/>
        </p>
        <p class="register-item">
            <span class="register-item-key">申请说明：</span>
            <textarea id="regApplyinfo" class="register-item-value register-item-value-area"> </textarea>
        </p>
        <p class="register-item error hide" id="regError">表单选项不能为空或格式不对！</p>
    </form>
</div>
<div id="loginDialog" title="登录" class="hide">
    <form class="registerWrap">
        <p class="register-item">
            <span class="register-item-key login-item-key">手机：</span>
            <input type="text" id="loginPhone" class="register-item-value"/>
        </p>
        <p class="register-item">
            <span class="register-item-key login-item-key">密码：</span>
            <input type="password" id="loginPwd" class="register-item-value"/>
        </p>
        <p class="register-item login-item-remember">
            <input type="checkbox" id="loginCheck" checked="checked"/>
            <label for="loginCheck">记住密码</label>
        </p>
        <p class="register-item error hide" id="loginError">表单选项不能为空或格式不对！</p>
    </form>
</div>

<script src="assets/js/jquery-1.11.0.js"></script>
<script src="dep/jquery-ui-1.10.4.custom/js/jquery-ui-1.10.4.custom.js"></script>
<script src="dep/bootstrap/js/bootstrap.js"></script>
<script src="assets/js/util.js"></script>
<script src="assets/js/common.js"></script>