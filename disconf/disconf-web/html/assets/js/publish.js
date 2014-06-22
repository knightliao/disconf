(function($) {
    var isUpdate = Util.param.isUpdate();
    var activityId = Util.param.getActivityId();
    
    $("#fileupload").attr("data-url", "/api/activity/five/" + activityId);
    $("#uploadImg").on("click", function() {
        $("#fileupload").trigger("click");
    });
    //filePath
    var imgPath;
    $('#fileupload').fileupload({
        dataType: 'json',
        done: function (e, data) {
            imgPath = data.result.result.path;
            $("#imgShow").attr("src", imgPath);
        },
        fail: function() {
            console.log("fail");
        }
    });
    //提交
    $("#submit").click(function (e) {
        //验证
        if (!activityId || !imgPath) {
            $("#error").show();
            return;
        }
        
        $.ajax({
            type: "POST",
            url: "/api/activity/five2/",
            data: {
                "activityId":activityId,
                "filePath" : imgPath
            }
        })
        .done(function(data) {
            if (data.success === "true") {
                if (isUpdate) {
                    window.location.href = "autoPush.html?type=update&activityId=" + data.result.id;
                }
                else {
                    window.location.href = "autoPush.html?activityId=" + data.result.id
                                         + "&createIndex=5";
                }
            }
        });
    });
    
    //读取
    $.ajax({
        type: "GET",
        url: "/api/activity/five/" + activityId
    })
    .done(function(data) {
        if (data.success === "true") {
            var result = data.result;
            imgPath = result.posterImagePath;
            var detailTime = result.startTime+ " ~ " + result.endTime;
            var ticketHtml = "";
            var ticketTpl = [
                '<p class="publish-detail-item">',
                     '<span class="publish-detail-item-key">{0}:</span>',
                     '<span>{1}</span>',
                 '</p>'
            ].join("");
            $.each(result.ticketList, function (key, item) {
                if (!item) {
                    return;
                }
                ticketHtml += Util.string.format(
                    ticketTpl,
                    item.name,
                    item.limitnum + "张  ~" + item.price + "元/张"
                );
            });
                           
            $("#detailTitle").html(result.activityName);
            $("#detailTime").html(detailTime);
            $("#detailLocation").html(result.province + result.city + result.location + '');
            $("#detailPeopleNum").html(result.peopleLimit);
            $("#detailTicketWrap").html(ticketHtml);
            $("#imgShow").attr("src", imgPath);
        }
        else {
            Util.input.whiteError($("#error"), data);
        }
    });
   
})(jQuery);
