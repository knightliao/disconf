getSession();

var configId = Util.param.getConfigId();

fetchFileData();

//
// 渲染主列表
//
function fetchFileData() {
    //
    // 获取APP信息
    //
    $.ajax({
        type: "GET",
        url: "/api/web/config/" + configId
    }).done(
        function (data) {
            if (data.success === "true") {
                var result = data.result;
                $("#app").text(
                        result.appName + ' (appid=' + result.appId + ')');
                $("#version").text(result.version);
                $("#env").text(result.envName);
                $("#key").text(result.key);
                $("#oldvalue").text(result.value);
                $("#currentData").text(
                        result.appName + " * " + result.version + " * "
                        + result.envName);
                // 获取APP下的配置数据
                fetchItems(result.appId, result.envId, result.version,
                    configId);
            }
        });
}

var upload_status = 0;
//
// 校验
//
function validate(formData, jqForm, options) {

    var myfilerar = $('input[name=myfilerar]')

    var val = myfilerar.val();
    if (validate_file_name(val)) {
        return true;
    }

    return false;
}

//
// 校验文件名
//
function validate_file_name(fileName) {

    switch (fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase()) {
        case 'properties':
            break;
        case 'xml':
            break;
        default:
            //errorrar.html("错误: " + "文件类型必须是properties/.xml");
            //return false;
            return true;
    }

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

$('#myfilerar').change(function (evt) {

    //
    // 清空错误及显示
    //
    errorrar.empty();
    $("#error").empty();

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
var options = {

    url: '/api/web/config/file/' + configId,
    beforeSubmit: validate,
    beforeSend: function (xhr) {

        $("#myfilerar").bind("updatecomplete", function () {
            xhr.abort();
        });
        var percentVal = '0%';
        progress_rar.show();
        bar.width(percentVal)
        percent.html(percentVal);
        errorrar.html("&nbsp;&nbsp;正在上传....请稍候...");
    },
    uploadProgress: function (event, position, total, percentComplete) {
        var percentVal = percentComplete + '%';
        upload_status = 1;
        bar.width(percentVal)
        percent.html(percentVal);
        errorrar.html("&nbsp;&nbsp;正在上传....请稍候...");
    },
    success: function () {
        var percentVal = '100%';
        bar.width(percentVal)
        percent.html(percentVal);
        progress_rar.hide();
    },
    complete: function (xhr) {

        $("#error").show();
        errorrar.show();

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
            upload_status = 2;
            // 重新刷新
            fetchFileData();
        } else {
            upload_status = 3;
            bar.width(0)
            percent.html(0);
        }
    }
};

var uploadWithFile = -1;

//
//
//
$("#file_submit").unbind('click').on('click', function (e) {
    if (uploadWithFile == -1) {
        $("#error").show();
        $("#error").html("请选择上传方式");
        return false;
    }
});

//
// 上传方式
//
$("#uploadChoice").on('click', 'li a', function () {

    $("#uploadChoiceA span:first-child").text($(this).text());

    if ($(this).text() == "上传配置文件") {

        $("#file_upload_choice").show().children().show();
        $("#text_upload_choice").hide();
        uploadWithFile = 1;

    } else if ($(this).text() == "输入文本") {

        $("#file_upload_choice").hide();
        $("#text_upload_choice").show().children().show();
        $("#fileContent").text($("#oldvalue").text());
        uploadWithFile = 0;
    }

    //
    // 清除错误信息
    //
    $("#error").html("");
    errorrar.html("");

    //
    // 事件绑定
    //
    if (uploadWithFile) {

        $("#file_submit").unbind('click').on('click', function (e) {

            // 提交
            $('#form').ajaxSubmit(options);
        });

    } else {

        $("#file_submit").unbind('click').on('click', function (e) {

            var fileContent = $("#fileContent").val();

            $.ajax({
                type: "PUT",
                url: "/api/web/config/filetext/" + configId,
                data: {
                    "fileContent": fileContent
                }

            }).done(function (data) {

                $("#error").show();
                errorrar.show();

                if (data.success === "true") {
                    $("#error").html(data.result);
                } else {
                    Util.input.whiteError($("#error"), data);
                }
                // 重新刷新
                fetchFileData();
            });
        });

    }
});
