var appId = -1;
var envId = -1;
var version = "";

// 提交
$("#item_submit").on("click", function (e) {
    $("#error").addClass("hide");
    var user_name = $("#reg_user_name").val();
    var pwd = $("#reg_pwd").val();

    // 验证
    if (!user_name || !pwd) {
        $("#error").removeClass("hide");
        $("#error").html("注册失败, 表单不能为空或填写格式错误！");
        return;
    }
    $.ajax({
        type: "PUT",
        url: "/api/account/register",
        data: {
            "name": user_name,
            "password": pwd
        }
    }).done(function (data) {
        $("#error").removeClass("hide");
        if (data.success === "true") {
            alert(data.result);
            window.location.href = "/login.html";
        } else {
            $("#error").html(data.result);
        }
    });
});
