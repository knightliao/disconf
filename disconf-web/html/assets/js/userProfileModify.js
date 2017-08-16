(function ($) {

    $.ajax({
        type: "GET",
        url: "/api/account/session",
        timeout: 3000 // 3s timeout
    }).done(function (data) {
        if (data.success === "true") {
            $("#userId").val(data.result.visitor.id);
            $("#name").text(data.result.visitor.name);
        } else {
            window.location.href = "/login.html";
        }
    }).fail(function (xmlHttpRequest, textStatus) {
        window.location.href = "/login.html";
    });

    // 提交
    $("#submit").on("click", function (e) {
        $("#error").addClass("hide");
        var name = $("#name").text();
        var password = $("#password").val();
        var passwordConfirm = $("#passwordConfirm").val();

        // 验证
        if (password != passwordConfirm) {
            $("#error").removeClass("hide");
            $("#error").html("密码和确认密码不相同！");
            return;
        }
        $.ajax({
            type: "POST",
            url: "/api/account/updateProfile",
            data: {
                "userId": $("#userId").val(),
                "password": password
            }
        }).done(function (data) {
            $("#error").removeClass("hide");
            if (data.success === "true") {
                $("#error").html("修改成功！");
            } else {
                Util.input.whiteError($("#error"), data);
            }
        });
    });

})(jQuery);
