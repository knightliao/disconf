(function ($) {


    getSession();

    index();

    var G_app_id = 0;
    var G_env_id = 0;
    var G_version = '';

    function index(){
        //
        // 渲染应用列表
        //
        $.ajax({
            type: "GET",
            url: "/api/app/list"
        }).done(
            function (data) {
                if (data.success === "true") {
                    var result = data.page.result;
                    $.each(result, function (index, item) {
                        html += '<li><a rel=' + item.id + ' href="#">'
                            + item.name + '</a></li>';
                    });
                    $("#appChoice").html(html);
                }
            });

        $("#appChoice").on('click', 'li a', function () {
            $("#appChoiceA span:first-child").text($(this).text());
            G_app_id = $(this).attr('rel');
            fetchVersion(G_app_id);
        });

//
// 获取版本信息
//
        function fetchVersion(appId) {

            $.ajax({
                type: "GET",
                url: "/api/web/config/versionlist?appId=" + appId
            }).done(function (data) {
                if (data.success === "true") {
                    var html = '<li><a rel="" href="#">' + "全部" + '</a></li>';
                    var result = data.page.result;
                    $.each(result, function (index, item) {
                        html += '<li><a rel=' + item + ' href="#">' + item + '</a></li>';
                    });
                    $("#versionChoice").html(html);
                }
            });

            $("#versionChoice").on('click', 'li a', function () {
                $("#versionChoiceA span:first-child").text($(this).text());
                G_version = $(this).attr('rel');

            });
        }

//
// 获取Env信息
//
        $.ajax({
            type: "GET",
            url: "/api/env/list"
        }).done(
            function (data) {
                if (data.success === "true") {
                    var html = '<li><a rel="0" href="#">' + "全部" + '</a></li>';
                    var result = data.page.result;
                    $.each(result, function (index, item) {
                        html += '<li><a rel=' + item.id + ' href="#">'
                            + item.name + '</a></li>';
                    });
                    $("#envChoice").html(html);
                }
            });
        $("#envChoice").on('click', 'li a', function () {
            $("#envChoiceA span:first-child").text($(this).text());
            G_env_id = $(this).attr('rel');
        });

        var getData = function( page ){
            G_page = page || "1";
            $.ajax({
                url : "/api/configHistory/list",
                type: "GET",
                data :  {
                    pageNo 	: G_page,
                    pageSize: 20,
                    appId    : G_app_id,
                    envId    : G_env_id,
                    version    : G_version,
                    configName    : $("#configName").val()
                },
                dataType : 'JSON',
                success : function(data){
                    if (data.success === "true") {
                        //存在popover，就销毁
                        if($('.popover').length>0){
                            $('.popover').remove();
                        }
                        setData(data.page.result);
                        //绑定事件
                        bindDetailEvent(data.page.result);
                        cateTree.callPage({
                            total 		: data.page.totalCount,
                            pageSize 	: data.page.pageSize,
                            onPage 		: data.page.pageNo
                        })
                    }
                }
            })
        }

        var setData = function( data ){
            var html = '';
            for(i=0; i<data.length; i++){
                html += '<tr>'+
                    '<td>' + data[i].id + '</td>' +
                    '<td>' + data[i].appName + '</td>' +
                    '<td>' + data[i].envName + '</td>' +
                    '<td>' + data[i].version + '</td>' +
                    '<td>' + data[i].configName + '</td>' +
                    '<td><a href="#" type="button" class="btn btn-success oldValueFetch" data-container="body" data-toggle="popover" data-content="<pre>'+Util.input.escapeHtml(Util.input.escapeHtml(data[i].oldValue))+'</pre>">点击获取</a></td>' +
                    '<td><a href="#" type="button" class="btn btn-success newValueFetch" data-container="body" data-toggle="popover" data-content="<pre>'+Util.input.escapeHtml(Util.input.escapeHtml(data[i].newValue))+'</pre>">点击获取</a></td>' +
                    '<td>' + data[i].updateBy + '</td>' +
                    '<td>' + data[i].updateTime + '</td>' +
                    '</tr>';
            }
            $('.table tbody').html(html);
        }
        var cateTree = new G.pageTree();
        cateTree.init("#config_history_page");
        cateTree.setCallback(getData);

        //查询
        $(".search").on("click", function () {
            getData(1);
            return false;
        })
    }

    // 详细列表绑定事件
    function bindDetailEvent(result) {
        $(".oldValueFetch").popover({html : true,placement:'bottom' });
        $(".newValueFetch").popover({html : true,placement:'bottom' });
        //if (result == null) {
        //    return;
        //}
        //$.each(result, function (index, item) {
        //    var id = item.id;
        //
        //
        //    // 绑定事件
        //    $(".oldValueFetch" + id).on('click', function () {
        //        fetchValue(id, 1);
        //    });
        //
        //    $(".newValueFetch" + id).on('click', function () {
        //        fetchValue(id, 2);
        //    });
        //
        //});

    }

    //
    // value
    //
    function fetchValue(configHistoryId, valueType) {

        $.ajax({
            type: "GET",
            url: "/api/configHistory/value",
            data :  {
                id 	: configHistoryId,
                type: valueType
            },
        }).done(
            function (data) {
                if (data.success === "true") {
                    var result = data.result;

                    alert(Util.input.escapeHtml(result));
                }
            });
    }


})(jQuery);
