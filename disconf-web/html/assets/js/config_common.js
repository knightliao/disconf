//
// 获取APP信息
//
$.ajax({
    type: "GET",
    url: "/api/app/list"
}).done(
    function (data) {
        if (data.success === "true") {
            var html = "";
            var result = data.page.result;
            $.each(result, function (index, item) {
                html += '<li><a rel=' + item.id + ' href="#">APP: '
                    + item.name + '</a></li>';
            });
            $("#appChoice").html(html);
        }
    });

$("#appChoice").on('click', 'li a', function () {
    $("#appChoiceA span:first-child").text($(this).text());
    appId = $(this).attr('rel');
    fetchVersion(appId);
});

//
// 获取版本信息
//
function fetchVersion(appId) {

    $("#versionChoiceA span:first-child").text("选择版本");
    version = "";

    $.ajax({
        type: "GET",
        url: "/api/web/config/versionlist?appId=" + appId
    }).done(function (data) {
        if (data.success === "true") {
            var html = "";
            var result = data.page.result;
            $.each(result, function (index, item) {
                html += '<li><a href="#">' + item + '</a></li>';
            });
            html += '<li><a href="#">' + "自定义版本" + '</a></li>';
            $("#versionChoice").html(html);
        }
    });

    $("#versionChoice").on('click', 'li a', function () {
        $("#versionChoiceA span:first-child").text($(this).text());
        version = $(this).text();
        if (version == '自定义版本') {
            $("#selfversion").show();
        } else {
            $("#selfversion").hide();
        }
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
            var html = "";
            var result = data.page.result;
            $.each(result, function (index, item) {
                html += '<li><a rel=' + item.id + ' href="#">环境:'
                    + item.name + '</a></li>';
            });
            $("#envChoice").html(html);
        }
    });
$("#envChoice").on('click', 'li a', function () {
    $("#envChoiceA span:first-child").text($(this).text());
    envId = $(this).attr('rel');
});
