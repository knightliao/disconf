(function ($) {

    getSession();

    var configId = Util.param.getConfigId();

    fetchItem();

    //
    // 获取配置项
    //
    function fetchItem() {

        //
        // 获取此配置项的数据
        //
        $.ajax({
            type: "GET",
            url: "/api/web/config/" + configId
        }).done(
            function (data) {
                if (data.success === "true") {
                    var result = data.result;
                    $("#app").text(
                            result.appName + ' (appid=' + result.appId
                            + ')');
                    $("#version").text(result.version);
                    $("#env").text(result.envName);
                    $("#key").text(result.key);
                    $("#value").val(result.value);
                    $("#currentData").text(
                            result.appName + " * " + result.version + " * "
                            + result.envName);
                    // 获取APP下的配置数据
                    fetchItems(result.appId, result.envId, result.version,
                        configId);
                }
            });
    }

    // 提交
    $("#submit").on("click", function (e) {
        $("#error").addClass("hide");
        var me = this;
        var value = $("#value").val();
        // 验证
        if (!value) {
            $("#error").removeClass("hide");
            $("#error").html("表单不能为空或填写格式错误！");
            return;
        }
        $.ajax({
            type: "PUT",
            url: "/api/web/config/item/" + configId,
            data: {
                "value": value
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

})(jQuery);
