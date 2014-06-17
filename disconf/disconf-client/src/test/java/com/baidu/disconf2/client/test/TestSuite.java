package com.baidu.disconf2.client.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.baidu.disconf2.client.test.config.ConfigMgrTestCase;
import com.baidu.disconf2.client.test.core.DisconfCoreMgrTestCase;
import com.baidu.disconf2.client.test.fetcher.RestfulMgrTestCase;
import com.baidu.disconf2.client.test.json.JsonTranslate;
import com.baidu.disconf2.client.test.scan.ScanMgrTestCase;
import com.baidu.disconf2.client.test.scan.inner.ScanPackTestCase;
import com.baidu.disconf2.client.test.watch.WatchMgrTestCase;

@RunWith(Suite.class)
@SuiteClasses({ ScanPackTestCase.class, DisconfMgrTestCase.class,
        WatchMgrTestCase.class, ScanMgrTestCase.class, JsonTranslate.class,
        RestfulMgrTestCase.class, DisconfCoreMgrTestCase.class,
        ConfigMgrTestCase.class })
public class TestSuite {

}