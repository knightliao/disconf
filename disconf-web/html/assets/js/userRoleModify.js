var roleId = -1;
(function ($) {
    getSession();

    var userId = Util.param.getUserId();
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
                fetchItem();
            }
        });


    //
    // 获取配置项
    //
    function fetchItem() {

        //
        // 获取此配置项的数据
        //
        $.ajax({
            type: "GET",
            url: "/api/user/manage/get/" + userId
        }).done(
            function (data) {
                if (data.success === "true") {
                    var result = data.result;
                    $("#name").text(result.name);
                    var ownAppIdList = result.ownAppIdList;
                    if(ownAppIdList!=null){
                        $.each(ownAppIdList, function (index, ownAppId) {
                            $("input:checkbox[name=appCheckbox]").not("input:checked").each(function(){
                                if(ownAppId == $(this).val()){
                                    $(this).attr("checked", true);
                                }
                            })
                        });
                    }
                    roleId = result.roleId;
                    $("#roleChoiceA span:first-child").text(result.role);
                }
            });
    }

    // 提交
    $("#submit").on("click", function (e) {
        $("#error").addClass("hide");
        var name = $("#name").text();
        var checkedAppIds="";
        $("input:checkbox[name=appCheckbox]:checked").each(function(){
            checkedAppIds+=$(this).val()+",";
        })
        if(checkedAppIds != ""){
            checkedAppIds=checkedAppIds.substring(0,checkedAppIds.length-1);
        }

        // 验证
        if (!name || roleId<1) {
            $("#error").removeClass("hide");
            $("#error").html("表单不能为空或填写格式错误！");
            return;
        }
        $.ajax({
            type: "POST",
            url: "/api/user/manage/update",
            data: {
                "userId": userId,
                "name":name,
                "ownApps": checkedAppIds,
                "roleId": roleId
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
