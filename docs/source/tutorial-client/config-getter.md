统一类获取任何配置数据
==============

## 2.6.34 版本起开始支持 ##

### 主要升级点

增加统一的类 来个性化编程式的获取任何配置数据, 目前只支持 .properties 文件

### 接口

    public class DisconfDataGetter {
    
        private static IDisconfDataGetter iDisconfDataGetter = new DisconfDataGetterDefaultImpl();
    
        /**
         * 根据 分布式配置文件 获取该配置文件的所有数据，以 map形式呈现
         *
         * @param fileName
         *
         * @return
         */
        public static Map<String, Object> getByFile(String fileName) {
            return iDisconfDataGetter.getByFile(fileName);
        }
    
        /**
         * 获取 分布式配置文件 获取该配置文件 中 某个配置项 的值
         *
         * @param fileName
         * @param fileItem
         *
         * @return
         */
        public static Object getByFileItem(String fileName, String fileItem) {
            return iDisconfDataGetter.getByFileItem(fileName, fileItem);
        }
    
        /**
         * 根据 分布式配置 获取其值
         *
         * @param itemName
         *
         * @return
         */
        public static Object getByItem(String itemName) {
            return iDisconfDataGetter.getByItem(itemName);
        }
    }

### 使用方式
    
获取 配置文件 redis.properties 的KV值：
    
    DisconfDataGetter.getByFile("redis.properties");
    
获取 配置文件 autoconfig.properties 的KV值：

    DisconfDataGetter.getByFile("autoconfig.properties")
    
获取 配置文件 autoconfig.properties 中 key为 auto 的值：
    
    DisconfDataGetter.getByFile("autoconfig.properties").get("auto")

获取 配置文件 autoconfig.properties 中 key为 auto 的值：
    
    DisconfDataGetter.getByFileItem("autoconfig.properties", "auto")

获取 配置项 moneyInvest 的值：    
    
    DisconfDataGetter.getByItem("moneyInvest");