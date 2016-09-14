Tutorial 7 可自定义的部分托管的分布式配置
=======

假设我们已经将所有配置文件和配置项都使用Disconf进行托管了。

在多人开发情况下，可能会有一两个配置文件需要经常改动，且每个人的配置都不大一样，在这种情况下，当然希望此配置文件（或多个配置）
均不要使用Disconf托管。

Disconf考虑到了此种情况。举个实例，数据库配置文件，每个人的数据库可能不大一样，那么，你可以修改 disconf.properties ：

    # 忽略哪些分布式配置，用逗号分隔
    disconf.ignore=jdbc-mysql.properties

将此配置文件添加到ignore的列表里。这样，程序运行时，Disconf就会忽略托管此配置文件，而改为读取你本地的配置文件 jdbc-mysql.properties。

