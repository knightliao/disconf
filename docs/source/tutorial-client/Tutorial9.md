Tutorial 9 实现真正意义上的统一上线包 
=======

### 问题

一直以来，凡是使用 disconf的程序均需要 `disconf.properties` ，在这个文件里去控制 app/env/version。

因此，我们要部署到不同的环境中，还是需要 不同的 `disconf.properties`。

有一种解决方法是，通过 jenkins 来进行打包，准备多份 `disconf.properties` 文件。

### 解决方法

真正的解决方法是，使用 java 命令行参数

目前 disconf 已经支持 `disconf.properties` 中所有配置项 通过参数传入方式 启动。

支持的配置项具体可参见： [link](../../config/src/client-config.html)

这样的话，未来大家只要通过 Java 参数 就可以 动态的改变启动的 app/env/version

#### standalone 启动示例

    java  -Ddisconf.env=rd \
        -Ddisconf.enable.remote.conf=true \
        -Ddisconf.conf_server_host=127.0.0.1:8000 \
        -Dlogback.configurationFile=logback.xml \
        -Dlog4j.configuration=file:log4j.properties \
        -Djava.ext.dirs=lib \
        -Xms1g -Xmx2g -cp ampq-logback-client-0.0.1-SNAPSHOT.jar \
        com.github.knightliao.consumer.ConsumerMain >/dev/null 2>&1 &
        
这里表示使用 disconf.env=rd 

#### tomcat 启动示例