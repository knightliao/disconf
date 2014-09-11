(function($) {

	getSession();

	var appId = -1;
	var envId = -1;
	var version = "#";

	//
	// 获取APP信息
	//
	$
			.ajax({
				type : "GET",
				url : "/api/app/list"
			})
			.done(
					function(data) {
						if (data.success === "true") {
							var html = "";
							var result = data.page.result;
							$
									.each(
											result,
											function(index, item) {
												html += '<li role="presentation" role="menuitem" tabindex="-1"><a rel='
														+ item.id
														+ ' href="#">APP: '
														+ item.name
														+ '</a></li>';
											});
							$("#applist").html(html);
						}
					});
	$("#applist").on('click', 'li a', function(e) {
		appId = $(this).attr('rel');
		$("#appDropdownMenuTitle").text($(this).text());
		version = "#";
		fetchMainList();
	});

	//
	// 获取版本信息
	//
	function fetchVersion(appId, envId) {

		var base_url = "/api/config/versionlist?appId=" + appId;
		if (envId != -1) {
			url = base_url + "&envId=" + envId;
		}

		$.ajax({
			type : "GET",
			url : url
		}).done(function(data) {
			if (data.success === "true") {
				var html = "";
				var result = data.page.result;
				$.each(result, function(index, item) {
					html += '<li><a href="#">' + item + '</a></li>';
				});
				$("#versionChoice").html(html);

				if (html != "") {
					$("#versionChoice li:first").addClass("active");
					version = $("#versionChoice li:first a").text();
				}
			}
		});
		$("#versionChoice").unbind('click').on('click', 'li a', function(e) {
			version = $(this).text();
			$("#versionChoice li").removeClass("active");
			$(this).parent().addClass("active");
			fetchMainList();
			e.stopPropagation();
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
						html += '<li><a rel=' + item.id + ' href="#">'
								+ item.name + ' 环境</a></li>';
					});
					$("#envChoice").html(html);
				}
			});
	$("#envChoice").on('click', 'li a', function() {
		envId = $(this).attr('rel');
		$("#envChoice li").removeClass("active");
		$(this).parent().addClass("active");
		version = "#";
		fetchMainList();
	});

	fetchMainList();

	//
	// 渲染主列表
	//
	function fetchMainList() {

		// 参数不正确，清空列表
		if (appId == -1 || envId == -1) {
			$("#mainlist_error").text("请选择" + getTips()).show();
			$("#accountBody").html("");
			$("#mainlist").hide();
			$("#zk_deploy").hide();
			return;
		}

		if (version == "#") {
			fetchVersion(appId, envId);
		}

		$("#zk_deploy").show().children().show();

		$("#mainlist_error").hide();
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
			if (version != "#") {
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
					html += renderItem(item, index);
				});
				if (html != "") {
					$("#mainlist").show();
					$("#accountBody").html(html);
				} else {
					$("#accountBody").html("");
				}

			} else {
				$("#accountBody").html("");
				$("#mainlist").hide();
			}

			bindDetailEvent(result);

			// ZK绑定情况
			fetchZkDeploy();
		});
		var mainTpl = $("#tbodyTpl").html();
		// 渲染主列表
		function renderItem(item, i) {

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
					link, del_link, i + 1);
		}
	}

	// 详细列表绑定事件
	function bindDetailEvent(result) {
		if (result == null) {
			return;
		}
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

	//
	function getTips() {
		if (appId == -1) {
			return "APP";
		}
		if (envId == -1) {
			return "环境";
		}
		return "参数";
	}

	//
	function fetchZkDeploy() {
		if ($("#zk_deploy_info").is(':hidden')) {
			var cc = '';
		} else {
			fetchZkDeployInfo();
		}
	}

	//
	// 获取ZK数据信息
	//
	function fetchZkDeployInfo() {

		$("#zk_deploy_info_pre").html("正在获取ZK信息，请稍等......");

		// 参数不正确，清空列表
		if (appId == -1 || envId == -1 || version == "#") {
			$("#zk_deploy_info_pre").html("无ZK信息");
			return;
		}

		var base_url = "/api/zoo/zkdeploy?appId=" + appId + "&envId=" + envId
				+ "&version=" + version

		$.ajax({
			type : "GET",
			url : base_url
		}).done(function(data) {
			if (data.success === "true") {
				var html = data.result.hostInfo;
				if (html == "") {
					$("#zk_deploy_info_pre").html("无ZK信息");
				} else {
					$("#zk_deploy_info_pre").html(html);
				}
			}
		});
	}

	$("#zk_deploy_button").on('click', function() {
		$("#zk_deploy_info").toggle();
		fetchZkDeploy();
	});

})(jQuery);
