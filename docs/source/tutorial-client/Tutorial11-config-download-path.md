Tutorial 11 配置文件下载地址讨论
=======

### 问题一 从disconf下载的配置文件都放到哪里去了？

### 解决：按以下顺序进行判断

- 一定会下载到 disconf.user_define_download_dir 目录下(使用此方法可以方便的构造一个下载器 [Tutorial10](Tutorial10.html) )
- 如果 disconf.enable_local_download_dir_in_class_path 为true(默认值), 则会执行以下判断：
    - 如果 @DisconfFile 的 copy2TargetDirPath 值不为空，则会下载这个目录下。(copy2TargetDirPath值说明：以"/"开头则是系统的全路径，否则则是相对于classpath的路径，默认是classpath根路径
            注意：根路径要注意是否有权限，否则会出现找不到路径，推荐采用相对路径)。这对于那些不想放在classpath根目录的程序，比较有用。
    - 如果 @DisconfFile 的 copy2TargetDirPath 值为空，则会下载到根路径下。大部分程序是从根目录读取配置的，因此，默认是放到根下。
- 如果 disconf.enable_local_download_dir_in_class_path 为false, 则不会下载到classpath目录下

注： 

1. 如果不作任何配置的改变，默认情况下，会下载到 disconf.user_define_download_dir 目录 和 classpath 两个目录下。
2. 配置说明看这里 [config](../../config/client-config.html)

### 问题二 不想下载到classpath目录下

将 disconf.enable_local_download_dir_in_class_path 为false, 并 指定 下载目录  disconf.user_define_download_dir 