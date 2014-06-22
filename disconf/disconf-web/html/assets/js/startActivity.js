(function($) {
    var isUpdate = Util.param.isUpdate();
    var activityId = Util.param.getActivityId ();
    $("#startTime" ).datepicker({
         format: 'yyyy/mm/dd',
         startDate: new Date(),
         autoclose:true,
         todayHighlight: true,
         todayBtn:"linked"
    });
    $("#endTime").datepicker({
        format: 'yyyy/mm/dd',
        startDate: new Date(),
        todayHighlight:true,
        autoclose:true,
        todayBtn:"linked"
    });
    $("#startTime" ).datepicker("setDate", new Date());
    $("#endTime" ).datepicker("setDate", new Date());
    UE.getEditor('content');
    $.initProv($("#province"), $("#city"));
    
    if (isUpdate) {
        get();
    }
    
    function get() {
        $.ajax({
            type: "GET",
            url: "/api/activity/first/" + activityId
        })
        .done(function(data) {
            if (data.success === "true") {
                var result = data.result;
                var startTime = result.startTime.slice(0, 4) + "/" 
                              + result.startTime.slice(4, 6) + "/"
                              + result.startTime.slice(6, 8);
                var endTime = result.endTime.slice(0, 4) + "/" 
                              + result.endTime.slice(4, 6) + "/"
                              + result.endTime.slice(6, 8);
                var startTime2 = result.startTime2.slice(0, 2) + ":" + result.startTime2.slice(2, 4);
                var endTime2 = result.endTime2.slice(0, 2) + ":" + result.endTime2.slice(2, 4);
                $("#activityName").val(result.activityName);
                $.initProv($("#province"), $("#city"), result.province, result.city);
                $("#location").val(result.location);
                $("#startTime" ).val(startTime);
                $("#endTime").val(endTime);
                $("#startTime2" ).val(startTime2);
                $("#endTime2").val(endTime2);
                $("#peopleNum").val(result.peopleNum);
                UE.getEditor('content').setContent(result.content);
            }
            else {
                Util.input.whiteError($("#error"), data);
            }
        });
    }

    function send() {
        var activityName = $("#activityName").val();
        var provinceVal = $("#province").val();
        var cityVal = $("#city").val();
        province = (provinceVal != "-1") ? $._cityInfo[provinceVal].name : "";
        city = (cityVal != "-1") ? $._cityInfo[provinceVal].cities[cityVal] : "";
        
        var location = $("#location").val();
        var startTime = $( "#startTime" ).datepicker( "getDate" );
        var endTime = $("#endTime").datepicker( "getDate" );
        startTime = startTime ? Util.date.dateToNumber(startTime) : null;
        endTime = endTime ? Util.date.dateToNumber(endTime) : null;
        var startTime2 = $("#startTime2").val().replace(":", "");
        var endTime2 = $("#endTime2").val().replace(":", "");
        var peopleNum = $("#peopleNum").val();
        var content = UE.getEditor('content').getContent();
        //验证
        if (!activityName || !province || !city
            || !location || !startTime || !endTime
            || !startTime2 || !endTime2
            || !peopleNum || !content) {
            $("#error").show();
            return;
        }
        $("#error").hide();
        
        var method = "POST";
        var url = "/api/activity/first/";
        if (isUpdate) {
            method = "PUT";
            url = "/api/activity/first/" + activityId;
        }
        
        $.ajax({
            type: method,
            url: url,
            data: {
                "activityName" : activityName,
                "city": city,
                "province": province,
                "location" : location,
                "startTime" : startTime,
                "endTime" : endTime,
                "startTime2" : startTime2,
                "endTime2" : endTime2,
                "peopleNum" : peopleNum,
                "content" : content
            }
        })
        .done(function(data) {
            if (data.success === "true") {
                if (isUpdate) {
                    window.location.href = "ticketDiscount.html?type=update&activityId=" + data.result.id;
                }
                else {
                    window.location.href = "ticketDiscount.html?activityId=" + data.result.id
                                         + "&createIndex=1";
                }
            }
            else {
                Util.input.whiteError($("#error"), data);
            }
        });
    }
    
    $("#submit").on("click", send);
    
    
})(jQuery);
