(function($) {
    var mainTpl = $("#tbodyTpl").html();
    var detalTpl = $("#detailTbodyTpl").html();
    var status = Util.param.getStatus();
    
    //页面初始化 入口
    (function() {
        getSession(); //获取用户列表前需要先获得用户id
        bindSideNavEvent();
    })();
    
    function getSession() {
        $.ajax({
            type: "GET",
            url: "/api/account/session"
        })
        .done(function(data) {
            if (data.success === "true") {
               window.VISITOR = data.result.visitor;
               reqAndRendMain();
            }
        });
    }
    
    //绑定右边导航
    function bindSideNavEvent() {
        var $sideNav = $("#sidebarMenu");
        var lis = $sideNav.find("li");
        $.each(lis, function(index, item) {
                $(item).removeClass("focus");
                $(item).find("a").removeClass("focus");
        });
        var li;
        if (status == 0) {
            li = lis[2];
        }
        else if (status == 1) {
            li = lis[0];
        }
        else if (status == 2) {
            li = lis[1];
        }
        $(li).addClass("focus");
        $(li).find("a").addClass("focus");
    }

    //请求并渲染主列表
    function reqAndRendMain() {
        $.ajax({
            type: "GET",
            url: "/api/activity/user/" + VISITOR.id,
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
                    html += renderItem(item);
                });
                $("#manageIngBody").html(html);
                bindMainEvent(result);
                enterDocument(result);
                //console.log(data);
            }
        });
    }

    //渲染主列表
    function renderItem(item) {
        return Util.string.format(
            mainTpl,
            item.activityId,
            item.posterImagePath,
            item.activityName,
            item.province + item.city + item.location,
            item.startTime,
            item.participants,
            item.peopleLimit,
            item.money
        );
    }
    
    $( "#dialog" ).dialog({
        autoOpen: false,
        width: 1200,
        modal: true,
        dialogClass: "alertWrap",
        buttons: [
            {
                text: "关闭",
                click: function() {
                    $( this ).dialog( "close" );
                }
            }
        ]
    });
    function bindMainEvent(result) {
        $.each(result, function(index, item) {
            var activityId = item.activityId;
            //查看详细信息绑定
            /*
            $("#openDetailLink" + activityId).on("click", function(e) {
                reqAndRendDetail(activityId, function() {
                    $( "#dialog" ).dialog( "open" );
                    e.preventDefault();
                });
            });
            */
            //编辑绑定
            
            //删除绑定
            $("#deleteLink" + activityId).on("click", function(e) {
                $.ajax({
                    type: "DELETE",
                    url: "api/activity/" + activityId
                })
                .done(function(data) {
                    if (data.success === "true") {
                        reqAndRendMain();
                    }
                });
            });
            
        });
    }
    //根据status来区别进行中1 已完成2 和草稿0
    function enterDocument(result) {
        $.each(result, function(index, item) {
            var id = item.activityId;
            var $detailLink = $("#openDetailLink" + id);
            var $editLink = $("#editLink" + id);
            if (status == 2) {
                $editLink.hide();
            }
            else if (status == 0) {
                $detailLink.hide();
            }
        });
    }
     
    
    //详细列表
    function reqAndRendDetail(activityId, callback) {
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
                    html += renderDetailItem(item);
                });
                $("#manageIngDetailBody").html(html);
                bindDetailEvent(result);
                callback && callback();
            }
        });
    }
     //渲染详细列表
    function renderDetailItem(item) {
        return Util.string.format(
            detalTpl,
            item.customPayId,
            item.name,
            item.phone,
            item.email,
            item.company,
            item.job,
            item.school,
            item.graduateYear,
            item.remarks,
            '',
            '',
            '',
            '',
            item.createTime
        );
    }
    
    //详细列表绑定事件
    function bindDetailEvent(result) {
        $.each(result, function(index, item) {
            var id = item.customPayId;
            //绑定编辑事件
            var $remark = $("#detailRemark" + id);
            var $span = $remark.find("span");
            var $input = $remark.find("input");
            $("#detailRemark" + id).on("dblclick", function(e) {
                $span.hide();
                $input.show();
            });
            $input.on("focusout", function(e) {
                editTable(id, $(this).val());
                $span.show();
                $input.hide();
            });
            //绑定删除事件
            $("#detailDel" + id).on("click", function(e) {
                deleteDetailTable(id);
            });
        });
    }
    //详细列表编辑
    function editTable(id, remark) {
        $.ajax({
            type: "PUT",
            url: "api/customdata/" + id,
            data: {
                "remarks" : remark
            }
        })
        .done(function(data) {
            if (data.success === "true") {
                reqAndRendDetail(data.result.activityId);
            }
        });
    }
    //详细列表删除
    function deleteDetailTable(id) {
        $.ajax({
            type: "DELETE",
            url: "api/customdata/" + id
        })
        .done(function(data) {
            if (data.success === "true") {
                reqAndRendDetail(data.result.activityId);
            }
        });
    }
    

})(jQuery);
