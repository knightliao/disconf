package com.baidu.disconf.core.test.path;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.core.common.path.DisconfWebPathMgr;
import com.baidu.disconf.core.test.common.BaseCoreTestCase;

/**
 * @author liaoqiqi
 * @version 2014-6-16
 */
public class DisconfWebPathMgrTestCase extends BaseCoreTestCase {

    @Test
    public void getZooPrefixUrlTest() {

        String result = DisconfWebPathMgr.getZooPrefixUrl("/test");

        Assert.assertEquals("/test/prefix", result);
    }

    @Test
    public void getZooHostsUrlTest() {

        String result = DisconfWebPathMgr.getZooHostsUrl("/test");

        Assert.assertEquals("/test/hosts", result);
    }

    @Test
    public void getRemoteUrlParameterTest() {

        String fileUrl =
                DisconfWebPathMgr.getRemoteUrlParameter("test", "app", "version", "env", "key", DisConfigTypeEnum.FILE);
        System.out.println(fileUrl);
        Assert.assertEquals("test/file?version=version&app=app&env=env&key=key&type=0", fileUrl);

        String itemUrl =
                DisconfWebPathMgr.getRemoteUrlParameter("test", "app", "version", "env", "key", DisConfigTypeEnum.ITEM);
        System.out.println(fileUrl);
        Assert.assertEquals("test/item?version=version&app=app&env=env&key=key&type=1", itemUrl);
    }
}
