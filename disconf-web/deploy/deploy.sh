#!/bin/bash

#
# 一个简单的WAR部署脚本
#

# WAR要放的路径 
WAR_ROOT_PATH=/home/work/dsp/disconf-rd

set -e

export PATH
echo "**********************************************"
echo "It's going to Generate the output for war"
echo "**********************************************"

current_path=`pwd`

#
# 使用RD进行WAR打包
#
echo "**********************************************"
echo "It's going to got war package"
echo "**********************************************"

sh build.sh rd


#
# 使用RD进行FE打包
#
echo "**********************************************"
echo "It's going to got fe package"
echo "**********************************************"

cd dsp-static
sh build.sh

cd $current_path


# 清空原始目录 
mkdir -p $WAR_ROOT_PATH
if [ ${#WAR_ROOT_PATH} -gt 15 ]; then 
	echo "rm " $WAR_ROOT_PATH
	rm -rf "$WAR_ROOT_PATH"
    mkdir -p $WAR_ROOT_PATH
fi	

echo "start to copy war"
cp output/dsp-web.war  $WAR_ROOT_PATH  -rp

echo "start to copy static"
mkdir $WAR_ROOT_PATH/dsp-static
cp dsp-static/output/dsp-static.tar.gz $WAR_ROOT_PATH/dsp-static

cd $WAR_ROOT_PATH 

echo "start to jar war"
jar xvf dsp-web.war 

echo "start to tar fe"
cd dsp-static
tar xzvf dsp-static.tar.gz 

cd $current_path

echo "deploy done" $WAR_ROOT_PATH