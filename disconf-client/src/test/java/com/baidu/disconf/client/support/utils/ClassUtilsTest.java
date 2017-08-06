package com.baidu.disconf.client.support.utils;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ClassUtilsTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testGetValeByType() throws Exception {
		assertTrue(ClassUtils.getValeByType(Integer.class, "10086").equals(new Integer(10086)));
		assertTrue(ClassUtils.getValeByType(Integer.class, "").equals(new Integer(0)));
		assertTrue(ClassUtils.getValeByType(Integer.class, 0).equals(new Integer(0)));
		assertTrue(ClassUtils.getValeByType(Long.class, "1688").equals(new Long(1688)));
		assertTrue(ClassUtils.getValeByType(Long.class, "").equals(new Long(0)));
		assertTrue(ClassUtils.getValeByType(Boolean.class, "true").equals(new Boolean(true)));
		assertTrue(ClassUtils.getValeByType(Boolean.class, "").equals(new Boolean(false)));
		assertTrue(ClassUtils.getValeByType(Double.class, "234.4").equals(new Double(234.4)));
		assertTrue(ClassUtils.getValeByType(Double.class, "").equals(new Double(0.0)));
		assertTrue(ClassUtils.getValeByType(String.class, "234.4").equals(String.valueOf(234.4)));
		List<Integer> list = (List<Integer>) ClassUtils.getValeByType(List.class, "[100,200,300]");
		assertTrue(list.contains(new Double(100)));
		Map<String, String> map = (Map<String, String>) ClassUtils.getValeByType(Map.class, "{\"name\":\"zxh\",\"city\":\"shenzhen\"}");
		assertTrue(map.get("name").equals("zxh"));
		assertTrue(ClassUtils.getValeByType(List.class, "234.4,123") == null);
	}

}
