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
    
})(jQuery);
