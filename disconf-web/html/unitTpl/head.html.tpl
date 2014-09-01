    <link rel="stylesheet" type="text/css" href="dep/bootstrap/css/bootstrap.css" />
    <link rel="stylesheet" href="dep/jquery-ui-1.10.4.custom/css/ui-lightness/jquery-ui-1.10.4.custom.css" />
    <link rel="stylesheet" type="text/css" href="assets/css/project.css" />
</head>

<body>

    <div id="top_nav_bar" class="navbar navbar-fixed-top" role="navigation">
        <div class="container-fluid">
			<div class="navbar-header">
				<a id="indexMain" href="/main.html" class="navbar-brand" style="margin-left:0px;"><span class="zu-top-nav-link">Disconf</span></a>
			</div>
			<div class="navbar-collapse collapse">
				<ul class="login-yes nav navbar-nav navbar-right" style="display:none;">
                    <li>
                         <a href="#"><span class="zu-top-nav-link loginName" id="username"></span></a>
                    </li>
                    <li>
                         <a href="#" id="signout">
                            <span class="zu-top-nav-link" >退出</span>
                         </a>
                    </li>
               </ul>
               <ul class="login-no nav navbar-nav navbar-right">
                    <li>
                         <a href="/login.html"><span class="zu-top-nav-link">登录</span></a>
                    </li>
				</ul>
				<ul class="navbar-form navbar-right">
					<li>
						<button class="btn btn-warning" title="GitHub" type="button" onclick="window.open('https://github.com/knightliao/disconf', '_blank');">
	                    	<span class="glyphicon glyphicon-link icon-white"></span> <b>GitHub</b>
	                	</button>
                	</li>
                </ul>
                <ul id="main_menu" class="nav navbar-nav navbar-right">
                	<li class="zu-top-nav-link">
                    	<a href="/main.html" ><span class="zu-top-nav-link">配置</span></a>
                	</li>
                	<li class="zu-top-nav-link">
                    	<a href="/configUsage.html" ><span class="zu-top-nav-link">部署</span></a>
                	</li>
                	<li class="dropdown" class="zu-top-nav-link">
		            	<a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown"><span class="zu-top-nav-link">新建<b class="caret"></b></span></a>
		            	<ul class="dropdown-menu">
		              		<li><a href="/newapp.html">新建APP</a></li>
		                	<li><a href="/newconfig_item.html">新建配置项</a></li>
		                	<li><a href="/newconfig_file.html">新建配置文件</a></li>
		                </ul>
		            </li>
					<li class="zu-top-nav-link">
						<a href="https://github.com/knightliao/disconf" target="_blank"><span class="zu-top-nav-link">帮助</span></a>
                	</li>
                </ul>
                
			</div>
		</div>
    </div>
    
    
    
    
    
    