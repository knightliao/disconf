(function($) {

	var configId = Util.param.getConfigId();

	//
	// 获取APP信息
	//
	$.ajax({
		type : "GET",
		url : "/api/config/" + configId
	}).done(function(data) {
		if (data.success === "true") {
			var result = data.result;
			$("#app").text(result.appName);
			$("#version").text(result.version);
			$("#env").text(result.envName);
			$("#key").text(result.key);
			$("#value").val(result.value);
		}
	});

})(jQuery);
