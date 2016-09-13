Tutorial 10 实现一个配置更新下载器agent
=======================================

问题
~~~~

我想在我的机器上做一个配置下载器agent, 可以实现以下功能：

-  启动时下载配置
-  配置被更新时，可以感知并下载下来

解决方法
~~~~~~~~

| 可以修改一下 disconf-demos/disconf-standalone-demo
  这个项目，让其变成一个 长驻进程，并指定
| `disconf.user\_define\_download\_dir <../../config/client-config.html>`__
  这个配置到你想指定的路径。

done.
