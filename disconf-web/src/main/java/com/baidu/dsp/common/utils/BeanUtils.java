package com.baidu.dsp.common.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 反射的Utils函数集合.
 * <p/>
 * 提供侵犯隐私的直接读取filed的能力.
 */
public class BeanUtils {

    protected static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    private static final String ENTITY_CLASS_PACKAGE = "com.baidu.dsp";

    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     */
    public static Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException {
        Field field = getDeclaredField(object, fieldName);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常{}", e.getMessage());
        }
        return result;
    }

    /**
     * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
     */
    public static void setFieldValue(Object object, String fieldName, Object value) throws NoSuchFieldException {
        Field field = getDeclaredField(object, fieldName);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }
    }

    /**
     * 循环向上转型,获取对象的DeclaredField.
     */
    private static Field getDeclaredField(Object object, String fieldName) throws NoSuchFieldException {
        Assert.notNull(object);
        return getDeclaredField(object.getClass(), fieldName);
    }

    /**
     * 循环向上转型,获取类的DeclaredField.
     */
    @SuppressWarnings("rawtypes")
    private static Field getDeclaredField(Class clazz, String fieldName) throws NoSuchFieldException {
        Assert.notNull(clazz);
        Assert.hasText(fieldName);
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
            }
        }
        throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + fieldName);
    }

    /**
     * 获得输入对象的两层镜象实例，要求参数类必需包含一个无参的public构造函数
     * <p/>
     * 两层镜像：如果输入对象的某个属性是系统的实体或实体的集合则clone该实体或集合里面的每个实体，且只是两层clone不会再次嵌套
     * 实体：com.baidu.dsp.*.*.java 实体集合：目前支持的集合为ArrayList HashSet
     *
     * @param source
     *
     * @return
     *
     * @author guojichun
     * @since 1.0.0
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Object getClone(Object source) {
        if (null == source) {
            return null;
        }
        Object target = BeanUtils.getOneLayerClone(source);

        try {
            // 此处使用了JAVA的内省机制（内省是 Java 语言对 Bean 类属性、事件的一种缺省处理方法）
            // 获得对像的BeanInfo 信息：
            BeanInfo bi = Introspector.getBeanInfo(source.getClass());
            // 获得属性的描述器
            PropertyDescriptor[] props = bi.getPropertyDescriptors();
            for (int i = 0; i < props.length; ++i) {
                if (null != props[i].getReadMethod()) {
                    Class propertyType = props[i].getPropertyType();
                    if (propertyType.equals(List.class)) {
                        List newList = new ArrayList();
                        // 此处使用了JAVA的反射机制通过获得方法的引用去执行方法，其中source:为要执行的方法所属的对象，
                        // 方法如果没有参数第二个参数则为null;
                        List valueList = (List) props[i].getReadMethod().invoke(source);
                        if (valueList == null) {
                            valueList = new ArrayList();
                        }
                        for (Object value : valueList) {
                            Object cloneValue = null;
                            if (value.getClass().getName().indexOf(ENTITY_CLASS_PACKAGE) >= 0) {
                                cloneValue = BeanUtils.getOneLayerClone(value);
                            } else {
                                cloneValue = value;
                            }
                            newList.add(cloneValue);
                        }
                        props[i].getWriteMethod().invoke(target, newList);
                    }
                    if (propertyType.equals(Set.class)) {
                        Set newSet = new HashSet();
                        Set valueSet = (Set) props[i].getReadMethod().invoke(source);
                        if (valueSet == null) {
                            valueSet = new HashSet();
                        }
                        for (Object value : valueSet) {
                            Object cloneValue = BeanUtils.getOneLayerClone(value);
                            newSet.add(cloneValue);
                        }
                        props[i].getWriteMethod().invoke(target, newSet);
                    } else {
                        // 如果是array 跳过 //FIXME
                        if (propertyType.equals(Arrays.class)) {
                            continue;
                        }
                        if (propertyType.toString().startsWith(ENTITY_CLASS_PACKAGE)) {
                            Object value = props[i].getReadMethod().invoke(source);
                            Object cloneValue = BeanUtils.getOneLayerClone(value);
                            props[i].getWriteMethod().invoke(target, cloneValue);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("clone object exception object class:" + source.getClass(), e);
        }
        return target;
    }

    /**
     * 获得输入对象的简单一层镜象实例，要求参数类必需包含一个无参的public构造函数
     *
     * @param source
     *
     * @return
     *
     * @author guojichun
     * @since 1.0.0
     */
    @SuppressWarnings("rawtypes")
    private static Object getOneLayerClone(Object source) {
        if (null == source) {
            return null;
        }
        Object target = null;
        try {
            Constructor con = source.getClass().getConstructor();
            target = con.newInstance();
            // 把一个集合类中的数据拷贝到另一集合类中
            PropertyUtils.copyProperties(target, source);

        } catch (Exception e) {
            logger.error("clone object exception object class:" + source.getClass(), e);
        }
        return target;
    }

    /**
     * 获得输入对象列表的镜象实例列表，要求参数类必需包含一个无参的public构造函数
     *
     * @param sourceList
     *
     * @return
     *
     * @author ying
     * @since 1.0.0
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List getCloneList(List sourceList) {
        if (null == sourceList) {
            return null;
        }
        List targetList = new ArrayList();
        for (int i = 0; i < sourceList.size(); i++) {
            Object target = null;
            Object source = sourceList.get(i);
            try {
                Constructor con = source.getClass().getConstructor();
                target = con.newInstance();
                PropertyUtils.copyProperties(target, source);
            } catch (Exception e) {
                logger.error("clone object exception object class:" + source.getClass(), e);
            }
            targetList.add(target);
        }
        return targetList;
    }

}
