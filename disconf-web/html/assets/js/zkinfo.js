(function ($) {

    getSession();

    fetchConfigUsage();

    //
    // 渲染主列表
    //
    function fetchConfigUsage() {

        url = "/api/usage/list";

        $.ajax({
            type: "GET",
            url: url
        }).done(function (data) {
            if (data.success === "true") {
                var html = data.result.hostInfo;
                $("#hostInfo").html(html);
            }
        });

    }

})(jQuery);
