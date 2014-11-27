package com.baidu.disconf.web.tasks.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baidu.disconf.web.tasks.IMonitorService;

/**
 * http://blog.csdn.net/sd4000784/article/details/7745947 <br/>
 * http://blog.sina.com.cn/s/blog_6925c03c0101d1hi.html
 * 
 * @author knightliao
 * 
 */
@Component
public class MonitorServiceImpl implements IMonitorService {

    protected static final Logger LOG = LoggerFactory.getLogger(MonitorServiceImpl.class);

    // 每天中午十二点触发
    @Scheduled(cron = "0 0 12  * * ? ")
    @Override
    public void myTest() {
    }

}
