TutorialSummary 分布式配置系统功能概述
=======

## 托管配置 ##

通过简单的注解类方式 托管配置。托管后，本地不需要此配置文件，统一从配置中心服务获取。

当配置被更新后，**注解类的数据自动同步**。

    @Service
    @DisconfFile(filename = "redis.properties")
    public class JedisConfig {
    
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
    }

[Tutorial1](../../tutorial-client/Tutorial1.html)

## 配置更新回调 ##

如果配置更新时，您需要的是 **不仅注解类自动同步，并且其它类也需要做些变化**，那么您需要一个回调来帮忙。

    @Service
    @Scope("singleton")
    @DisconfUpdateService(classes = { JedisConfig.class }) // 这里或者写成 @DisconfUpdateService(confFileKeys = { "redis.properties" })
    public class SimpleRedisServiceUpdateCallback implements IDisconfUpdate 

[Tutorial2](../../tutorial-client/Tutorial2.html)

## 支持基于XML的配置文件托管 ##

除了支持基于注解式的配置文件，我们还支持 基于XML无代码侵入式的：

（properties文件更新时数据自动同步reload，非properties文件需要写回调来支持数据自动同步）

    <bean id="configproperties_disconf"
          class="com.baidu.disconf.client.addons.properties.ReloadablePropertiesFactoryBean">
        <property name="locations">
            <list>
                <value>classpath:/autoconfig.properties</value>
                <value>classpath:/autoconfig2.properties</value>
                <value>classpath:/myserver_slave.properties</value>
                <value>classpath:/testJson.json</value>
                <value>classpath:/testXml2.xml</value>
                <value>myserver.properties</value>
            </list>
        </property>
    </bean>

    <bean id="propertyConfigurer"
          class="com.baidu.disconf.client.addons.properties.ReloadingPropertyPlaceholderConfigurer">
        <property name="propertiesArray">
            <list>
                <ref bean="configproperties_disconf"/>
            </list>
        </property>
    </bean>
    
    <bean id="autoService" class="com.example.disconf.demo.service.AutoService">
        <property name="auto" value="${auto=100}"/>
    </bean>

[Tutorial8](../../tutorial-client/Tutorial8.html)

## 支持配置项 ##

变量亦支持分布式配置哦

    @DisconfItem(key = key)
    public Double getMoneyInvest() {
        return moneyInvest;
    }

[Tutorial3](../../tutorial-client/Tutorial3.html)

    
## 支持静态配置 ##

除了支持@Service类以外，我们还支持 静态配置

    @DisconfFile(filename = "static.properties")
    public class StaticConfig {
    
        private static int staticVar;
    
        @DisconfFileItem(name = "staticVar", associateField = "staticVar")
        public static int getStaticVar() {
            return staticVar;
        }
    }

[Tutorial4](../../tutorial-client/Tutorial4.html)

## 支持基于XML的配置文件托管: 不会自动reload ##

与 **支持基于XML的配置文件托管** 相对应，只是在配置文件更改时，不会自动reload到java bean里。

值得说的是，此种方式支持 任意类型 格式配置文件。

    <!-- 使用托管方式的disconf配置(无代码侵入, 配置更改不会自动reload)-->
    <bean id="configproperties_no_reloadable_disconf"
          class="com.baidu.disconf.client.addons.properties.ReloadablePropertiesFactoryBean">
        <property name="locations">
            <list>
                <value>myserver.properties</value>
            </list>
        </property>
    </bean>

    <bean id="propertyConfigurerForProject1"
          class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="ignoreResourceNotFound" value="true"/>
        <property name="ignoreUnresolvablePlaceholders" value="true"/>
        <property name="propertiesArray">
            <list>
                <ref bean="configproperties_no_reloadable_disconf"/>
            </list>
        </property>
    </bean>


[Tutorial5](../../tutorial-client/Tutorial5.html)

## 过滤要进行托管的配置 ##

有时候你不想托管所有的配置文件，有1~2个配置文件你只想和本地的，可以：

    # 忽略哪些分布式配置，用逗号分隔
    ignore=jdbc-mysql.properties

[Tutorial7](../../tutorial-client/Tutorial7.html)

## 强大的WEB配置平台控制 ##

在WEB配置平台上，您可以

- 上传、更新 您的配置文件、配置项（有邮件通知），并且实现动态推送。
- 批量下载配置文件，查看ZK上部署情况
- 查看 此配置的影响范围： 哪些机器在使用，各机器上的配置内容各是什么，并且自动校验 一致性。
- 支持 自动化校验配置一致性。
- 简单权限控制

详见：[Tutorial6](../../tutorial-web/Tutorial6.html)

![](http://ww3.sinaimg.cn/mw1024/60c9620fgw1ekdeid28pmj20rc0fkdj9.jpg)







