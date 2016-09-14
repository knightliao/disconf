Client常问问题
=======

## **异常:** ERROR c.b.d.c.c.processor.impl.DisconfCoreProcessUtils - Spring Context is null. Cannot autowire com.szzjcs.commons.thirdapi.push.config.JpushConfig
   
**可能原因：**

- 程序没有使用spring环境
-  `<context:component-scan>` 放在 disconfMgrBean 定义的后面
- 对于版本2.6.28（包括此版本）之前的版本， component-scan 可能没有扫描 `com.baidu.disconf` 

**解决办法：**

- 非静态配置 必须使用spring环境
- `<context:component-scan>`  必须出现在disconfMgrBean之前
- 对于版本2.6.28（包括此版本）之前的版本，必须使 component-scan 增加扫描项 `com.baidu.disconf` 
  
