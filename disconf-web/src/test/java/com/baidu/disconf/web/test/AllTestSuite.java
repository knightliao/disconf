package com.baidu.disconf.web.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.baidu.disconf.web.test.service.config.dao.ConfigDaoTestCase;

/**
 * 
 * @author liaoqiqi
 * @version 2014-1-14
 */
@RunWith(Suite.class)
@SuiteClasses({ ConfigDaoTestCase.class })
public class AllTestSuite {

}
