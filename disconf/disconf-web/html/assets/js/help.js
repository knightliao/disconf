(function($) {

    //绑定事件
    var choiceArr = $("#choiceSide a");
    $.each(choiceArr, function(index, elm) {
        $(elm).on("click", function(e) {
            var id = $(this).attr("alt");
            $("#" + id).parent().show();
        });
    });
    
    //提交
    $("#submit").on("click", function(e) {
        //activityId
        var activityId = Util.param.getActivityId();
        
        //applicationData
        var name = $("#name").val();
        var phone = $("#phone").val();
        var email = $("#email").val();
        var applicationData = [
            [0, name].join("|"),
            [1, phone].join("|"),
            [2, email].join("|"),
        ].join();
         //验证
        if (!activityId || !name || !phone || !email) {
            $("#error").show();
            return;
        }
        $("#error").hide();
        $.ajax({
            type: "POST",
            url: "api/activity/third",
            data: {
                "activityId":activityId,
                "applicationData": applicationData
            }
        })
        .done(function(data) {
            if (data.success === "true") {
                window.location.href = "questionnaire.html?activityId=" + data.result.id;
            }
        });
    });
    
    
    
})(jQuery);
