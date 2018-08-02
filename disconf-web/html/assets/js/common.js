// 初始入口
(function () {
    window.VISITOR = {};
})();

//
// 头部显示初始化
//
function headShowInit() {
    if (VISITOR.id) {
        $(".login-no").hide();
        $(".login-yes").show();
        $("#username").show();
        $("#username").html(VISITOR.name);
        if (VISITOR.role == "管理员" || VISITOR.role == "admin"){
            $("#userRoleManage").css('display','inline');
        }
    } else {
        $(".login-no").show();
        $(".login-yes").hide();
        $("#username").hide();
    }
}

//
// 登录其它的控制
//
function loginActions() {
    if (VISITOR.id) {
        $("#brand_url").attr("href", "/main.html");
    } else {
        $("#brand_url").attr("href", "/");
    }
}

//
// 获取Session信息
//
function getSession() {
    $.ajax({
        type: "GET",
        url: "/api/account/session",
        timeout: 3000 // 3s timeout
    }).done(function (data) {
        if (data.success === "true") {
            window.VISITOR = data.result.visitor;
            headShowInit();
        } else {
            window.location.href = "/login.html";
        }
    }).fail(function (xmlHttpRequest, textStatus) {
        window.location.href = "/login.html";
    });
}

// 获取是否登录并且进行跳转
function getSession2Redirect() {
    $.ajax({
        type: "GET",
        url: "/api/account/session"
    }).done(function (data) {
        if (data.success === "true") {
            window.location.href = "/main.html";
        } else {
        }
    });
    loginActions();
}

/**
 * [全局函数G]
 */
var G = (function(){

    "use strict";
    var layer_load = '';

    /**
     * [CSSPIE 用PIE js解决ie兼容问题]
     * @param selector {[string]} JQuery选择器
     */
    var CSSPIE = function ( selector ){
        if (window.PIE) {
            $(selector).each(function() {
                PIE.attach(this);
            });
        }
    }

    var ajax = function( obj ){
        if(!obj.url || obj.url == "" ) return false;
        var data = obj.data || {};
        var url = obj.url;
        $.ajax({
            url : url,
            type :'POST',
            data : data,
            async : false,
            dataType : 'JSON',
            beforeSend: function(){
                layer_load = layer.load(2, {time: 2000});
            },
            success : function(data){
                layer.close(layer_load);
                if(data.code == 2000){
                    if(obj.callback && typeof obj.callback == 'function') obj.callback( data.data , data );
                }else{
                    alert(data.message)
                    if(obj.error && typeof obj.error == "function") obj.error();
                }
            },error : function(){
                alert("网络错误！");
                if(obj.error && typeof obj.error == "function") obj.error();
            }
        })

    }


    var deleteEven = function( selector , url , page ){
        $( selector ).on("click", ".deleteConfirm" ,function(){
            var id = $(this).data("id");
            var r = confirm("确定要删除吗？");
            if(r){
                G.ajax({
                    url : url,
                    data :  { id : id },
                    callback : function( json ){
                        if(typeof page == 'object') page.reloadPage();
                    }
                })
            }
        })

    }


    /**
     * [addJSON JSON操作监听-->中间件]
     * ues setData(Json,opt,val)
     * 监听写法 Json.onAdd = function(){}
     */
    var addJSON = function(Json,opt,val){//仿JQuery get set
        if(Object.prototype.toString.call(opt) == '[object Object]'){
            //当传入为对象则为多个内容设置方法
            for(var i in opt){
                Json[i] = opt[i];
                if(typeof Json.onAdd == 'function') Json.onAdd.call(this,i)
            }
        }else{
            //否则为get或set
            if(val){//set
                Json[opt] = val;
                if(typeof Json.onAdd == 'function') Json.onAdd.call(this,opt);
            }else{//get
                return Json[opt] || undefined;
            }
        }
    }

    /**
     * @author  ZHU
     * 页码树用法
     var page = new G.pageTree();
     page.init("#page");
     page.setCallback(function(page){
			//DoSomething
			page.callPage({
				total 		: 300,
				pageSize 	: 15,
				onPage 		: 1
			});
		});
     */
    var pageTree = function(){

        //存放内部数据
        var pData = {
            pBody 			: '<ul class="page-list"><ul>',//页码盒子,初始化前为html。后为jquery对象
            totalNum 		: 0,//总条数
            onPage 			: 1,//当前页数
            totalPage 		: 1,//总页数
        }

        /**
         * [buildPage 构建页码]
         * @param  {[int]} hover [当前页数]
         * @param  {[int]} max   [最大页数]
         * @return {[string]}       [页码数据]
         */
        var buildPage = function(hover,max){
            var htmls = '';

            var jumpTo = '<li class="jump-to">跳到</li>'
                + '<li class="point"><input type="text" class="page-input"></li>'
                + '<li>页</li>'
                + '<li class="go">GO</li>';
            htmls += '<li class="total_num">共'+ pData.totalNum +'条数据</li>'

            if(hover > 1 && max != 1){//左按钮
                htmls += '<li class="turnPage prev">上一页</li>';
            }

            if(max < 10){
                for(var i = 1; i <= max; i++){
                    if(hover == i){
                        htmls += '<li class="pages hover">'+ i +'</li>';
                    }else{
                        htmls += '<li class="pages">'+ i +'</li>';
                    }
                }
            }else{
                if(hover > 4 && hover < (max-3)){
                    htmls += '<li class="pages">'+ 1 +'</li>';
                    htmls += '<li class="point">…</li>';
                    for(var i = hover-1 ; i <= hover+1 ; i++){
                        if(hover == i){
                            htmls += '<li class="pages hover">'+ i +'</li>';
                        }else{
                            htmls += '<li class="pages">'+ i +'</li>';
                        }
                    }
                    htmls += '<li class="point">…</li>';
                    htmls += '<li class="pages">'+ max +'</li>';
                }else if(hover < 5){
                    for(var i = 1 ; i <= 5 ; i++){
                        if(hover == i){
                            htmls += '<li class="pages hover">'+ i +'</li>';
                        }else{
                            htmls += '<li class="pages">'+ i +'</li>';
                        }
                    }
                    htmls += '<li class="point">…</li>';
                    htmls += '<li class="pages">'+ max +'</li>';
                }else{
                    htmls += '<li class="pages">'+ 1 +'</li>';
                    htmls += '<li class="point">…</li>';
                    for(var i = max-4 ; i <= max ; i++){
                        if(hover == i){
                            htmls += '<li class="pages hover">'+ i +'</li>';
                        }else{
                            htmls += '<li class="pages">'+ i +'</li>';
                        }
                    }
                }
            }
            if(hover < max && max != 1){//右按钮
                htmls += '<li class="turnPage next">下一页</li>'
            }
            return htmls + jumpTo;
        }

        //事件绑定
        var evenBind = function(){
            //直接点击页码
            pData.pBody.on("click",".pages",function(){
                var pageNum = $(this).text();
                if(!/^\d+$/.test(pageNum) || pageNum > pData.totalPage){
                    alert("错误操作!");
                }else{
                    addJSON(pData,"onPage",pageNum);
                }
            })
            //点击上下页按钮
            pData.pBody.on("click",".turnPage",function(){
                if($(this).hasClass("prev")){
                    addJSON(pData,"onPage",parseInt(pData.onPage) - 1);
                }else if($(this).hasClass("next")){
                    addJSON(pData,"onPage",parseInt(pData.onPage) + 1);
                };
            })
            //输入页码事件
            pData.pBody.on("click",".go",function(){
                var pageNum = $(".page-input").val();
                if(!/^\d+$/.test(pageNum)){
                    alert("请输入正确数字!");
                }else if(pageNum >  pData.totalPage){
                    alert("输入页码过大，请重新输入！");
                }else{
                    addJSON(pData,"onPage",pageNum);
                }
            })

        }

        //切换事件
        var turnEven = function(opt){
            if(opt == "onPage"){
                var data = evenFunction(pData.onPage);
            }
        }

        //测试包
        var evenFunction = function(pages){
            //doSomething
            this.callPage({
                total 		: 300,
                pageSize 	: 15,
                onPage 		: pages
            })
        };

        /****************************接口部分**********************************/

        /**
         * [init 初始化方法]
         * @param  {[String]} selector [JQuery选择器]
         */
        this.init = function( selector ){
            pData.pBody = $(selector).addClass("page-box").html(pData.pBody).hide();
            evenBind();
            pData.onAdd = turnEven;
        }

        /**
         * [setCallback 设置点击翻页后的callback事件]
         * @param  {[Function]} func     [callback方法]
         */
        this.setCallback = function(func){
            if(typeof func == 'function') evenFunction = func;//调试模式使用模拟数据
            //turnEven("onPage");
        }

        this.callPage = function( data ){
            if(!data) return false;
            pData.totalNum = data.total
            pData.onPage = data.onPage
            pData.totalPage = Math.ceil(data.total/data.pageSize);
            if(pData.totalPage >= 2){
                var pages = buildPage(parseInt(data.onPage),parseInt(pData.totalPage));
                pData.pBody.children("ul").html(pages);
                pData.pBody.show();
            }else{
                pData.pBody.hide();
            }
        }

        this.reloadPage = function(){
            turnEven("onPage");
        }

        this.changePage = function(page) {
            pData.onPage = page;
        }

    }//pageTree end


    return {
        CSSPIE 			: CSSPIE
        ,pageTree 		: pageTree
        ,ajax 			: ajax
        ,deleteEven 	: deleteEven
        ,addJSON 		: addJSON
    }


})()