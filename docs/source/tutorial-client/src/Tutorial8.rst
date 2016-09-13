Tutorial 8 基于XML的分布式配置文件管理,自动reload
=================================================

在 `Tutorial1 <Tutorial1.html>`__, `Tutorial2 <Tutorial2.html>`__,
`Tutorial3 <Tutorial3.html>`__, `Tutorial4 <Tutorial4.html>`__
里讲到使用 注解式的分布式配置，它的特点是

-  优点：

   -  支持.properties配置文件
   -  支持配置项
   -  通过撰写配置类，代码结构清晰
   -  配置更新时，自动注入
   -  支持并发时配置更新统一生效
   -  无额外的XML配置，不需要在xml定义 java bean(config类)
   -  代码风格 简单易懂

-  缺点：

   -  需要撰写配置类，代码侵入

在 `Tutorial5 <Tutorial5.html>`__
里讲解了非注解式的分布式配置文件动态管理。它的特点是

-  优点：

   -  支持任意类型的配置文件
   -  无代码侵入

-  缺点：

   -  需要在xml定义 java bean
   -  配置更新时无法自动注入java
      bean里。你可以写回调函数来支持自动注入。

在本教程里，将讲解一种基于XML的分布式配置文件管理，它是\ `Tutorial5 <Tutorial5.html>`__\ 方式的一种增强，它的特点是：

-  优点：

   -  支持任意类型的配置文件
   -  对于.properties配置文件，配置更新时，自动注入reload
   -  无代码侵入
   -  适合于旧项目的迁移

-  缺点：

   -  需要在xml定义 java bean
   -  非.properties配置更新时无法自动注入java
      bean里，你可以写回调函数来支持自动注入。

在这里将以 disconf-standalone-demo中的 demo为例，讲解如何实现
无代码侵入的分布式配置

第一步：上传配置文件
--------------------

上传 autoconfig.properties 至 disconf-web里

第二步：修改配置文件
--------------------

添加 基本的 disconf支持

::

    <context:component-scan base-package="com.baidu,com.example"/>

    <aop:aspectj-autoproxy proxy-target-class="true"/>

    <!-- 使用disconf必须添加以下配置 -->
    <bean id="disconfMgrBean" class="com.baidu.disconf.client.DisconfMgrBean"
          destroy-method="destroy">
        <property name="scanPackage" value="com.example.disconf.demo"/>
    </bean>
    <bean id="disconfMgrBean2" class="com.baidu.disconf.client.DisconfMgrBeanSecond"
          init-method="init" destroy-method="destroy">
    </bean>

注：从版本\ ``2.6.30``\ 开始，不再需要扫描包\ ``com.baidu``\ 了，扫描自己的包即可。即：

::

    <context:component-scan base-package="com.example"/>

    <aop:aspectj-autoproxy proxy-target-class="true"/>

    <!-- 使用disconf必须添加以下配置 -->
    <bean id="disconfMgrBean" class="com.baidu.disconf.client.DisconfMgrBean"
          destroy-method="destroy">
        <property name="scanPackage" value="com.example.disconf.demo"/>
    </bean>
    <bean id="disconfMgrBean2" class="com.baidu.disconf.client.DisconfMgrBeanSecond"
          init-method="init" destroy-method="destroy">
    </bean>

特别的，添加 需要进行托管的 配置文件：

::

    <!-- 使用托管方式的disconf配置(无代码侵入, 配置更改会自动reload)-->
    <bean id="configproperties_disconf"
          class="com.baidu.disconf.client.addons.properties.ReloadablePropertiesFactoryBean">
        <property name="locations">
            <list>
                <value>classpath:/autoconfig.properties</value>
                <value>classpath:/autoconfig2.properties</value>
            </list>
        </property>
    </bean>

    <bean id="propertyConfigurer"
          class="com.baidu.disconf.client.addons.properties.ReloadingPropertyPlaceholderConfigurer">
        <property name="ignoreResourceNotFound" value="true" />
        <property name="ignoreUnresolvablePlaceholders" value="true" />
        <property name="propertiesArray">
            <list>
                <ref bean="configproperties_disconf"/>
            </list>
        </property>
    </bean>

在这里，添加了 6个配置文件，其中有4个.properties， 2个非properties文件

添加需要配置的java bean
-----------------------

::

    <bean id="autoService" class="com.example.disconf.demo.service.AutoService">
        <property name="auto" value="${auto=100}"/>
    </bean>

    <bean id="autoService2" class="com.example.disconf.demo.service.AutoService2">
        <property name="auto2" value="${auto2=100}"/>
    </bean>

java bean 与 传统的 spring 写法没有任何区别

补充
----

如果想配置文件，但是不想自动reload，那么该怎么办？

可以使用以下与本方法非常相似的做法：

::

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

在这里，myserver.properties被disconf托管，当在disconf-web上修改配置文件时，配置文件会被自动下载至本地，但是不会reload到系统里。

具体可参见：\ `Tutorial5 <Tutorial5.html>`__

完结
----

当在disconf-web中对 4个properties文件中的任何一个文件更新时，所有
使用这些配置文件的java bean都将自动重新注入。无需重启程序。

非properties文件，则需要重启程序才可以支持。当然你可以写回调函数来支持自动注入。
