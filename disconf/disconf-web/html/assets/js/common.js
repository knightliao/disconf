// 初始入口
(function() {
	window.VISITOR = {};
})();

// 获取Session信息
function getSession() {
	$.ajax({
		type : "GET",
		url : "/api/account/session"
	}).done(function(data) {
		if (data.success === "true") {
			window.VISITOR = data.result.visitor;
		} else {
			window.location.href = "/about.html";
		}
	});
}
