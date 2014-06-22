(function($) {
    var isUpdate = Util.param.isUpdate();
    var activityId = Util.param.getActivityId();
    
    if (isUpdate) {
        get();
    }
    
    var $questionItemWrap = $("#questionItemWrap");
    var tpl = $("#questionPool").html();
    var count = 4;
    //新建问题
    $("#create").on("click",createItem);
    
    function createItem() {
        var newItemHtml = Util.string.format(tpl, count);
        $questionItemWrap.append(newItemHtml);
        count++;
    }
    
    //提交
    $("#submit").on("click", function(e) {
         //activityId
        var activityId = Util.param.getActivityId();
        
        //questionPaper
        var questionElms = $("#questionItemWrap input:text");
        var questionPaperArr = [];
        $.each(questionElms, function(index, elm) {
            var val = $(elm).val();
            questionPaperArr.push(index + 1 + "|" +val);  
        });
        var questionPaper = questionPaperArr.join(',');
        
        //验证
        if (questionPaperArr.length < 3) {
            $("#error").show();
            return;
        }
        $("#error").hide();
        
        $.ajax({
            type: "POST",
            url: "/api/activity/forth/",
            data: {
                "activityId":activityId,
                "questionPaper" : questionPaper
            }
        })
        .done(function(data) {
            if (data.success === "true") {
                if (isUpdate) {
                    window.location.href = "publish.html?type=update&activityId=" + data.result.id;
                }
                else {
                    window.location.href = "publish.html?activityId=" + data.result.id
                                         + "&createIndex=4";
                }
            }
        });
    });
    
    function get () {
        $.ajax({
            type: "GET",
            url: "api/activity/forth/" + activityId
        })
        .done(function(data) {
            if (data.success === "true") {
                var quStr = data.result.questionPaper;
                
                if (!quStr) return;
                var quArr = quStr.split(",");
                var all = [];
                $.each(quArr, function(index, item) {
                    all.push(item.split("|"));
                });
                $.each(all, function(index, item) {
                    var id = item[0];
                    var val = item[1];
                    if (id > 3) {
                        createItem();
                    }
                    $("#question" + id).val(val);
                });
            }
            else {
                Util.input.whiteError($("#error"), data);
            }
        });
        
    }
})(jQuery);
