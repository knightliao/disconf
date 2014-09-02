#!/bin/bash

#
# War包的部署
#

#
if [ $# -le 0 ];then
		echo "**********************************************"
        echo "use online profile"
        echo "**********************************************"
        mvn clean
        mvn package install -Dmaven.test.skip=true -Ponline -U
else
	
	if [ "$1" == "rd" ];then		
        echo "**********************************************"
        echo "use default profile(RD)"
        echo "**********************************************"
        mvn clean
        mvn package install -Dmaven.test.skip=true  -U
	else
        echo "**********************************************"
        echo "use the following as profile"
        echo $1
        echo "**********************************************"
        mvn clean
        mvn package install -P$1 -U
	fi
fi

#
#
#
if [ -d "output" ]; then
    printf '%s\n' "Removing output"
    rm -rf output
fi

mkdir -p output

cp target/disconf-web.war output


