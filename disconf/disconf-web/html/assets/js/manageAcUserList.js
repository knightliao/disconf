(function($) {
    var mainTpl = $("#tbodyTpl").html();
    var detalTpl = $("#detailTbodyTpl").html();
    var activityId = Util.param.getActivityId();
    
    //页面初始化 入口
    get();
    //详细列表
    function get() {
        $.ajax({
            type: "GET",
            url: "/api/activity/joininfo/" + activityId,
            data: {
                "page.order" : "desc",
                "page.orderBy" : "id",
                "page.pageNo" : 1,
                "page.pageSize" : 100,
                "status": status
            }
        })
        .done(function(data) {
            if (data.success === "true") {
                var html = "";
                var result = data.page.result;
                $.each(result, function(index, item) {
                    html += rendItem(item);
                });
                $("#tbodyWrap").html(html);
                bindEvent(result);
            }
        });
    }
     //渲染详细列表
    function rendItem(item) {
        return Util.string.format(
            mainTpl,
            item.payId,
            item.name,
            item.phone,
            Util.date.dateFormat(item.createTime),
            item.ticketName,
            item.ticketNum,
            item.money,
            "支付宝",
            ["未支付", "支付"][item.state]
        );
    }

    $( "#dialog" ).dialog({
        autoOpen: false,
        width: 600,
        modal: true,
        dialogClass: "alertWrap",
        buttons: [
            {
                text: "保存",
                click: function() {
                    submitEdit.call(this, function() {
                        $(this).dialog( "close" );
                    });
                }
            },
            {
                text: "关闭",
                click: function() {
                    $( this ).dialog( "close" );
                }
            }
        ]
    });
    function bindEvent(result) {
        $.each(result, function(index, item) {
            var customerId = item.customerId;
            var payId = item.payId;
            //编辑绑定
            $("#edit" + payId).on("click", function(e) {
                reqAndRendEdit(customerId, function() {
                    $( "#dialog" ).dialog( "open" );
                    e.preventDefault();
                });
            });
        });
    }
    
     //详细列表删除
    function reqAndRendEdit(id, callback) {
        $.ajax({
            type: "GET",
            url: "api/customer/" + id
        })
        .done(function(data) {
            if (data.success === "true") {
                var result = data.result;
                rendEditItem(result);
                callback();
            }
        });
    }
    
    function rendEditItem(data) {
        $("#form").attr("rel", data.customerId);
        $("#name").val(data.name);
        $("#email").val(data.email);
        $("#company").val(data.company);
        $("#job").val(data.job);
        $("#major").val(data.major);
        $("#school").val(data.school);
        $("#graduateYear").val(data.graduateYear);
        $("#remarks").val(data.remarks);
    }

    //详细列表编辑
    function submitEdit(callback) {
        var me = this;
        var id = $("#form").attr("rel");
        var name = $("#name").val();
        var email = $("#email").val();
        var company = $("#company").val();
        var job = $("#job").val();
        var major = $("#major").val();
        var school = $("#school").val();
        var graduateYear = $("#graduateYear").val();
        var remarks = $("#remarks").val();
        //验证
        /*
       if (!name || !email || !company
           || !job || !major || !school
           || !graduateYear || !remarks ) {
           $("#error").show();
           return;
       }
       */
        $.ajax({
            type: "PUT",
            url: "api/customer/" + id,
            data: {
                "name" : name,
                "email" : email,
                "company" : company,
                "job" : job,
                "major" : major,
                "school" : school,
                "graduateYear" : graduateYear,
                "remarks" : remarks
            }
        })
        .done(function(data) {
            if (data.success === "true") {
                callback.call(me);
            }
        });
    }
   
    

})(jQuery);
