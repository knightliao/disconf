(function($) {
    var mainTpl = $("#tbodyTpl").html();
    
        
    $.ajax({
        type: "GET",
        url: "/api/account/noactivation/list"
    })
    .done(function( data ) {
       if (data.success === "true") {
            var html = "";
            var result = data.page.result;
            $.each(result, function(index, item) {
                html += renderItem(item);
            });
            $("#accountBody").html(html);
            bindEvent(result);
        }
    });
    
    //渲染主列表
    function renderItem(item) {
        return Util.string.format(
            mainTpl,
            item.id,
            item.companyName,
            item.companyType,
            item.email,
            item.phone,
            item.city,
            item.applyinfo,
            item.registerTimeString
        );
    }
    function bindEvent(result) {
        $.each(result, function(index, item) {
            var id = item.id;
            var $activate =  $("#activate" + id);
            //初始化 显示未激活
            if (item.state) {
                 $activate.html('已激活');
                 $activate.addClass("green");
                 return;
            }
            $activate.addClass("blue");
            //激活
            $activate.on("click", function(e) {
                var $me = $(this);
                if ($me.attr("success")) return; 
                $.ajax({
                    type: "GET",
                    url: "/api/auth/actemail/" + id
                })
                .done(function(data) {
                    if (data.success === "true") {
                        $me.html('已发送激活邮件');
                        $me.removeClass("blue");
                        $me.addClass("green");
                        $me.attr("success", "true");
                    }
                    else {
                        $me.html("激活失败");
                        $me.removeClass("blue");
                        $me.addClass("red");
                    }
                });
            });
            
        });
    }
    
    
})(jQuery);
