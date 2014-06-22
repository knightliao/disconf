(function($) {

	$.ajax({
		type : "GET",
		url : "/api/app/list"
	}).done(
			function(data) {
				if (data.success === "true") {
					var html = "";
					var result = data.page.result;
					$.each(result, function(index, item) {
						html += '<li><a rel=' + item.id + ' href="#">APP: '
								+ item.name + '</a></li>';
					});
					$("#appChoice").html(html);
				}
			});
	$("#appChoice").on('click', 'li a', function() {
		$("#appChoiceA span:first-child").text($(this).text());
		fetchVersion($(this).attr('rel'));
	});

	// 渲染主列表
	function fetchVersion(appId) {
		
		$("#versionChoiceA span:first-child").text("选择版本");
		
		$.ajax({
			type : "GET",
			url : "/api/config/versionlist?appId=" + appId
		}).done(function(data) {
			if (data.success === "true") {
				var html = "";
				var result = data.page.result;
				$.each(result, function(index, item) {
					html += '<li><a href="#">版本: ' + item + '</a></li>';
				});
				$("#versionChoice").html(html);
			}
		});
		$("#versionChoice").on('click', 'li a', function() {
			$("#versionChoiceA span:first-child").text($(this).text());
		});
	}

	$.ajax({
		type : "GET",
		url : "/api/env/list"
	}).done(
			function(data) {
				if (data.success === "true") {
					var html = "";
					var result = data.page.result;
					$.each(result, function(index, item) {
						html += '<li><a rel=' + item.id + ' href="#">环境:'
								+ item.name + '</a></li>';
					});
					$("#envChoice").html(html);
				}
			});
	$("#envChoice").on('click', 'li a', function() {
		$("#envChoiceA span:first-child").text($(this).text());
	});

})(jQuery);
