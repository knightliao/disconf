Tutorial 5 基于XML的分布式配置文件管理,不会自动reload
=======

在 [Tutorial 1](Tutorial1.html) 里, 我们实现了一个简单的Redis服务程序，它使用分布式配置进行管理，此Redis的配置文件存储在分布式服务器 disconf-web 上。它使用的是注解式的配置管理。

Disconf亦支持非注解式的分布式配置管理，下面定义一下概念：

- 非注解式（托管式）：配置文件没有相应的配置注解类，此配置文件不会被注入到配置类中。disconf只是简单的对其进行“托管”。
启动时下载配置文件；配置文件变化时，负责动态推送。注意，此种方式，程序不会自动reload配置，需要自己写回调函数。

**由于此方法不会自动reload，因此，对于那些比较重的资源，比如jdbc等，是比较好的托管方式。**

使用方法就是在你的applicationContext.xml里添加以下一段代码：

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

该配置文件列表将被Disconf全部托管。实例启动时，这些配置文件将被下载，当配置更改时，实例亦能感知到，并重新下载这些配置，并且自动调用回调函数。

目前支持 任意类型的 配置文件的托管。

注意：这些配置文件没有相应的配置文件类。