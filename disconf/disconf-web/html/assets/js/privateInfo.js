(function($) {
    var activityId = Util.param.getActivityId();
    
    get();
    function get() {
        $.ajax({
            type: "GET",
            url: "/api/account/advertiser/info"
        })
        .done(function(data) {
            if (data.success === "true") {
               var result = data.result;
               $("#companyName").val(result.companyName);
               $("#companyType").val(result.companyType);
               $("#phone").val(result.phone);
               $("#email").val(result.email);
               $("#city").val(result.city);
               $("#qq").val(result.qq);
               $("#wechat").val(result.weixin_id);
               $("#wechatInfo").val(result.weixin_info);
               $("#wechatType").val(result.weixin_type);
            }
            else {
                Util.input.whiteError($("#error"), data);
            }
        });
    }
    
    
    //提交
    $("#submit").on("click", function(e) {
        var me = this;
        var emailReg = /^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})$/;
        var companyName = $("#companyName").val();
        var companyType = $("#companyType").val();
        var phone = $("#phone").val();
        var email = $("#email").val();
        var city = $("#city").val();
        var qq = $("#qq").val();
        var wechat = $("#wechat").val();
        var wechatInfo = $("#wechatInfo").val();
        var wechatType = $("#wechatType").val();
        
        
        //验证
        if (!companyName || !companyName || !city 
            || !qq || !wechat || !wechatInfo 
           || !emailReg.test(email) || phone.length !== 11) {
           $("#error").show();
           $("#error").html("表单不能为空或填写格式错误！");
           return;
        }
        $.ajax({
            type: "PUT",
            url: "/api/account/advertiser/info",
            data: {
                "companyName": companyName,
                "companyType": companyType,
                "phone": phone,
                "email": email,
                "city": city,
                "qq": qq,
                "weixin_id": wechat,
                "weixin_info": wechatInfo,
                "weixin_type": wechatType
            }
        })
        .done(function(data) {
            $("#error").show();
            if (data.success === "true") {
                $("#error").html("修改成功！");
            }
            else {
                Util.input.whiteError($("#error"), data);
            }
        });
    });

})(jQuery);
