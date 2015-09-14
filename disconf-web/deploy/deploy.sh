#!/bin/bash

#
# 一个简单的 WAR&&静态代码 部署脚本, 执行时必须在disconf-web目录下执行本脚本
#

#
# 执行前请确定环境变量里存在以下两个变量
#	1. $ONLINE_CONFIG_PATH 	: java web 线上配置目录
#	2. $WAR_ROOT_PATH 		: java web war 包
#

# 脚本执行后，将生成从下文件：
#	$WAR_ROOT_PATH/						项目根目录(请将Tomcat指向此目录)
#	$WAR_ROOT_PATH/disconf-web.war  	生成的War包
#	$WAR_ROOT_PATH/html  				HTML前端代码(请将Nginx指向此目录)
#	$WAR_ROOT_PATH/META-INF  			
#	$WAR_ROOT_PATH/WEB-INF				Java Classes
#		



#
# 线上配置的路径  ONLINE_CONFIG_PATH=/home/work/dsp/disconf-rd/online-resources
# 需要预先设置在环境变量里
if [ "$ONLINE_CONFIG_PATH" = "" ]; then	
	echo "ONLINE_CONFIG_PATH is null, please set it in your env."
	exit 1
fi

# 
# WAR要放的路径   WAR_ROOT_PATH=/home/work/dsp/disconf-rd/war
# 需要预先设置在环境变量里
if [ "$WAR_ROOT_PATH" = "" ]; then	
	echo "ONLINE_CONFIG_PATH is null, please set it in your env."
	exit 1
fi

set -e

export PATH

echo "**********************************************"
echo "copy online config " $ONLINE_CONFIG_PATH
echo "**********************************************"
if [ -d "src/main/online-resources" ]; then
    printf '%s\n' "Removing src/main/online-resources/*"
    rm -rf src/main/online-resources/*
fi
mkdir -p src/main/online-resources
cp -rp "$ONLINE_CONFIG_PATH"/* src/main/online-resources 

echo "**********************************************"
echo "It's going to Generate the output for war"
echo "**********************************************"

current_path=`pwd`

#
# 进行WAR打包
#
echo "**********************************************"
echo "It's going to got war package"
echo "**********************************************"

sh deploy/build_java.sh


#
# 进行FE打包
#
echo "**********************************************"
echo "It's going to got fe package"
echo "**********************************************"

cd html
python build.py

#
cd ${current_path}


#
# 清空原始目录 
#
mkdir -p ${WAR_ROOT_PATH}
if [ ${#WAR_ROOT_PATH} -gt 15 ]; then
	echo "rm " $WAR_ROOT_PATH
	rm -rf "$WAR_ROOT_PATH"
    mkdir -p ${WAR_ROOT_PATH}
fi	


#
#
#
echo "start to copy war"
cp -rp output/disconf-web.war  $WAR_ROOT_PATH  

#
#
#
echo "start to copy static"
mkdir ${WAR_ROOT_PATH}/html
cp -rp html/output/* $WAR_ROOT_PATH/html

#
#
#

cd ${WAR_ROOT_PATH}

echo "start to jar war"
jar xvf disconf-web.war

cd ${current_path}

echo "deploy done" $WAR_ROOT_PATH

