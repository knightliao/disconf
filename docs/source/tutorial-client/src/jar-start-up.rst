-jar jar包启动支持
==================

2.6.33 版本起开始支持
---------------------

主要升级点
~~~~~~~~~~

当使用以 -jar 方式启动的程序（非tomcat,web方式）时，例如 springboot
时，可以无缝对接（不会出现配置文件找不到的情况）

正确的使用方式
~~~~~~~~~~~~~~

::

    <!-- 使用托管方式的disconf配置(无代码侵入, 配置更改会自动reload)-->
    <bean id="configproperties_disconf"
          class="com.baidu.disconf.client.addons.properties.ReloadablePropertiesFactoryBean">
        <property name="locations">
            <list>
                <value>classpath*:autoconfig.properties</value>
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

细节
~~~~

在未升级前，要使用spring-boot，我们的配置可能是这样的

::

    <!-- 使用托管方式的disconf配置(无代码侵入, 配置更改会自动reload)-->
    <bean id="configproperties_disconf"
          class="com.baidu.disconf.client.addons.properties.ReloadablePropertiesFactoryBean">
        <property name="locations">
            <list>
                <value>file:autoconfig.properties</value>
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

注意，我们使用 ``file:autoconfig.properties`` 而不是
``classpath*:autoconfig.properties``\ 。这是为什么呢？

当以 -jar
的方式启动服务时，classpath可能会找不到，spring读取配置的原则是：

-  ``classpath*:autoconfig.properties`` : 只能读取jar包内的配置文件
-  ``file:autoconfig.properties`` : 读取的当前路径下的配置文件

当以 java入口类
的方式启动服务时，classpath一定是WEB-INF/classes或者target/classes目录下，spring读取配置的原则是：

-  ``classpath*:autoconfig.properties`` :
   读取的WEB-INF/classes或者target/classes目录下的配置文件
-  ``file:autoconfig.properties`` : 读取的是当前路径下的配置文件

在以-jar方式启动时，原则上，我们希望将配置文件放在jar包外面，所以这里采用了
``file:autoconfig.properties``\ ，当以这种方式启动程序时，disconf将配置下载到当前路径下，程序可以找到配置，不会报错。

| 但当你使用IDE(eclipse or
  intellij)启动时，它就报错了，说配置文件找不到了。这是因为IDE启动时，不是以-jar的方式启动的，它是以Java入口类的方式进行启动的，disconf将配置下载到target/classes
| 目录下，如果还是使用\ ``file:autoconfig.properties``\ ，程序在当前路径下是无法找到的。

这就产生了一个问题：我在IDE调试时，必须使用
``classpath*:autoconfig.properties`` ，当以命令行启动时，必须使用
``file:autoconfig.properties``

本次升级就是为了避免这个问题。未来配置统一是\ ``classpath*:autoconfig.properties``\ 。原理是，在以-jar启动时，通过将当前路径设置为classpath，程序启动后，disconf将配置下载到当前路径，通过读取classpath进行读取配置，就可以找到这个配置了。
