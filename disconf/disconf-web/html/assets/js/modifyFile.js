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
			$("#app").text(result.appName + ' (appid=' + result.appId + ')');
			$("#version").text(result.version);
			$("#env").text(result.envName);
			$("#key").text(result.key);
			$("#oldvalue").text(result.value);
		}
	});

	// 提交
	$("#submit").on("click", function(e) {
		var me = this;
		var value = $("#value").val();
		// 验证
		if (!value) {
			$("#error").show();
			$("#error").html("表单不能为空或填写格式错误！");
			return;
		}
		$.ajax({
			type : "PUT",
			url : "/api/config/item/" + configId,
			data : {
				"value" : value
			}
		}).done(function(data) {
			$("#error").show();
			if (data.success === "true") {
				$("#error").html(data.result);
			} else {
				Util.input.whiteError($("#error"), data);
			}
		});
	});

})(jQuery);
