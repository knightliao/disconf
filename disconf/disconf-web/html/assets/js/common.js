(function($) {
    
    //初始入口
    (function() {
        window.VISITOR = {};
        getSession(); //获取session信息
        headlClassInit(); //导航条样式控制
        setActivitySideNav(); //设置产品定制侧边的导航
        registerInit(); //注册
        loginInit();  //登录
        loginoutInit(); //登出
    })();
 
   //头部显示初始化
   function headShowInit() {
       if (VISITOR.id) {
           $("#loginBtn").hide();
           $("#registerBtn").hide();
           $("#logoutBtn").show();
           $("#username").show();
           $("#username").html('你好！   ' + VISITOR.name);
       }
       else {
           $("#loginBtn").show();
           $("#registerBtn").show();
           $("#logoutBtn").hide();
           $("#username").hide();
       }
       //权限控制
       if (VISITOR.role === "ADMIN" || VISITOR.role === " GREP_ADMIN") {
           $("#accountListBtn").removeClass("hide");
       }
   }
    
    //获取Session信息
    function getSession() {
        $.ajax({
            type: "GET",
            url: "/api/account/session"
        })
        .done(function(data) {
            if (data.success === "true") {
               window.VISITOR = data.result.visitor;
            }
            headShowInit();   //控制用户显示
            headlClickInit(); //控制导航点击
        });
    }
    
   //导航的样式初始化控制
   function headlClassInit() {
       var page = Util.param.getPageName();
       var create = "startActivity,ticketDiscount,regForm,questionnaire,publish,autoPush";
       var navs = $("#navBlockMenu").find("a");
       $.each(navs, function(index,item) {
           $(item).removeClass("focus");
       });
       if (create.indexOf(page) !== -1) {
           $(navs[1]).addClass("focus");
       }
       else if (page === "manage" || page === "manageAcUserList") {
           $(navs[2]).addClass("focus");
       }
       else if (page === "privateInfo" || page === "accountInfo" 
               || page === "passwordEdit" || page === "memberCard") {
           $(navs[4]).addClass("focus");
       }
       else if (page === "accountList") {
           $(navs[5]).addClass("focus");
       }
       else if (page === "help") {
           $(navs[6]).addClass("focus");
       }
       else {
           $(navs[0]).addClass("focus");
       }
   }
   
   //导航条点击控制
   function headlClickInit() {
       var links = $("#navBlockMenu").find("a");
       var id = VISITOR.id;
       if (id) return;
       else {
           $.each(links, function(index, item) {
               $(item).on("click", function(e) {
                   id = VISITOR.id;
                   if (id) {
                       $(item).unbind();
                   }
                   else {
                       $("#registerDialog").dialog( "open" );
                       e.preventDefault(); 
                   }
               });
           });
           $("#create").on("click", function(e) {
               id = VISITOR.id;
               if (id) {
                   $("#create").unbind();
               }
               else {
                   $("#registerDialog").dialog( "open" );
                   e.preventDefault(); 
               }
           });
       }
   }
   
   //设置产品定制侧边的导航
   function setActivitySideNav(){
       var isUpdate = Util.param.isUpdate();
       var isCreate = Util.param.isCreate();
       var createIndex = Util.param.getCreateIndex();
       var page = Util.param.getPageName();
       var activityId = Util.param.getActivityId();
       var createArr = ["startActivity","ticketDiscount","regForm","questionnaire","publish","autoPush"];
       var createStr = createArr.join(",");
       if (createStr.indexOf(page) !== -1 /*&& isUpdate*/) {
           $("#sidebarMask").remove();
           var num;
           $.each(createArr,function(index, value) {
               if (page == value) {
                   num = index;
               }
           });
           var links = $("#sidebarMenu").find("a");
           $.each(links, function(index, link) {
               var href = $(link).attr("href");
               //不是创建时的更新
               if (isUpdate && !isCreate) {
                   href += "?type=update&activityId=" + activityId;
               }
               //创建时返回去更新的情况
               else if (isUpdate && isCreate) {
                   if (createIndex > index) {
                       href += "?type=update&activityId=" + activityId + "&createIndex=" + createIndex;
                   }
                   else if (createIndex == index) {
                       href += "?activityId=" + activityId + "&createIndex=" + createIndex;
                   } 
                   else {
                       href = "#";
                   }
               }
               //创建时
               else if (isCreate) {
                   if (num > index) {
                       href += "?type=update&activityId=" + activityId + "&createIndex=" + num;
                   } 
                   else {
                       href = "#";
                   } 
               }
               else {
                   href = "#";
               }
               $(link).attr("href", href);
           });
       }
   }
   
   //注册
   function registerInit() {
       $("#registerDialog").dialog({
            autoOpen: false,
            width: 500,
            modal: true,
            dialogClass: "alertWrap",
            buttons: [
                {
                    text: "确定",
                    click: function() {
                        reqRegister.call(this, function() {
                            $(this).dialog( "close" );
                        });
                    }
                },
                {
                    text: "取消",
                    click: function() {
                        $(this).dialog( "close" );
                    }
                }
            ]
        });
        
        $("#registerBtn").on("click", function(e) {
            $("#registerDialog").dialog( "open" );
            e.preventDefault();
        });
   }
   
   //发送注册请求
   function reqRegister(callback) {
       var me = this;
       var emailReg = /^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})$/;
       var companyName = $("#regCompanyName").val();
       var companyType = $("#regCompanyType").val();
       var phone = $("#regPhone").val();
       var email = $("#regEmail").val();
       var city = $("#regCity").val();
       var applyinfo = $("#regApplyinfo").val();
        //验证
       if (!companyName || !companyType || !phone
           || !email || !city || !applyinfo
           || !emailReg.test(email) || phone.length !== 11) {
           $("#regError").show();
           $("#regError").html("表单不能为空或填写格式错误！");
           return;
       }
       
       $.ajax({
            type: "POST",
            url: "/api/auth/register",
            data: {
                "companyName": companyName,
                "companyType": companyType,
                "phone": phone,
                "email": email,
                "city": city,
                "applyinfo": applyinfo
            }
        })
        .done(function(data) {
            if (data.success === "true") {
                $("#regError").hide();
                callback.call(me);
            }
            else {
                Util.input.whiteError($("#regError"), data);
            }
        });
   }
   
   //登录
   function loginInit() {
       $("#loginDialog").dialog({
            autoOpen: false,
            width: 430,
            modal: true,
            dialogClass: "alertWrap",
            buttons: [
                {
                    text: "确定",
                    click: function() {
                        startlogin.call(this, function() {
                            $(this).dialog( "close" );
                        });
                    }
                },
                {
                    text: "取消",
                    click: function() {
                         $("#loginError").hide();
                        $(this).dialog( "close" );
                    }
                }
            ]
        });
        
        $("#loginBtn").on("click", function(e) {
            $("#loginDialog").dialog( "open" );
            e.preventDefault();
        });
   }
   
   //发送登录请求
   function startlogin(callback) {
       var me = this;
       var phone = $("#loginPhone").val();
       var pwd = $("#loginPwd").val();
       var remember = $("#loginCheck").is("checked") ? 1 : 0;
        //验证
       if (phone.length !== 11 || !pwd) {
           $("#loginError").show();
           return;
       }
       
       $.ajax({
            type: "POST",
            url: "/api/auth/signin",
            data: {
                "phone": phone,
                "password": pwd,
                "remember": remember
            }
        })
        .done(function(data) {
            if (data.success === "true") {
                window.VISITOR = data.result.visitor;
                $("#loginError").hide();
                headShowInit();
                callback.call(me);
            }
            else {
                Util.input.whiteError($("#loginError"), data);
            }
        });
   }
   //登出
   function loginoutInit() {
       $("#logoutBtn").on("click", function () {
           $.ajax({
                type: "GET",
                url: "/api/auth/signout"
            })
            .done(function(data) {
                if (data.success === "true") {
                    VISITOR = {};
                    headShowInit();
                }
            });
       });
   }
   
})(jQuery);
