package com.baidu.disconf.web.test.common;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * 
 * @author liaoqiqi
 * @version 2014-1-14
 */
public class BaseTestBeforeClass extends AbstractTestExecutionListener {

    /**
     * The default implementation is <em>empty</em>. Can be overridden by
     * subclasses as necessary.
     */
    public void prepareTestInstance(TestContext testContext) throws Exception {

    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {

    }

}
