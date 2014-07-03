(function($) {

	getSession();

	var appId = -1;
	var envId = -1;
	var version = "所有版本";

	//
	// 获取APP信息
	//
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
					html += '<li><a rel=' + -1 + ' href="#">所有APP</a></li>';
					$("#appChoice").html(html);
				}
			});
	$("#appChoice").on('click', 'li a', function() {
		$("#appChoiceA span:first-child").text($(this).text());
		appId = $(this).attr('rel');
		fetchVersion(appId);
		fetchMainList();
	});

	//
	// 获取版本信息
	//
	function fetchVersion(appId) {

		$("#versionChoiceA span:first-child").text("选择版本");
		version = "所有版本";

		$.ajax({
			type : "GET",
			url : "/api/config/versionlist?appId=" + appId
		}).done(function(data) {
			if (data.success === "true") {
				var html = "";
				var result = data.page.result;
				$.each(result, function(index, item) {
					html += '<li><a href="#">' + item + '</a></li>';
				});
				html += '<li><a href="#">' + "所有版本" + '</a></li>';
				$("#versionChoice").html(html);
			}
		});
		$("#versionChoice").on('click', 'li a', function() {
			$("#versionChoiceA span:first-child").text($(this).text());
			version = $(this).text();
			fetchMainList();
		});
	}

	//
	// 获取Env信息
	//
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
					html += '<li><a rel=' + -1 + ' href="#">所有环境</a></li>';
					$("#envChoice").html(html);
				}
			});
	$("#envChoice").on('click', 'li a', function() {
		$("#envChoiceA span:first-child").text($(this).text());
		envId = $(this).attr('rel');
		fetchMainList();
	});

	fetchMainList();

	//
	// 渲染主列表
	//
	function fetchMainList() {

		var parameter = ""

		url = "/api/config/list";
		if (appId == null && envId == null && version == null) {

		} else {
			url += "?";
			if (appId != -1) {
				url += "appId=" + appId + "&";
			}
			if (envId != -1) {
				url += "envId=" + envId + "&";
			}
			if (version != "所有版本") {
				url += "version=" + version + "&";
			}
		}

		$.ajax({
			type : "GET",
			url : url
		}).done(function(data) {
			if (data.success === "true") {
				var html = "";
				var result = data.page.result;
				$.each(result, function(index, item) {
					html += renderItem(item);
				});
				$("#accountBody").html(html);
			}
			bindDetailEvent(result);
		});
		var mainTpl = $("#tbodyTpl").html();
		// 渲染主列表
		function renderItem(item) {

			var link = "";
			del_link = '<a id="itemDel' + item.configId
					+ '" style="cursor: pointer; cursor: hand; " >删除</a>';
			if (item.type == "配置文件") {
				link = '<a href="modifyFile.html?configId=' + item.configId
						+ '">修改</a>';
			} else {
				link = '<a href="modifyItem.html?configId=' + item.configId
						+ '">修改</a>';
			}

			return Util.string.format(mainTpl, item.appName, item.appId,
					item.version, item.envId, item.envName, item.type,
					item.key, item.createTime, item.modifyTime, item.value,
					link, del_link);
		}
	}

	// 详细列表绑定事件
	function bindDetailEvent(result) {
		$.each(result, function(index, item) {
			var id = item.configId;
			// 绑定删除事件
			$("#itemDel" + id).on("click", function(e) {
				deleteDetailTable(id);
			});
		});
	}

	// 删除
	function deleteDetailTable(id) {

		var ret = confirm("你确定要删除吗?");
		if (ret == false) {
			return false;
		}

		$.ajax({
			type : "DELETE",
			url : "/api/config/" + id
		}).done(function(data) {
			if (data.success === "true") {
				fetchMainList();
			}
		});
	}

})(jQuery);
