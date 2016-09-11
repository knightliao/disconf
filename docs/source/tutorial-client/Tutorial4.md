Tutorial 4 注解式分布式静态配置文件和静态配置项（最佳实践）
=======

## 配置类 ##

### 定义 ###

    package com.example.disconf.demo.config;
    
    import com.baidu.disconf.client.common.annotations.DisconfFile;
    import com.baidu.disconf.client.common.annotations.DisconfFileItem;
    
    /**
     * 静态 配置文件 示例
     *
     * @author liaoqiqi
     * @version 2014-6-17
     */
    @DisconfFile(filename = "static.properties")
    public class StaticConfig {
    
        private static int staticVar;
    
        @DisconfFileItem(name = "staticVar", associateField = "staticVar")
        public static int getStaticVar() {
            return staticVar;
        }
    
        public static void setStaticVar(int staticVar) {
            StaticConfig.staticVar = staticVar;
        }
    
    }

### 使用 ###
     
    package com.example.disconf.demo.service;
    
    import com.baidu.disconf.client.common.annotations.DisconfItem;
    import com.example.disconf.demo.config.StaticConfig;
    
	/**
	 * 使用静态配置文件的示例<br/>
	 * Plus <br/>
	 * 静态配置项 使用示例
	 * 
	 * @author liaoqiqi
	 * @version 2014-8-14
	 */
	public class SimpleStaticService {
	
	    private static int staticItem = 56;
	
	    /**
	     * 
	     * @return
	     */
	    public static int getStaticFileData() {
	
	        return StaticConfig.getStaticVar();
	    }
	}

和

	LOGGER.info("static file data:"
	                        + SimpleStaticService.getStaticFileData());

## 配置项 ##

###定义####

    package com.example.disconf.demo.service;
    
    import com.baidu.disconf.client.common.annotations.DisconfItem;
    import com.example.disconf.demo.config.StaticConfig;

	/**
	 * 使用静态配置文件的示例<br/>
	 * Plus <br/>
	 * 静态配置项 使用示例
	 * 
	 * @author liaoqiqi
	 * @version 2014-8-14
	 */
	public class SimpleStaticService {
	
	    private static int staticItem = 56;
	
	    /**
	     * 
	     * @return
	     */
	    public static int getStaticFileData() {
	
	        return StaticConfig.getStaticVar();
	    }
	
	    @DisconfItem(key = "staticItem")
	    public static int getStaticItem() {
	        return staticItem;
	    }
	
	    public static void setStaticItem(int staticItem) {
	        SimpleStaticService.staticItem = staticItem;
	    }
	}


###使用###

    LOGGER.info("static item data:"
                        + SimpleStaticService.getStaticItem());