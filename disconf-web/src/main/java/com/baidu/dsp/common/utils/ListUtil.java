package com.baidu.dsp.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

import com.baidu.dsp.common.form.RequestListBase.Page;

public class ListUtil {
    public static <T> List<T> getSubList(List<T> list, int offset, int count) {
        if (offset < 0) {
            offset = 0;
        }
        if (list == null || list.size() - 1 < offset) {
            return new ArrayList<T>();
        }
        int end = (list.size() > offset + count) ? offset + count : list.size();
        return list.subList(offset, end);
    }

    public static int calcOffset(Page page) {
        return (page.getPageNo() - 1) * page.getPageSize();
    }

    public static <K, V> void insertIntoMap(Map<K, V> map, List<K> keys, V value) {
        if (map != null && keys != null) {
            for (K key : keys) {
                map.put(key, value);
            }
        }
    }

    public static boolean allNull(Object first, Object... objects) {
        if (first != null) {
            return false;
        } else if (ArrayUtils.isEmpty(objects)) {
            return true;
        }
        for (Object o : objects) {
            if (o != null) {
                return false;
            }
        }
        return true;
    }

    public static boolean allNotNull(Object first, Object... objects) {
        if (first == null) {
            return false;
        } else if (ArrayUtils.isEmpty(objects)) {
            return true;
        }
        for (Object o : objects) {
            if (o == null) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasNull(Object first, Object... objects) {
        if (first == null) {
            return true;
        } else if (ArrayUtils.isEmpty(objects)) {
            return false;
        }
        for (Object o : objects) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasNotNull(Object first, Object... objects) {
        if (first != null) {
            return true;
        } else if (ArrayUtils.isEmpty(objects)) {
            return false;
        }
        for (Object o : objects) {
            if (o != null) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean in(T value, T... list) {
        if (ArrayUtils.isEmpty(list)) {
            return false;
        }
        for (T t : list) {
            if (ObjectUtils.equals(value, t)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean notIn(T value, T... list) {
        return !in(value, list);
    }
}
