disconf-spring-boot-demo
=======
使用disconf的spring-boot demo程序,更少的配置


项目启动运行Application main方法

    @Configuration
    @EnableAutoConfiguration
    @ComponentScan(basePackages = {"com.baidu","com.example"})
    @PropertySource({"classpath:application.properties"})
    @ImportResource({"classpath:disconf.xml"})//引入disconf
    public class Application extends SpringBootServletInitializer {

        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }

        @Override
        protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
            return application.sources(Application.class);
        }

    }


disconf.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
           xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans-3.1.xsd
            http://www.springframework.org/schema/aop
            http://www.springframework.org/schema/aop/spring-aop-3.0.xsd">
        <aop:aspectj-autoproxy proxy-target-class="true"/>

        <!-- 使用disconf必须添加以下配置 -->
        <bean id="disconfMgrBean" class="com.baidu.disconf.client.DisconfMgrBean"
              destroy-method="destroy">
            <property name="scanPackage" value="com.example.disconf"/>
        </bean>
        <bean id="disconfMgrBean2" class="com.baidu.disconf.client.DisconfMgrBeanSecond"
              init-method="init" destroy-method="destroy">
        </bean>
    </beans>