(function ($) {

    // 登出
    $("#signout").on("click", function () {
        $.ajax({
            type: "GET",
            url: "/api/account/signout"
        }).done(function (data) {
            if (data.success === "true") {
                VISITOR = {};
                getSession();
            }
        });
    });

})(jQuery);