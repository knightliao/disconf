var appId = -1;
var envId = -1;
var version = "";
getSession();

var upload_status = 0;
//
// 校验
//
function validate(formData, jqForm, options) {

	var myfilerar = $('input[name=myfilerar]')

	var val = myfilerar.val();
	switch (val.substring(val.lastIndexOf('.') + 1).toLowerCase()) {
	case 'properties':
		break;
	default:
		errorrar.html("错误: " + "文件类型必须是properties");
		return false;
	}

	return true;
}

function validate2(formData, jqForm, options) {

	validate(formData, jqForm, options);

	if (version == '自定义版本') {
		version = $('#selfversion_value').val();
	}

	// 验证
	if (appId < 1 || envId < 1 || version == "") {
		$("#error").show();
		$("#error").html("表单不能为空或填写格式错误！");
		return false;
	}

	formData.push({
		name : 'appId',
		value : appId
	});
	formData.push({
		name : 'version',
		value : version
	});
	formData.push({
		name : 'envId',
		value : envId
	});

	return true;
}

//
// 上传按钮
//
var bar = $('.bar_rar');
var percent = $('.percent_rar');
var progress_rar = $('.progress_rar');
var myfilerar = $('#myfilerar');
var errorrar = $('#error_rar');
var add_file_but = $('#open_dialog_rar')
$('#myfilerar').change(function(evt) {
	errorrar.empty();
	upload_status = 0;
	add_file_but.html("上传配置文件...")
	var ret = validate(null, null, null);
	if (ret == true) {
		upload_status = 1;
		var myfilerar = $('input[name=myfilerar]')
		var val = myfilerar.val();
		errorrar.html("&nbsp;&nbsp;准备上传: " + val);
	}
});

// 提交
$('#form').ajaxForm({

	url : '/api/config/file',
	beforeSubmit : validate2,
	beforeSend : function(xhr) {
		$("#myfilerar").bind("updatecomplete", function() {
			xhr.abort();
		});
		$("#error").html("");
		var percentVal = '0%';
		progress_rar.show();
		bar.width(percentVal)
		percent.html(percentVal);
		errorrar.html("&nbsp;&nbsp;正在上传....请稍候...");
	},
	uploadProgress : function(event, position, total, percentComplete) {
		var percentVal = percentComplete + '%';
		upload_status = 1;
		bar.width(percentVal)
		percent.html(percentVal);
		errorrar.html("&nbsp;&nbsp;正在上传....请稍候...");
	},
	success : function() {
		var percentVal = '100%';
		bar.width(percentVal)
		percent.html(percentVal);
		progress_rar.hide();
	},
	complete : function(xhr) {
		var is_ok = true;
		if (xhr.status != 200 && xhr.status != 0) {
			errorrar.html("上传失败，请重新上传. 状态码：" + xhr.status);
			is_ok = false;
		} else if (xhr.aborted == 1) {
			is_ok = false;
		} else if (xhr.statusText == "abort") {
			is_ok = false;
		} else {
			xhr.responseText = xhr.responseText.replace("<PRE>", "");
			xhr.responseText = xhr.responseText.replace("</PRE>", "");
			data = $.parseJSON(xhr.responseText);
			if (data.success === "true") {
				$("#error").html(data.result);
			} else {
				add_file_but.html("添加资源文件...")
				Util.input.whiteError(errorrar, data);
				is_ok = false;
			}
		}
		if (is_ok == true) {
			add_file_but.html("重新上传")
			errorrar.html("&nbsp;&nbsp;上传成功！");
			status.html("已上传:" + data.file_name);
			upload_status = 2;
		} else {
			upload_status = 3;
			bar.width(0)
			percent.html(0);
		}
	}
});