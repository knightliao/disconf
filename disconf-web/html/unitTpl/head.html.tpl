
        <link rel="stylesheet" href="dep/bootstrap/css/bootstrap.css" />
        <!--[if lte IE 6]>
        <link rel="stylesheet" href="dep/bootstrap/css/bootstrap-ie6.css" />
        <![endif]-->
        <!--[if lte IE 7]>
        <link rel="stylesheet" href="dep/bootstrap/css/ie.css" />
        <![endif]-->
        <![if !IE]>
        <link rel="stylesheet" href="dep/bootstrap/css/noie.css" />
        <![endif]>
        <link rel="stylesheet" href="dep/jquery-ui-1.10.4.custom/css/ui-lightness/jquery-ui-1.10.4.custom.css" />
        <link rel="stylesheet" type="text/css" href="assets/css/project.css" />
    </head>

    <body>
        
        <div class="navbar navbar-fixed-top clearfix">
            <div class="navbar-inner zu-top">
                <div class="container">
                    <button type="button" class="btn btn-navbar collapsed" data-toggle="collapse" data-target=".nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <div class="nav-collapse collapse">

                        <a id="brand_url" href="/main.html" class="brand" style="margin-left:0px;padding:8px;"> <span class="zu-top-nav-link">Disconf</span> </a>

                        <span class="span2"> </span>
                        <form class="navbar-form pull-left">
                            <button class="btn btn-warning" title="GitHub" type="button" style=""  onclick="window.open('https://github.com/knightliao/disconf', '_blank');">
                                <i class="icon-circle-arrow-up  icon-white"></i> <b>GitHub</b>
                            </button>
                        </form>
                        
                        <ul class="nav pull-right" >
                            
                            <div class="login-yes"  style="display:none;padding:10px;">
                                <li style="display:inline;">
                                    <a href="#">
                                        <span class="zu-top-nav-link loginName" id="username"></span>
                                    </a>
                                </li>
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <li style="display:inline;" class="dropdown">
                                    <a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown">
                                        <span class="zu-top-nav-link">新建<b class="caret"></b></span>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li><a href="/newapp.html">新建APP</a></li>
                                        <li><a href="/newconfig_item.html">新建配置项</a></li>
                                        <li><a href="/newconfig_file.html">新建配置文件</a></li>
                                    </ul>
                                </li>
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <li style="display:inline;">
                                    <a href="/modifypassword.html">
                                        <span class="zu-top-nav-link" >修改密码</span>
                                    </a>
                                </li>
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <li style="display:inline;">
                                    <a href="#" id="signout">
                                        <span class="zu-top-nav-link" >退出</span>
                                    </a>
                                </li>
                            </div>
                            
                            <div class="login-no"  style="padding:10px;">
                                <li style="display:inline;">
                                    <a href="/login.html"><span class="zu-top-nav-link">登录</span></a>
                                </li>   
                            </div>
                            
                        </ul>
                    </div>
                </div>
                
            </div>
        </div>
    
