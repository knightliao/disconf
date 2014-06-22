(function($) {
    //提交
    $("#submit").on("click", function(e) {
        var me = this;
        var oldPassword = $("#oldPassword").val();
        var newPassword = $("#newPassword").val();
        var newPassword2 = $("#newPassword2").val();
        //验证
        if (!oldPassword || !newPassword || !newPassword2 ) {
           $("#error").show();
           $("#error").html("表单不能为空或填写格式错误！");
           return;
        }
        $.ajax({
            type: "PUT",
            url: "/api/auth/password",
            data: {
                "oldPassword": oldPassword,
                "newPassword": newPassword,
                "newPassword2": newPassword2
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
