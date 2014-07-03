var appId = -1;
var envId = -1;
var version = "";
getSession();

// 提交
$("#item_submit").on("click", function(e) {

	var key = $("#key").val();
	var value = $("#value").val();
	
	// 验证
	if ( appId < 1 || envId < 1 || version=="" || !value || !key) {
		$("#error").show();
		$("#error").html("表单不能为空或填写格式错误！");
		return;
	}
	$.ajax({
		type : "POST",
		url : "/api/config/item",
		data : {
			"appId":appId,
			"version":version,
			"key":key,
			"envId":envId,
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

// 提交
$("#file_submit").on("click", function(e) {

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