(function ($) {

    $("#indexMain").attr("href", "/");

    getSession2Redirect();

    // 发送登录请求
    $(".form-submit").on("click", function () {

        var me = this;
        var email = $("#name").val();
        var pwd = $("#password").val();
        var remember = $("#inlineCheckbox2").is(':checked') ? 1 : 0;

        // 验证
        if (email.length <= 0 || !pwd) {
            $("#loginError").show();
            return;
        }

        $.ajax({
            type: "POST",
            url: "/api/account/signin",
            data: {
                "name": email,
                "password": pwd,
                "remember": remember
            }
        }).done(function (data) {
            if (data.success === "true") {
                window.VISITOR = data.result.visitor;
                $("#loginError").hide();
                headShowInit();
                window.location.href = "/main.html";
            } else {
                Util.input.whiteError($("#loginError"), data);
                $("#loginError").show();
            }
        });
    });

})(jQuery);