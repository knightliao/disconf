Tutorial 14 配置初始化 或 更新时，通知采用 "bean setter模式"
============================================================

目的
~~~~

当通过 java bean 进行控制配置时，当某个配置有初始化或更新时，可以在 bean
property setter 方法里 做适合自己的业务逻辑。

示例项目
~~~~~~~~

https://github.com/knightliao/disconf-demos-java/tree/master/disconf-standalone-demo

demo1: java bean 注解式配置
~~~~~~~~~~~~~~~~~~~~~~~~~~~

::

    /**
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
            LOGGER.info("i' m here: setting redis port");
        }

        public void reload() throws Exception {
            LOGGER.info("host: " + host);
        }
    }

在这里的 setPort 方法 会在该 javabean ``初始化`` 或者 ``配置更新`` 时
被调用 。

::

    public void setPort(int port) {
        this.port = port;
        LOGGER.info("i' m here: setting redis port");
    }

demo2: static class 注解式配置
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

::

    /**
     */
    @DisconfFile(filename = "static.properties")
    public class StaticConfig {

        protected static final Logger LOGGER = LoggerFactory.getLogger(StaticConfig.class);

        private static int staticVar;

        @DisconfFileItem(name = "staticVar", associateField = "staticVar")
        public static int getStaticVar() {
            return staticVar;
        }

        public static void setStaticVar(int staticVar) {
            StaticConfig.staticVar = staticVar;
            LOGGER.info("i' m here: setting static class variable");
        }

    }

在这里的 setStaticVar 方法 会在该 class ``初始化`` 或者 ``配置更新`` 时
被调用 。

demo3: 基于XML配置文件的无侵入式 配置
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

class:

::

    /**
     */
    public class AutoService {

        protected static final Logger LOGGER = LoggerFactory.getLogger(AutoService.class);

        private String auto;

        public String getAuto() {
            return auto;
        }

        public void setAuto(String auto) {
            this.auto = auto;
            LOGGER.info("i' m here: setting auto");
        }
    }

配置：

::

    <!-- 使用托管方式的disconf配置(无代码侵入, 配置更改会自动reload)-->
        <bean id="configproperties_disconf"
              class="com.baidu.disconf.client.addons.properties.ReloadablePropertiesFactoryBean">
            <property name="locations">
                <list>
                    <value>classpath:/autoconfig.properties</value>
                </list>
            </property>
        </bean>

        <bean id="propertyConfigurer"
              class="com.baidu.disconf.client.addons.properties.ReloadingPropertyPlaceholderConfigurer">
            <property name="ignoreResourceNotFound" value="true"/>
            <property name="ignoreUnresolvablePlaceholders" value="true"/>
            <property name="propertiesArray">
                <list>
                    <ref bean="configproperties_disconf"/>
                </list>
            </property>
        </bean>

demo4: 配置项 配置
~~~~~~~~~~~~~~~~~~

::

    @Service
    public class BaoBaoService {

        protected static final Logger LOGGER = LoggerFactory.getLogger(BaoBaoService.class);

        public static final String key = "moneyInvest";

        @Value(value = "2000d")
        private Double moneyInvest;

        @Autowired
        private Coefficients coefficients;

        /**
         * 计算百发一天赚多少钱
         *
         * @return
         */
        public double calcBaiFa() {
            return coefficients.getBaiFaCoe() * coefficients.getDiscount() * getMoneyInvest();
        }

        /**
         * k 计算余额宝一天赚多少钱
         *
         * @return
         */
        public double calcYuErBao() {
            return coefficients.getYuErBaoCoe() * coefficients.getDiscount() * getMoneyInvest();
        }

        /**
         * 投资的钱，分布式配置 <br/>
         * <br/>
         * 这里切面无法生效，因为SpringAOP不支持。<br/>
         * 但是这里还是正确的，因为我们会将值注入到Bean的值里.
         *
         * @return
         */
        @DisconfItem(key = key)
        public Double getMoneyInvest() {
            return moneyInvest;
        }

        public void setMoneyInvest(Double moneyInvest) {
            this.moneyInvest = moneyInvest;
            LOGGER.info("i' m here: setting moneyInvest");
        }

    }

在这里的 setMoneyInvest 方法 会在该 class ``初始化`` 或者 ``配置更新``
时 被调用 。

配置更新时通知的所有方式 总结
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

-  指定key的注解式 `Tutorial2 <Tutorial2>`__
-  统一通知模式 unify-notify `Tutorial13 <Tutorial13-unify-notify>`__
-  bean setter模式
