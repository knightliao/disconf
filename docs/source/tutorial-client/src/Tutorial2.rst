Tutorial 2 注解式分布式的配置文件高级篇: 配置更新的通知（最佳实践）
===================================================================

| 在 `Tutorial 1 <Tutorial1.html>`__ 里,
| 我们实现了一个简单的Redis服务程序，它使用分布式配置进行管理，此Redis的配置文件存储在分布式服务器
  disconf-web 上。

也许有一天，我们需要更新Redis的Host和Port配置数据。由于
redis是根据配置生成的实例，因此，这种情况下，你有三种选择：

-  不使用Disconf（\ `Tutorial 1 <Tutorial1.html>`__
   里第一部分的使用方法）。那么你需要 更改线上机器的配置文件
   redis.properties，重启服务就可以了。
-  使用Disconf, 采用 `Tutorial 1 <Tutorial1.html>`__
   里第二部分的使用的方案。那么你需要 更改 分布式服务器 disconf-web 上的
   redis.properties 文件。
   然后重启服务就可以了。和第一种方法的区别在于，不需要更改线上机器的配置文件。
-  使用Disconf，采用 `Tutorial 1 <Tutorial1.html>`__
   里第二部分的使用的方案，并额外加上本Tutorial的方案，那么你 只需要
   更改 分布式服务器 disconf-web 上的 redis
   .properties 文件。然后服务的配置自动生效。此过程无需要重新启动服务。

本教程就是阐述如何通过简单的配置和极简代码实现第三步的功能。

第一步：准备工作
----------------

完成 `Tutorial 1 <Tutorial1.html>`__ 上第二部分方案里的步骤。

第二步：修改 SimpleRedisService，支持Redis重连接
------------------------------------------------

在这里，我们这个类添加了一个方法，重新生成了一个Jedis对象，代码如下：

::

    /**
     * 更改Jedis
     */
    public void changeJedis() {

        LOGGER.info("start to change jedis hosts to: " + jedisConfig.getHost()
                + " : " + jedisConfig.getPort());

        jedis = JedisUtil.createJedis(jedisConfig.getHost(),
                jedisConfig.getPort());

        LOGGER.info("change ok.");
    }

之所以添加这个函数的原因是：在配置更新时，此函数要被调用，从而更改Jedis实例。

整个类的完整代码如下：

::

    package com.example.disconf.demo.service;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.DisposableBean;
    import org.springframework.beans.factory.InitializingBean;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Scope;
    import org.springframework.stereotype.Service;

    import com.example.disconf.demo.config.JedisConfig;
    import com.example.disconf.demo.utils.JedisUtil;

    import redis.clients.jedis.Jedis;

    /**
     * 一个简单的Redis服务
     *
     * @author liaoqiqi
     * @version 2014-6-17
     */
    @Service
    @Scope("singleton")
    public class SimpleRedisService implements InitializingBean, DisposableBean {

        protected static final Logger LOGGER = LoggerFactory.getLogger(SimpleRedisService.class);

        // jedis 实例
        private Jedis jedis = null;

        /**
         * 分布式配置
         */
        @Autowired
        private JedisConfig jedisConfig;

        /**
         * 关闭
         */
        public void destroy() throws Exception {

            if (jedis != null) {
                jedis.disconnect();
            }
        }

        /**
         * 进行连接
         */
        public void afterPropertiesSet() throws Exception {

            jedis = JedisUtil.createJedis(jedisConfig.getHost(), jedisConfig.getPort());
        }

        /**
         * 获取一个值
         *
         * @param key
         *
         * @return
         */
        public String getKey(String key) {
            if (jedis != null) {
                return jedis.get(key);
            }

            return null;
        }

        /**
         * 更改Jedis
         */
        public void changeJedis() {

            LOGGER.info("start to change jedis hosts to: " + jedisConfig.getHost() + " : " + jedisConfig.getPort());

            jedis = JedisUtil.createJedis(jedisConfig.getHost(), jedisConfig.getPort());

            LOGGER.info("change ok.");
        }
    }

第三步： 撰写配置更新回调类
---------------------------

当配置更新时，应用程序要得到通知。因此我们要写一个回调类来响应此“通知”。完整的类如下：

::

    package com.example.disconf.demo.service.callbacks;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Scope;
    import org.springframework.stereotype.Service;

    import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
    import com.baidu.disconf.client.common.update.IDisconfUpdate;
    import com.example.disconf.demo.config.Coefficients;
    import com.example.disconf.demo.config.JedisConfig;
    import com.example.disconf.demo.service.SimpleRedisService;

    /**
     * 更新Redis配置时的回调函数
     *
     * @author liaoqiqi
     * @version 2014-6-17
     */
    @Service
    @Scope("singleton")
    @DisconfUpdateService(classes = {JedisConfig.class}, itemKeys = {Coefficients.key})
    public class SimpleRedisServiceUpdateCallback implements IDisconfUpdate {

        protected static final Logger LOGGER = LoggerFactory.getLogger(SimpleRedisServiceUpdateCallback.class);

        @Autowired
        private SimpleRedisService simpleRedisService;

        /**
         *
         */
        public void reload() throws Exception {

            simpleRedisService.changeJedis();
        }

    }

**具体步骤是：**

-  撰写此类，实现 IDisconfUpdate
   接口。此类必须是JavaBean，Spring托管的，且 "scope"
   都必须是singleton的。
-  添加 @DisconfUpdateService 注解，classes 值加上 JedisConfig.class
   ，表示当 JedisConfig.class
   这个配置文件更新时，此回调类将会被调用。或者，使用
   confFileKeys 也可以。
-  在函数 reload() 里调用 SimpleRedisService 的 changeJedis() 方法

回调类与配置类放在一起
~~~~~~~~~~~~~~~~~~~~~~

如果你觉得写两个类太累，在某些场景下，则可以将回调与配置类放在一起的。

::

    /**
     * Redis配置文件
     *
     * @author liaoqiqi
     * @version 2014-6-17
     */
    @Service
    @Scope("singleton")
    @DisconfFile(filename = "redis.properties")
    @DisconfUpdateService(classes = {JedisConfig.class})
    public class JedisConfig implements IDisconfUpdate {

        protected static final Logger LOGGER = LoggerFactory.getLogger(JedisConfig.class);

        // 代表连接地址
        private String host;

        // 代表连接port
        private int port;

        /**
         * 地址, 分布式文件配置
         *
         * @return
         */
        @DisconfFileItem(name = "redis.host", associateField = "host")
        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        /**
         * 端口, 分布式文件配置
         *
         * @return
         */
        @DisconfFileItem(name = "redis.port", associateField = "port")
        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        @Override
        public void reload() throws Exception {
            LOGGER.info("host: " + host);
        }
    }

完结
----

至此，支持配置更新的 分布式配置文件 的撰写就已经写完了。

当用户在 disconf-web 上更新配置时，你的服务里的Redis就会指向新的地址。
