Disconf-client详细设计文档
=======

本文档主要阐述了版本 Disconf-Client 的设计。、

## 程序运行流程图 ##

### 版本2.0的设计 ###

![](http://ww3.sinaimg.cn/bmiddle/60c9620fjw1eqi7tnuic8j20l50g7acs.jpg)

[点击查看大图 ](http://ww3.sinaimg.cn/mw1024/60c9620fjw1eqi7tnuic8j20l50g7acs.jpg)

**运行流程详细介绍：**

- **启动事件A**：以下按顺序发生。
	- A1：扫描静态注解类数据，并注入到配置仓库里。
	- A2：根据仓库里的配置文件、配置项，到 disconf-web 平台里下载配置数据。
	- A3：将下载得到的配置数据值注入到仓库里。
	- A4：根据仓库里的配置文件、配置项，去ZK上监控结点。
	- A5：根据XML配置定义，到 disconf-web 平台里下载配置文件，放在仓库里，并监控ZK结点。
	- A6：A1-A5均是处理静态类数据。A6是处理动态类数据，包括：实例化配置的回调函数类；将配置的值注入到配置实体里。
- **更新配置事件B**：以下按顺序发生。
	- B1：管理员在 Disconf-web 平台上更新配置。
	- B2：Disconf-web 平台发送配置更新消息给ZK指定的结点。
	- B3：ZK通知 Disconf-cient 模块。
	- B4：与A2一样。唯一不同的是它只处理一个配置文件或者一个配置项，而事件A2则是处理所有配置文件和配置项。下同。
	- B5：与A3一样。
	- B6：基本与A4一样，区别是，这里还会将配置的新值注入到配置实体里。

### 完全版的设计 ###

![](http://ww3.sinaimg.cn/bmiddle/60c9620fjw1eqj81no7shj20l50h2q65.jpg)

[点击查看大图 ](http://ww3.sinaimg.cn/mw1024/60c9620fjw1eqj81no7shj20l50h2q65.jpg

**运行流程详细介绍：**

与2.0版本的主要区别是支持了：主备分配功能/主备切换事件。

- **启动事件A**：以下按顺序发生。
	- A3：扫描静态注解类数据，并注入到配置仓库里。
	- A4+A2：根据仓库里的配置文件、配置项，去 disconf-web 平台里下载配置数据。这里会有主备竞争
	- A5：将下载得到的配置数据值注入到仓库里。
	- A6：根据仓库里的配置文件、配置项，去ZK上监控结点。
    - A7+A2：根据XML配置定义，到 disconf-web 平台里下载配置文件，放在仓库里，并监控ZK结点。这里会有主备竞争。
	- A8：A1-A6均是处理静态类数据。A7是处理动态类数据，包括：实例化配置的回调函数类；将配置的值注入到配置实体里。
- **更新配置事件B**：以下按顺序发生。
	- B1：管理员在 Disconf-web 平台上更新配置。
	- B2：Disconf-web 平台发送配置更新消息给ZK指定的结点。
	- B3：ZK通知 Disconf-cient 模块。
	- B4：与A4一样。
	- B5：与A5一样。
	- B6：基本与A4一样，唯一的区别是，这里还会将配置的新值注入到配置实体里。
- **主备机切换事件C**：以下按顺序发生。
	- C1：发生主机挂机事件。
	- C2：ZK通知所有被影响到的备机。
	- C4：与A2一样。
	- C5：与A4一样。
	- C6：与A5一样。
	- C7：与A6一样。

## 类设计图 ##

![](http://ww4.sinaimg.cn/bmiddle/60c9620fgw1ej0ycv2fjbj21ao0u8441.jpg)

[查看大图](http://ww4.sinaimg.cn/mw1024/60c9620fgw1ej0ycv2fjbj21ao0u8441.jpg)

**Disconf-client包括的大模块有：**

- scan 配置扫描模块
- core 配置核心处理模块
- fetch 配置抓取模块
- watch 配置监控模块
- store 配置仓库模块
- addons 配置reload模块

**各个模块均采用以下设计模式来进设计：**

- 各个模块均以接口的方式对外暴露，松耦合，强内聚
- 各个模块均提供工厂类由其它模块来进行获取实例，实例的操纵方式均采用接口方式。
- 对于配置文件和配置项，采用类扩展的方法来避免if else判断。

## Disconf-client 的启动  ##

启动分成两步，由两个Bean来实现

    <bean id="disconfMgrBean" class="com.baidu.disconf.client.DisconfMgrBean"
        destroy-method="destroy">
        <property name="scanPackage" value="com.baidu.disconf.demo" />
    </bean>
    <bean id="disconfMgrBean2" class="com.baidu.disconf.client.DisconfMgrBeanSecond"
        init-method="init" destroy-method="destroy">
    </bean>

这里 com.baidu.disconf.dem 是要扫描的类。

第一步由Bean com.baidu.disconf.client.DisconfMgrBean 来控制。第二步由 com.baidu.disconf.client.DisconfMgrBeanSecond 控制。

### 第一步：com.baidu.disconf.client.DisconfMgrBean ###

此Bean实现了BeanFactoryPostProcessor和PriorityOrdered接口。它的Bean初始化Order是最高优先级的。

因此，当Spring扫描了所有的Bean信息后，在所有Bean初始化（init）之前，DisconfMgrBean的postProcessBeanFactory方法将被调用，在这里，Disconf-Client会进行第一次扫描。

扫描按顺序做了以下几个事情：

1. 初始化Disconf-client自己的配置模块。
2. 初始化Scan模块。
3. 初始化Core模块，并极联初始化Watch，Fetcher，Restful模块。
4. 扫描用户类，整合分布式配置注解相关的静态类信息至配置仓库里。
5. 执行Core模块，从disconf-web平台上下载配置数据：配置文件下载到本地，配置项直接下载。
6. 配置文件和配置项的数据会注入到配置仓库里。
7. 使用watch模块为所有配置关联ZK上的结点。

其中对配置的处理详细为：

![](http://ww3.sinaimg.cn/bmiddle/60c9620fgw1ej1x5tfvzgj20pj141421.jpg)

[查看大图](http://ww3.sinaimg.cn/mw1024/60c9620fgw1ej1x5tfvzgj20pj141421.jpg)

### 第二步：com.baidu.disconf.client.DisconfMgrBeanSecond ###

DisconfMgrBean的扫描主要是静态数据的初始化，并未涉及到动态数据。DisconfMgrBeanSecond Bean则是将一些动态的数据写到仓库里。

本次扫描按顺序做了以下几个事情：

1. 将配置更新回调实例放到配置仓库里
2. 为配置实例注入值。

![](http://ww3.sinaimg.cn/bmiddle/60c9620fgw1ej1x5ve5w6j20pj11xwj4.jpg)

[查看大图](http://ww3.sinaimg.cn/mw1024/60c9620fgw1ej1x5ve5w6j20pj11xwj4.jpg)

## 分布式配置的实现  ##

下面将 分别详细阐述 分布式配置文件 和 分布式配置项 的实现方式。

由于目前版本只支持 Spring编程方式，因此，以下均只阐述Spring编程下的实现方式。

### 注解式实现 ###

#### 分布式配置文件的实现 ####

**定义分布式配置文件类**

对于配置文件，我们必须实现一个Java类来表示此 分布式配置文件。如：

    package com.example.disconf.demo.config;
    
    import org.springframework.context.annotation.Scope;
    import org.springframework.stereotype.Service;
    
    import com.baidu.disconf.client.common.annotations.DisconfFile;
    import com.baidu.disconf.client.common.annotations.DisconfFileItem;
    
    /**
     * Redis配置文件
     *
     * @author liaoqiqi
     * @version 2014-6-17
     */
    @Service
    @Scope("singleton")
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


对于此Java类，它必须是Spring托管的。此配置文件是redis.properties。

此配置类必须标注为 @DisconfFile，标识它是一个分布式配置文件。且必须指定文件名。

此配置类含有两个配置项，分别是host和port。这两个变量必须有 get 方法。且get方法名必须是符合JavaBean规范的。

我们通过在这两个变量的 get 方法上添加 @DisconfFileItem 注解来标注它是分布式配置文件里的配置项。必须指定name参数，表示配置文件里的KEY值。associateField值是可选的，表示此get方法相对应的域的名字。

**Disconf-client优先启动，并从平台上下载配置文件：**

应用程序启动时，当Spring容器扫描了所有Java Bean却还未初始化这些Bean时，disconf-client 模块会优先开始初始化（最高优先级）。它会将 配置文件名、配置项名记录在配置仓库里，并去 disconf-web 平台下载配置文件至classpath目录下。并且，还会到ZK上生成相应的结点。

接着Spring开始初始化用户定义的SpringBean。由于配置文件已经被正确下载至Classpath路径下，因此，JavaBean的配置文件使用的是分布式配置文件，而非本地的配置文件。

**待SpringBean初始化后，Disconf-client会获取配置更新回调类实例：**

此时，Spring上的所有Bean均已被init。Disconf-client模块会再次运行，这时它会去获取用户撰写的配置更新回调函数类实例。

一个配置更新回调函数通常是这样撰写的：

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

此类必须实现接口IDisconfUpdate，它可以不必是Java托管的。如果是SpringBean，则disconf-client会从Spring容器里获取此Bean。如果它不是SpringBean，disconf-client就会new一个实例出来。

使用SpringBean来定义此类的好处是，我们可以在此类中使用@Autowired来使用其它SpringBean。比较方便些。

disconf-client根据注解@DisconfUpdateService 以配置文件为Key，将回调函数实例列表放在此Key的Map里。当配置文件更新时，这些回调函数实例就会被按顺序执行。

**配置文件更新时，分布式配置文件会重新被下载：**

当配置文件更新时，disconf-client便会重新从 disconf-web 平台下载配置文件，并重新将值放在配置仓库里。并按顺序进行调用回调函数类的 reload() 方法。

**如何使用分布式配置文件类：**

在上面我们说到，配置文件类中的配置项必须有 get 方法，并且必须有 @DisconfFileItem 注解。

在 get 上面添加注解的原因就是为了做切面。

disconf-cient使用Spring AOP拦截 系统里所有含有@DisconfFileItem注解的 get 方法，把所有此类请求都定向到用户程序的配置仓库中去获取。

通过这种方式，我们可以实现统一的、集中式的在配置仓库里去获取配置文件数据。这是一种简洁的实现方式。

#### 分布式配置项的实现 ####

配置项相对于配置文件，比较灵活。我们可以在任何SpringBean里添加配置项。

如以下是在一个配置文件类里添加配置项：

    package com.example.disconf.demo.config;
    
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;
    
    import com.baidu.disconf.client.common.annotations.DisconfFile;
    import com.baidu.disconf.client.common.annotations.DisconfFileItem;
    import com.baidu.disconf.client.common.annotations.DisconfItem;
    
    /**
     * 金融系数文件
     */
    @Service
    @DisconfFile(filename = "coefficients.properties")
    public class Coefficients {
    
        public static final String key = "discountRate";
    
        @Value(value = "2.0d")
        private Double discount;
    
        private double baiFaCoe;
    
        private double yuErBaoCoe;
    
        /**
         * 阿里余额宝的系数, 分布式文件配置
         *
         * @return
         */
        @DisconfFileItem(name = "coe.baiFaCoe")
        public double getBaiFaCoe() {
            return baiFaCoe;
        }
    
        public void setBaiFaCoe(double baiFaCoe) {
            this.baiFaCoe = baiFaCoe;
        }
    
        /**
         * 百发的系数, 分布式文件配置
         *
         * @return
         */
        @DisconfFileItem(name = "coe.yuErBaoCoe")
        public double getYuErBaoCoe() {
            return yuErBaoCoe;
        }
    
        public void setYuErBaoCoe(double yuErBaoCoe) {
            this.yuErBaoCoe = yuErBaoCoe;
        }
    
        /**
         * 折扣率，分布式配置
         *
         * @return
         */
        @DisconfItem(key = key)
        public Double getDiscount() {
            return discount;
        }
    
        public void setDiscount(Double discount) {
            this.discount = discount;
        }
    }

或者，我们也可以在一个Service类里添加配置项：

    package com.example.disconf.demo.service;
    
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;
    
    import com.baidu.disconf.client.common.annotations.DisconfItem;
    import com.example.disconf.demo.config.Coefficients;
    
    /**
     * 金融宝服务，计算一天赚多少钱
     *
     * @author liaoqiqi
     * @version 2014-5-16
     */
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
        }
    }


采用哪种方式，由用户选择。

值得注意的是，在第二种实现中，它的方法calcBaiFa() 时调用了 getMoneyInvest() 方法。 getMoneyInvest() 是配置项的get方法，它添加了@DisconfItem注解，表明它是一个配置项，并且会被切面拦截，moneyInvest的值会在配置仓库里获取。但是，可惜的是，SpringAOP是无法拦截"Call myself"方法的。也就是说getMoneyInvest()是无法被切面拦截到的。

为了解决此问题，在实现中，我们不仅将它的值 注入到配置仓库中，而且还注入到配置项所在类的实例里。因此，在上面第二种实现中，虽然 getMoneyInvest()  方法无法被拦截，但是它返回的还是正确的分布式值的。

配置文件也一样，配置值亦会注入到配置文件类实体中。

#### 非Spring编程的实现 ####

在非Spring方式下，无法使用AOP切面编程，因此无法统一的拦截配置数据请求。

在这种情况下，用户配置类的实现有两种方式：

1. 配置类的域是static。用户直接访问这些域便可以获取得到配置类数据。
2. 配置类使用单例。用户通过单例访问配置获取配置类数据。

注意：此两种方式均无法自动避免“配置读取不一致问题”。

当事件发生时，用户程序处理配置的方式是：

1. 配置文件更新时，系统会自动去下载配置文件存储到本地，并存储到配置仓库。对于static变量，系统会自动注入到配置类中。对于使用单例实现方式，用户必须在回调函数中进行用户配置类的更新。
2. 配置项更新时，与配置文件更新一样。

#### Zookeeper的目录存储结构 ####

	|----disconf  
	        |----app1_version1_env1        
					|----file
							|----confA.properties	
					|----item
							|----keyA
	        |----app2_version2_env2
					|----file
							|----conf2.properties	
					|----item
							|----key2

### 基于XML的实现

虽然注解式编程简单、直观，易维护，但是，它是具有一定的代码侵入性的。
disconf考虑到有些用户不想写代码，只想通过XML配置（可能是在旧项目中使用disconf）来实现分布式配置的需求。因此，disconf亦实现了基于XML分布式的实现方式。

#### ReloadablePropertiesFactoryBean实现了配置文件的disconf托管  

ReloadablePropertiesFactoryBean继承了PropertiesFactoryBean类，它主要做到：

- 托管配置文件至disconf仓库，并下载至本地。
- 解析配置数据传递到 ReloadingPropertyPlaceholderConfigurer

#### ReloadingPropertyPlaceholderConfigurer实现了配置数据至Bean的映射

ReloadingPropertyPlaceholderConfigurer继承自Spring的配置类PropertyPlaceholderConfigurer，它会在Spring启动时将配置数据与Bean做映射，以便在检查到配置文件更改时，可以实现Bean相关域值的自动注入。

#### ReloadConfigurationMonitor 定时校验配置是否更新

它是一个Timer类，定时校验配置是否有更改，进而促发 ReloadingPropertyPlaceholderConfigurer 类来分析要对哪些 Bean实例进行重新注入。


## 系统配置 ##

<table border="0" cellspacing="0" cellpadding="0">
  <tr>
   <th width="100px">配置项</th>
   <th width="150px">说明</th>
   <th width="30px">是否必填</th>
   <th width="50px">默认值</th>
  </tr>
  <tr>
    <td width="100px">conf_server_store_action</td>
    <td width="150px">仓库 URL</td>
    <td width="30px">是</td>
    <td width="50px">/api/config</td>
  </tr>
  <tr>
    <td width="100px">conf_server_zoo_action</td>
    <td width="150px">zoo URL</td>
    <td width="30px">是</td>
    <td width="50px">/api/zoo</td>
  </tr>
  <tr>
    <td width="100px">conf_server_master_num_action</td>
    <td width="150px">获取远程主机个数的URL</td>
    <td width="30px">是</td>
    <td width="50px">/api/getmasterinfo</td>
  </tr>
  <tr>
    <td width="100px">zookeeper_url_prefix</td>
    <td width="150px">zookeeper的前缀路径名  </td>
    <td width="30px">是</td>
    <td width="50px">/disconfserver2</td>
  </tr>
  <tr>
    <td width="100px">local_dowload_dir</td>
    <td width="150px">下载文件夹, 远程文件下载后会放在这里</td>
    <td width="30px">是</td>
    <td width="50px">./disconf/download</td>
  </tr>
</table>

## 局限性和注意事项 ##
        
[局限性和注意事项](局限性和注意事项.html)

## 异构系统主备控制实现

disconf将会为所有配置提供主备功能的开关，对于一个配置，多台实例机器可以进行竞争成为主机（使用主配置），竞争失败的实例将会成为备机（使用备配置）。基于zookeeper提供的分布式一致性锁，可以非常容易的达到此目的。

	  	