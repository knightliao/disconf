(function($) {
    var activityId = Util.param.getActivityId();
    
    get();
    function get() {
        $.ajax({
            type: "GET",
            url: "/api/account/advertiser/info2"
        })
        .done(function(data) {
            if (data.success === "true") {
               var result = data.result;
               $("#alipyId").val(result.alipyId);
               $("#bankAccountName").val(result.bankAccountName);
               $("#bankId").val(result.bankId);
               $("#bankName").val(result.bankName);
               $("#bankaddress").val(result.bankaddress);
            }
            else {
                Util.input.whiteError($("#error"), data);
            }
        });
    }
    
    
    //提交
    $("#submit").on("click", function(e) {
        var me = this;
        var alipyId = $("#alipyId").val();
        var bankAccountName = $("#bankAccountName").val();
        var bankId = $("#bankId").val();
        var bankName = $("#bankName").val();
        var bankaddress = $("#bankaddress").val();
        //验证
        if (!alipyId || !bankAccountName || !bankId 
            || !bankName || !bankaddress
           ) {
           $("#error").show();
           $("#error").html("表单不能为空或填写格式错误！");
           return;
        }
        $.ajax({
            type: "PUT",
            url: "/api/account/advertiser/info2",
            data: {
                "alipyId": alipyId,
                "bankAccountName": bankAccountName,
                "bankId": bankId,
                "bankName": bankName,
                "bankaddress": bankaddress
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
