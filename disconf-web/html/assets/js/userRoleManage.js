(function ($) {


    getSession();


    fetchMainList();

    //
    // 渲染页面
    //
    function fetchMainList() {

        $.ajax({
            type: "GET",
            url: "/api/user/manage/get"
        }).done(function (data) {
            if (data.success === "true") {
                var html = "";
                var result = data.page.result;
                $.each(result, function (index, item) {
                    html += renderItem(item, index);
                });
                if (html != "") {
                    $("#userRoleBody").html(html);
                } else {
                    $("#userRoleBody").html("");
                }
                bindDetailEvent(result);
            }
        //绑定详细列表

        });
        var mainTpl = $("#tbodyTpl").html();
        // 渲染主列表
        function renderItem(item, i) {
            var appNames = "";
            var ownApps = item.ownAppList;
            if(ownApps!=null){
                $.each(ownApps, function (index, appName) {
                    appNames += appName + ',';
                });
                appNames=appNames.substring(0,appNames.length-1);
            }
            if(appNames == ""){
                appNames = "所有";
            }
            var update_link =  '<a target="_blank" href="user_role_modify.html?userId='
                    + item.id
                    + '"><i title="修改" class="icon-edit"></i></a>';
            var delete_link = '<a id="itemDel'
                + item.id
                + '" style="cursor: pointer; cursor: hand; " ><i title="删除" class="icon-remove"></i></a>';

            return Util.string.format(mainTpl,i+1,item.name, appNames,
                item.role,update_link,delete_link);
        }
    }


    // 详细列表绑定事件
    function bindDetailEvent(result) {
        if (result == null) {
            return;
        }
        $.each(result, function (index, item) {
            var id = item.id;

            // 绑定删除事件
            $("#itemDel" + id).on("click", function (e) {
                deleteDetailTable(id, item.name);
            });

        });

    }

    // 删除
    function deleteDetailTable(id, name) {

        var ret = confirm("你确定要删除吗 " + name + "?");
        if (ret == false) {
            return false;
        }

        $.ajax({
            type: "DELETE",
            url: "/api/user/manage/delete/" + id
        }).done(function (data) {
            if (data.success === "true") {
                fetchMainList();
            }
        });
    }

})(jQuery);
