Tutorial 13 增加统一的回调类 (unify-notify模式) 灵活处理更新配置通知
=======

### 目的

当 任意的 配置文件 或 配置项 得到更新时，此类 就会被调用。

它与 [Tutorial2](Tutorial2.html) 不一样，不需要注解，不需要必须指定变更地象。更加freely，方便大家在这里统一的、自由的控制更新逻辑. 

### 示例项目

https://github.com/knightliao/disconf-demos-java/tree/master/disconf-standalone-demo

### demo

只要实现 `IDisconfUpdatePipeline` 接口即可。不要求必须是 java bean.

- 函数 `reloadDisconfFile` 是针对分布式配置文件的。key是文件名；filePath是文件路径。用户可以在这里(read file freely)按你喜欢的解析文件的方式进行处理。
- 函数 `reloadDisconfItem` 是针对分布式配置项的。key是配置项名；content是其值，并且含有类型信息。

示例代码：

    /**
     */
    @Service
    public class UpdatePipelineCallback implements IDisconfUpdatePipeline {
    
        public void reloadDisconfFile(String key, String filePath) throws Exception {
            System.out.println(key + " : " + filePath);
        }
    
        public void reloadDisconfItem(String key, Object content) throws Exception {
            System.out.println(key + " : " + content);
        }
    }

    



