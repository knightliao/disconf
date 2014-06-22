(function($) {
    var isUpdate = Util.param.isUpdate();
    var activityId = Util.param.getActivityId();
    
    if (isUpdate) {
        get();
    }
    
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
                if (isUpdate) {
                    window.location.href = "questionnaire.html?type=update&activityId=" + data.result.id;
                }
                else {
                    window.location.href = "questionnaire.html?type=create&activityId=" + data.result.id
                                         + "&createIndex=3";
                }
            }
        });
    });
    
    function get () {
        $.ajax({
            type: "GET",
            url: "api/activity/third/" + activityId
        })
        .done(function(data) {
            if (data.success === "true") {
                var regStr = data.result.applicationData;
                if (!regStr) return;
                var regArr = regStr.split(",");
                var all = [];
                $.each(regArr, function(index, item) {
                    all.push(item.split("|"));
                });
                $.each(all, function(index, item) {
                    var id = item[0];
                    var val = item[1];
                    switch (id) {
                        case "0":
                        $("#name").val(val);
                        break;
                        case "1":
                        $("#phone").val(val);
                        break;
                        case "2":
                        $("#email").val(val);
                        break;
                        case "3":
                        $("#company").val(val);
                        $("#company").show();
                        break;
                        case "4":
                        $("#profession").val(val);
                        $("#profession").show();
                        break;
                        case "5":
                        $("#school").val(val);
                        $("#school").show();
                        break;
                        case "6":
                        $("#year").val(val);
                        $("#year").show();
                        break;
                        case "7":
                        $("#major").val(val);
                        $("#major").show();
                        break;
                        case "7":
                        $("#remark").val(val);
                        $("#remark").show();
                        break;
                    }
                });
            }
            else {
                Util.input.whiteError($("#error"), data);
            }
        });
        
    }
    
})(jQuery);
