var appId = -1;
var envId = -1;
var version = "";
getSession();

// 提交
$("#item_submit").on("click", function (e) {
    $("#error").addClass("hide");
    var old_password = $("#old_password").val();
    var new_password = $("#new_password").val();
    var new_password_2 = $("#new_password_2").val();

    // 验证
    if (!old_password || !new_password || !new_password_2) {
        $("#error").removeClass("hide");
        $("#error").html("表单不能为空或填写格式错误！");
        return;
    }
    $.ajax({
        type: "PUT",
        url: "/api/account/password",
        data: {
            "old_password": old_password,
            "new_password": new_password,
            "new_password_2": new_password_2
        }
    }).done(function (data) {
        $("#error").removeClass("hide");
        if (data.success === "true") {
            $("#error").html(data.result);
        } else {
            Util.input.whiteError($("#error"), data);
        }
    });
});
