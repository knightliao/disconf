var appId = -1;
var envId = -1;
var version = "";
getSession();

// 提交
$("#item_submit").on("click", function(e) {

	var app = $("#app").val();
	var desc = $("#desc").val();

	// 验证
	if (!desc || !app) {
		$("#error").show();
		$("#error").html("表单不能为空或填写格式错误！");
		return;
	}
	$.ajax({
		type : "POST",
		url : "/api/app",
		data : {
			"app" : app,
			"desc" : desc,
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
