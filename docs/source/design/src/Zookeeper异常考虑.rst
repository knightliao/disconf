disconf的Zookeeper异常考虑
==========================

disconf-web的ZK异常处理
-----------------------

disconf-web可以完全保证在任何情况下，与ZK集群的自动连接。

下面按情况进行分析：

服务启动前，zk连接不上：
~~~~~~~~~~~~~~~~~~~~~~~~

-  开始连接不上：

   -  apache ZK client自身会自动(永久）去连接ZK server.
      但是一直连接不上。
   -  因此，web上所有操作均会失败，抛大异常，请求失败，只会重试一次，不会重试多次

-  后面突然连接上了：

   -  apache ZK client 收到server SyncConnected消息。
   -  这时所有操作均成功

-  后面又突然连接不上了：

   -  apache ZK client 收到server Disconnected 消息。
   -  这时，apache ZK client自身会自动(永久）去连接ZK server.
      但是一直连接不上。
   -  这时 web 上
      所有操作均会失败，抛大异常，请求失败，只会重试一次，不会重试多次

-  后面突然连接上了：

   -  apache ZK client 收到server Expired 消息。
   -  这时表示会话丢失啦，apache ZK client
      自动断开与Server的连接，表示此时让你来处理，因为它不知道应该如何处理。
   -  这时，disconf-core会reconnect zkserver，重新建立会话。
   -  成功后，apache ZK client 收到server SyncConnected
      消息。表示连接成功

-  后面又突然连接不上了：

   -  apache ZK client 收到server Disconnected 消息。
   -  这时，apache ZK client自身会自动(永久）去连接ZK server.
      但是一直连接不上。
   -  这时 web 上
      所有操作均会失败，抛大异常，请求失败，只会重试一次，不会重试多次

服务启动前，zk连接上了：
~~~~~~~~~~~~~~~~~~~~~~~~

-  开始连接：

   -  apache ZK client 收到server SyncConnected消息。
   -  这时所有操作均成功功

-  后面又突然连接不上了…… （与上面分析一样，此不再赘述）

注意
~~~~

ZK一般需要以集群的形式提供出来。假设有N台ZK，

-  只要至少有一台ZK存活，disconf-web就可以正常工作。而且永远不会收到
   server Expired 的消息。
-  只要有一台ZK死亡，disconf-web就会收到 Disconnected
   消息。但是系统仍可以继续工作。
-  如果所有zk都死亡，那么disconf-web会收到 Disconnected
   消息。只要有一台存活，disconf-web就会收到

disconf-client的ZK异常处理
--------------------------

disconf-client可以完全保证:
**如果在启动程序时保证ZK集群是可用的**\ ，那么，就可以保证在任何情况下，与ZK集群的自动连接。

下面按情况进行分析：

程序启动前，zk连接不上：
~~~~~~~~~~~~~~~~~~~~~~~~

这时disconf-client无法在ZK上注册信息。这是必须禁止发生的情况。也是disconf-client无法支持的情况。

一旦发生这种情况，请先恢复ZK集群，再启动你的程序。

程序启动前，zk连接上了：
~~~~~~~~~~~~~~~~~~~~~~~~

如果在程序启动过程中，ZK是正常的，那么，disconf-client可以保证与ZK连接的自动性。

-  只要集群有一台还存活着，你的程序配置还是受disconf托管。
-  如果集群所有机器均死亡，这时你的程序将游离于disconf之外。只要集群中有任何一台ZK机器重新开启，那么
   你的程序将重新 由disconf进行托管。

注意
~~~~

disconf-client必须保证在程序在启动时，ZK集群的可用性。
