package com.baidu.disconf.tool.context.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.tool.context.ContextMgr;
import com.github.knightliao.apollo.redis.RedisClient;

import junit.framework.Assert;
import junit.framework.TestCase;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

/**
 * @author : WuNing
 * @description : ContextMgrTestCase
 * @date : 2014年8月1日 下午5:30:00
 */
@RunWith(JMockit.class)
public class ContextMgrTestCase extends TestCase {

    static final Logger logger = LoggerFactory.getLogger(ContextMgrTestCase.class);

    @Before
    public void setUp() {
        logger.info("mockup initial begin");
        new MockUp<RedisClient>() {
            Map<String, String> redisStorage = new HashMap<String, String>();

            @Mock
            public boolean flushall() {
                logger.info("Redis Client Flush");
                return true;
            }

            @Mock
            public void hput(String key, String field, Serializable fieldValue) {
                redisStorage.put(key + "_" + field, fieldValue.toString());
            }

            @Mock
            public Object hget(String key, String field) {
                return redisStorage.get(key + "_" + field);
            }

        };

        logger.info("mockup initial done");
    }

    @Test
    public void testSaveLoadInt() {

        logger.info("begin to test save and load");

        ContextMgr mgr = new ContextMgr("127.0.0.1", 8000, "authkey", 300);

        // Int
        mgr.save("KEY", "INT_VAL", 10);
        int val = (Integer) mgr.load("KEY", "INT_VAL", Integer.class, 0);
        Assert.assertEquals(val, 10);

    }

    @Test
    public void testSaveLoadStr() {
        logger.info("begin to test save and load");

        ContextMgr mgr = new ContextMgr("127.0.0.1", 8000, "authkey", 300);

        // String
        String str = "IMPORT INFORMATION";
        mgr.save("KEY", "STR_VAL", str);
        String str2 = (String) mgr.load("KEY", "STR_VAL", String.class, "");
        Assert.assertEquals(str, str2);
    }

    @Test
    public void testSaveLoadComplexClass() {
        logger.info("begin to test save and load");

        ContextMgr mgr = new ContextMgr("127.0.0.1", 8000, "authkey", 300);

        ComplexClass complex = new ComplexClass();
        complex.intVal = 42;
        complex.doubleVal = Math.PI;
        complex.strList.add("Hello,world");
        complex.strList.add("Disconf rocks!");

        mgr.save("KEY", "COMPLEX_VAL", complex);
        ComplexClass complex2 = (ComplexClass) mgr.load("KEY", "COMPLEX_VAL", ComplexClass.class, null);

        Assert.assertTrue(complex.equals(complex2));

    }

    public static class ComplexClass {
        int intVal;
        double doubleVal;
        List<String> strList = new ArrayList<String>();

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ComplexClass other = (ComplexClass) obj;
            if (Double.doubleToLongBits(doubleVal) != Double.doubleToLongBits(other.doubleVal)) {
                return false;
            }
            if (intVal != other.intVal) {
                return false;
            }
            if (strList == null) {
                if (other.strList != null) {
                    return false;
                }
            } else if (!strList.equals(other.strList)) {
                return false;
            }
            return true;
        }

        public int getIntVal() {
            return intVal;
        }

        public void setIntVal(int intVal) {
            this.intVal = intVal;
        }

        public double getDoubleVal() {
            return doubleVal;
        }

        public void setDoubleVal(double doubleVal) {
            this.doubleVal = doubleVal;
        }

        public List<String> getStrList() {
            return strList;
        }

        public void setStrList(List<String> strList) {
            this.strList = strList;
        }
    }

}
