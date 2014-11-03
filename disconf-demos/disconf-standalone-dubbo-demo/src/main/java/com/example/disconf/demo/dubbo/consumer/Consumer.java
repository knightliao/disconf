package com.example.disconf.demo.dubbo.consumer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.example.disconf.demo.dubbo.service.DubboService;

public class Consumer {

    public static void main(String[] args) throws Exception {

        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext(new String[] { "dubbo-consumer.xml" });

        context.start();

        DubboService demoService = (DubboService) context.getBean("dubboService"); // 获取远程服务代理

        for (int i = 0; i < 100; ++i) {

            String hello = demoService.printWord("Dubbo Client: " + i); // 执行远程方法

            System.out.println(hello); // 显示调用结果

            Thread.sleep(1000);
        }
    }

}
