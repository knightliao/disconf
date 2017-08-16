
//
// 获取role信息
//
$.ajax({
    type: "GET",
    url: "/api/role/list"
}).done(
    function (data) {
        if (data.success === "true") {
            var html = "";
            var result = data.page.result;
            $.each(result, function (index, item) {
                html += '<li><a rel=' + item.id + ' href="#">'
                    + item.roleName + '</a></li>';
            });
            $("#roleChoice").html(html);
        }
    });
$("#roleChoice").on('click', 'li a', function () {
    $("#roleChoiceA span:first-child").text($(this).text());
    roleId = $(this).attr('rel');
});