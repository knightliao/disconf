(function($) {
    var activityId = Util.param.getActivityId();
    
    get();
    function get() {
        $.ajax({
            type: "GET",
            url: "/api/companycard/detail"
        })
        .done(function(data) {
            if(data.success === "true") {
                var result = data.result;
                if (!result.length) return;
                $.each(result, function(index, item) {
                    if (item.name == "金卡") {
                        $("#goldId").val(item.cardId);
                        $("#goldDiscount").val(item.discount);
                        $("#goldScore").val(item.coinNum);
                        $("#goldStart").val(item.startCoinNum);
                    }
                    else if (item.name == "白金卡") { 
                        $("#platinumId").val(item.cardId);
                        $("#platinumDiscount").val(item.discount);
                        $("#platinumScore").val(item.coinNum);
                        $("#platinumStart").val(item.startCoinNum);
                    }
                    else if (item.name == "黑卡"){
                        $("#blackId").val(item.cardId);
                        $("#blackDiscount").val(item.discount);
                        $("#blackScore").val(item.coinNum);
                        $("#blackStart").val(item.startCoinNum);
                    }
                });
            }
            else {
                Util.input.whiteError($("#error"), data);
            }
        });
    }
    
    
    //提交
    $("#submit").on("click", function(e) {
        var me = this;
       //cardData
        var gold = [$("#goldId").val(), $("#goldDiscount").val(), $("#goldScore").val(), $("#goldStart").val()].join("|");
        var platinum = [$("#platinumId").val(), $("#platinumDiscount").val(), $("#platinumScore").val(), $("#platinumStart").val()].join("|");
        var black = [$("#blackId").val(), $("#blackDiscount").val(), $("#blackScore").val(), $("#blackStart").val()].join("|");
        var cardData = [gold, platinum, black].join();
        //验证
        if (!$("#goldId").val() || !$("#goldDiscount").val() || !$("#goldScore").val() || !$("#goldStart").val()
            || !$("#platinumId").val() || !$("#platinumDiscount").val() || !$("#platinumScore").val() || !$("#platinumStart").val()
            || !$("#blackId").val() || !$("#blackDiscount").val() || !$("#blackScore").val() || !$("#blackStart").val()
            ) {
            $("#error").show();
            $("#error").html("表单不能为空或填写格式错误！");
            return;
        }
        $("#error").hide();
        $.ajax({
            type: "PUT",
            url: "/api/companycard/detail",
            data: {
                "cardData": cardData
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
