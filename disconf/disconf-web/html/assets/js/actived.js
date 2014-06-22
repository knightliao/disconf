(function($) {
    var mainTpl = $("#tbodyTpl").html();
    
        
    $.ajax({
        type: "GET",
        url: "/api/auth/activation" + location.search
    })
    .done(function( data ) {
       if (data.success === "true") {
            $("#message").addClass("green");
            $("#message").html('<a href="privateInfo.html">你已激活成功，点击将跳转到个人页面！</a>');
            setTimeout(function() {
                window.location = "privateInfo.html";
            }, 3000);
        }
        else {
            $("#message").addClass("red");
            $("#message").html(data.message.field.regCode);
        }
    });
    
    
})(jQuery);
