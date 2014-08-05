// 初始入口
(function() {
	window.VISITOR = {};
})();

// 头部显示初始化
function headShowInit() {
	if (VISITOR.id) {
		$(".login-no").hide();
		$(".login-yes").show();
		$("#username").show();
		$("#username").html(VISITOR.name);
	} else {
		$(".login-no").show();
		$(".login-yes").hide();
		$("#username").hide();
	}
}

// 获取Session信息
function getSession() {
	$.ajax({
		type : "GET",
		url : "/api/account/session"
	}).done(function(data) {
		if (data.success === "true") {
			window.VISITOR = data.result.visitor;
			headShowInit();
		} else {
			window.location.href = "/login.html";
		}
	});
}

// 获取是否登录并且进行跳转
function getSession2Redirect() {
	$.ajax({
		type : "GET",
		url : "/api/account/session"
	}).done(function(data) {
		if (data.success === "true") {
			window.location.href = "/main.html";
		} else {
		}
	});
}
