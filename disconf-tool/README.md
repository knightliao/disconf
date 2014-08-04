Disconf-tool
====

Disconf-tool是disconf的辅助工具类，主要的功能包括：

1. Disconf-ContextMgr: 一个基于Redis的Context持久化辅助类，方便的将一些频繁更新读取的数据保存起来；注意，ContextMgr并没有进行分布应用的读写控制，现阶段该工作应由使用方来完成。
2. [TODO]Disconf-Mointor：配置监控类，用于及时发现应用与服务间配置的不一致并报警；
