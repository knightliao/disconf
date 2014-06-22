(function($) {
    var isUpdate = Util.param.isUpdate();
    var activityId = Util.param.getActivityId();
    
    if (isUpdate) {
        get();
    }
    
    $("#submit").click(function (e) {
        var email = $("#email").is("checked") ? "1" : "0";
        var note = $("#note").is("checked") ? "1" : "0";
        var wechat = $("#wechat").is("checked") ? "1" : "0";
        var union = $("#union").is("checked") ? "1" : "0";
        var sns = $("#sns").is("checked") ? "1" : "0";
        var pushMethod = email + note + wechat + union + sns;
        
        $.ajax({
            type: "POST",
            url: "/api/activity/six",
            data: {
                "activityId": activityId,
                "pushMethod": pushMethod
            }
        })
        .done(function( data ) {
           if (data.success === "true") {
                window.location.href = "index.html";
            }
        });
    });
    
    function get () {
        $.ajax({
            type: "GET",
            url: "api/activity/six/" + activityId
        })
        .done(function(data) {
            if (data.success === "true") {
                var pm = data.result.pushMethod.split("");
                if (pm[0] == 1) {
                    $("#email").attr("checked",'true');
                }
            }
        });
    }
    
})(jQuery);
