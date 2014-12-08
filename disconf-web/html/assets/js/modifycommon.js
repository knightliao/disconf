//
// 获取指定配置下的配置数据
//
function fetchItems(appId, envId, version, curConfigId) {

    var parameter = ""

    url = "/api/web/config/simple/list";
    url += "?";
    url += "appId=" + appId + "&";
    url += "envId=" + envId + "&";
    url += "version=" + version + "&";

    $.ajax({
        type: "GET",
        url: url
    }).done(function (data) {
        if (data.success === "true") {
            var html = '<li style="margin-bottom:10px">配置文件/配置项列表</li>';
            var result = data.page.result;
            $.each(result, function (index, item) {
                html += renderItem(item);
            });
            $("#sidebarcur").html(html);
        }
    });
    var mainTpl = $("#tItemTpl").html();
    // 渲染主列表
    function renderItem(item) {

        var link = "";
        var key = "";
        if (item.type == "配置文件") {
            link = 'modifyFile.html?configId=' + item.configId;
            key = '<i title="配置文件" class="icon-file"></i>' + item.key;
        } else {
            link = 'modifyItem.html?configId=' + item.configId;
            key = '<i title="配置项" class="icon-leaf"></i>' + item.key;
        }

        var style = "";
        if (item.configId == curConfigId) {
            style = "active";
        }

        return Util.string.format(mainTpl, key, link, style);
    }
}
