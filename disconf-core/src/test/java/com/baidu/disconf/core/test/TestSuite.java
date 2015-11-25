package com.baidu.disconf.core.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.baidu.disconf.core.test.path.DisconfWebPathMgrTestCase;
import com.baidu.disconf.core.test.path.ZooPathMgrTestCase;
import com.baidu.disconf.core.test.restful.RestfulMgrTestCase;
import com.baidu.disconf.core.test.utils.MyStringUtilsTestCase;
import com.baidu.disconf.core.test.zookeeper.ZookeeperMgrTest;

/**
 * @author liaoqiqi
 * @version 2014-7-30
 */
@RunWith(Suite.class)
@SuiteClasses({DisconfWebPathMgrTestCase.class, ZooPathMgrTestCase.class, RestfulMgrTestCase.class,
                  ZookeeperMgrTest.class, MyStringUtilsTestCase.class})
public class TestSuite {

}