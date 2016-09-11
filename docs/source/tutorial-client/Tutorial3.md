Tutorial 3 注解式分布式的配置项（最佳实践）
=======

在 [Tutorial 1](Tutorial1.html) 里, 
我们实现了一个简单的Redis服务程序，它使用注解式的分布式配置进行管理，此Redis的配置文件存储在分布式服务器 disconf-web 上。

那如果你的配置只是一个变量，不是配置文件，怎么办？还能实现分布式配置吗？

答案当然是肯定的！

Disconf支持配置项(配置项是指 一个类的某个域变量)的分布式化。

这里以 disconf-demo 某个程序片段为例，详细介绍了 分布式的配置项 的简单示例程序。

在这里，我们将分两种情况来进行演示：

1. 配置项在某个配置类里：外部程序通过配置类的get`***`方法获取。
2. 配置项在某个Service类里。Service通过本身类的的get`***`方法获取。

对于这两种方式，Disconf的后台实现方式上有所不同。这里不会讲述具体原因。但是Disconf做到了兼容性，以上两种方式均支持。

## 配置类里的配置项 ##

### 第一步：撰写 配置项类 ###

这里假设有一个金融系数类，它有一个折扣率变量。

    package com.example.disconf.demo.config;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;
    import com.baidu.disconf.client.common.annotations.DisconfFile;
    import com.baidu.disconf.client.common.annotations.DisconfFileItem;
    import com.baidu.disconf.client.common.annotations.DisconfItem;

    
    /**
     * 金融系数文件
     * 
     **/
    @Service
    @DisconfFile(filename = "coefficients.properties")
    public class Coefficients {
    
        public static final String key = "discountRate";
    
        @Value(value = "2.0d")
        private Double discount;
    

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


**具体步骤是：**

1. 编写Coefficients类，添加域 discount
2. 用Eclipse为域discount添加 get & set方法
3. 为get方法添加注解 @DisconfItem(key = key) ，这里的key是分布式配置项的标识，这里是 discountRate
4. 此类必须是JavaBean，Spring托管的，且 "scope" 都必须是singleton的。
5. 可以使用@Value为它设置一个默认值。

### 第二步：使用此分布式配置项 ###

撰写一个Service类，它使用 第二步里的 discountRate 变量，完整的类是：

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
    
        protected static final Logger LOGGER = LoggerFactory
                .getLogger(BaoBaoService.class);
    
        @Autowired
        private Coefficients coefficients;
    
        /**
         * 
         * 
         * @return
         */
        public double calcMoney() {
            return 10000 * coefficients.getDiscount();
        }
    
    }

calcMoney()会调用 coefficients.getDiscount() 获取折扣率 来计算 真正的money.

### 第三步：配置项更新回调类 ###

当配置项更新时，你的服务程序自动就会获取最新的配置项数据（不需要写回调函数，因为这里不像Redis这种较“重”的服务，这里的配置是实时生效的）。

但是，如果当你的配置项更新时，配置项本身被更新后，可能还会其它类依赖此配置项的更新，那么，你需要撰写一个回调类来获取此通知。

为了简单，这里我们以  [Tutorial 2](Tutorial2.html) 里的 SimpleRedisServiceUpdateCallback 类 为基础，进行扩展。

假设，当此配置项被更新时，Redis也需要重新被reload，那么，你可以这样来改写 SimpleRedisServiceUpdateCallback 类，完整的代码如下：

    package com.example.disconf.demo.service.callbacks;
    
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    
    import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
    import com.baidu.disconf.client.common.update.IDisconfUpdate;
    import com.example.disconf.demo.config.Coefficients;
    import com.example.disconf.demo.config.JedisConfig;
    
    /**
     * 更新Redis配置时的回调函数
     * 
     * @author liaoqiqi
     * @version 2014-6-17
     */
    @Service
    @DisconfUpdateService(classes = { JedisConfig.class }, itemKeys = { Coefficients.key })
    public class SimpleRedisServiceUpdateCallback implements IDisconfUpdate {
    
        protected static final Logger LOGGER = LoggerFactory
                .getLogger(SimpleRedisServiceUpdateCallback.class);
    
        @Autowired
        private SimpleRedisService simpleRedisService;
    
        /**
         * 
         */
        public void reload() throws Exception {
    
            simpleRedisService.changeJedis();
        }
    
    }

这里通过为注解 @DisconfUpdateService 添加一个 itemKeys: Coefficients.key ，就实现了配置项更新的通知。怎么样？是不是很强大？

### 第四步：在`disconf-web`上上传配置 ###

上传方式是先在首页点击 新建配置项

![](http://ww3.sinaimg.cn/mw1024/60c9620fjw1em9mstdddrj20ts04ojru.jpg)

然后新建就行啦

![](http://ww4.sinaimg.cn/mw1024/60c9620fjw1em9mutbw50j20q00fbdgk.jpg)

### 完结 ###

通过几行简单的配置，分布式配置项 就这样添加到你的应用程序里了。

## Service类的配置项 ##

在上一节里，我们阐述了如何在 配置项类 里添加一个配置项的方法。

在本节里，我们将在上一部分的基础上，阐述如何实现 不创建配置项类 就可以 实现 分布式配置项 的方法。

### 准备 ###

在 分布式配置服务器 disconf-web 上添加 moneyInvest 和 discountRate 配置项值。

### 第一步：撰写 含有 配置项 的Service类 ###

在上一节里，我们撰写了一个 Coefficients.java 类，它含有 分布式配置项 discountRate，BaoBaoService.java 则是一个使用 discountRate 的服务。BaoBaoService.java 在计算（calcMoney）时，使用了固定值 10000.

在本节里，我们 将 10000 这个值动态化，标注为分布式配置项。

完整的类是： 

    package com.example.disconf.demo.service;
    
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;
    
    import com.baidu.disconf.client.common.annotations.DisconfItem;
    import com.example.disconf.demo.config.Coefficients;
    
    /**
     * 
     * @author liaoqiqi
     * @version 2014-5-16
     */
    @Service
    public class BaoBaoService {
    
        protected static final Logger LOGGER = LoggerFactory
                .getLogger(BaoBaoService.class);
    
        public static final String key = "moneyInvest";
    
        private Double moneyInvest = 1000d;
    
        @Autowired
        private Coefficients coefficients;
    
        /**
         * 
         * @return
         */
        public double calcMoney() {
            return coefficients.getDiscount()
                    * getMoneyInvest();
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

**具体实现步骤是：**

1. 添加域 moneyInvest ，并使用Eclipse自动生成 get&set 方法。
2. 为get方法添加 @DisconfItem 注解，并添加 key 为 moneyInvest
3. 在函数 calcMoney() 里 调用本身类的 getMoneyInvest() 方法。
4. 此类必须是JavaBean，Spring托管的，且 "scope" 都必须是singleton的。

### 第二步：在`disconf-web`上上传配置 ###

这里不再赘述。

### 完结 ###

只需要上面一步，就完成了分布式配置项。

配置更新也是实时的，不需要写回调函数。

service类的配置项 其实和 配置项类的配置项 撰写方法 差不多。它的好处是不需要再新建一个配置项类。
