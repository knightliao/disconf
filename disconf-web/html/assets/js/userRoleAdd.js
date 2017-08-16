var roleId = -1;
getSession();
//
// 获取APP信息
//
$.ajax({
    type: "GET",
    url: "/api/app/list"
}).done(
    function (data) {
        if (data.success === "true") {
            var html = "";
            var result = data.page.result;
            $.each(result, function (index, item) {
                html += '<input type="checkbox" name="appCheckbox" value="' + item.id + '"><label style="margin-right:10px;">'
                     + item.name + '</label>';
            });
            $("#appChoice").html(html);
        }
    });

// 提交
$("#item_submit").on("click", function (e) {
    $("#error").addClass("hide");
    var name = $("#name").val();
    var password = $("#password").val();
    var passwordConfirm = $("#passwordConfirm").val();
    var checkedAppIds="";
    $("input:checkbox[name=appCheckbox]:checked").each(function(){
        checkedAppIds+=$(this).val()+",";
    })
    if(checkedAppIds != ""){
        checkedAppIds=checkedAppIds.substring(0,checkedAppIds.length-1);
    }

    // 验证
    if (password != passwordConfirm) {
        $("#error").removeClass("hide");
        $("#error").html("密码和确认密码不相同！");
        return;
    }
    if (!name || roleId<1) {
        $("#error").removeClass("hide");
        $("#error").html("表单不能为空或填写格式错误！");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/api/user/manage/add",
        data: {
            "name": name,
            "password":password,
            "ownApps": checkedAppIds,
            "roleId": roleId
        }
    }).done(function (data) {
        $("#error").removeClass("hide");
        if (data.success === "true") {
            $("#error").html("添加成功！");
        } else {
            Util.input.whiteError($("#error"), data);
        }
    });
});
