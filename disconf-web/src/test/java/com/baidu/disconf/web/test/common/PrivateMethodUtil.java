package com.baidu.disconf.web.test.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.springframework.aop.TargetSource;

/**
 * 
 * @author liaoqiqi
 * @version 2014-1-26
 */
public final class PrivateMethodUtil {

    private PrivateMethodUtil() {
    }

    /**
     * spring注入对象的私有方法调用
     * 
     * @param owner
     *            注入的对象
     * @param methodName
     *            私有方法名
     * @param parameters
     *            私有方法参数
     * @return 私有方法返回值
     */
    @SuppressWarnings("rawtypes")
    public static Object invokeMethod(final Object owner,
            final String methodName, final Object... parameters)
            throws Exception {

        Class[] parameterTypes = null;

        if (parameters != null) {

            parameterTypes = new Class[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                parameterTypes[i] = parameters[i].getClass();
            }
        }

        return invokeMethod(owner, methodName, parameterTypes, parameters);
    }

    /**
     * spring注入对象的私有方法调用
     * 
     * @param owner
     *            注入的对象
     * @param methodName
     *            私有方法名
     * @param parameterTypes
     *            私有方法参数类型
     * @param parameters
     *            私有方法参数
     * @return 私有方法返回值
     */
    @SuppressWarnings({ "rawtypes" })
    public static Object invokeMethod(final Object owner,
            final String methodName, final Class[] parameterTypes,
            final Object[] parameters) throws Exception {

        // get class
        final Class ownerclass = owner.getClass();

        // get property
        try {

            @SuppressWarnings("unchecked")
            final Method getTargetClass = ownerclass
                    .getMethod("getTargetSource");
            final TargetSource target = (TargetSource) getTargetClass.invoke(
                    owner, new Object[] {});
            final Class targetClass = target.getTargetClass();
            @SuppressWarnings("unchecked")
            final Method method = targetClass.getDeclaredMethod(methodName,
                    parameterTypes);

            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            final Object targetInstance = target.getTarget();
            return method.invoke(targetInstance, parameters);

        } catch (NoSuchMethodException e) {

            return invokeMethod(owner, 0, methodName, parameterTypes,
                    parameters);
            // e.printStackTrace();
        }
    }

    /**
     * 普通对象私有方法调用
     * 
     * @param owner
     *            : target object
     * @param classLevel
     *            : 0 means itself, 1 means it's father, and so on...
     * @param methodName
     *            : name of the target method
     * @param parameterTypes
     *            : types of the target method's parameters
     * @param parameters
     *            : parameters of the target method
     * @return result of invoked method
     */
    @SuppressWarnings("rawtypes")
    public static Object invokeMethod(final Object owner, final int classLevel,
            final String methodName, final Class[] parameterTypes,
            final Object[] parameters) throws Exception {

        // get class
        final Class ownerclass = getOwnerclass(owner, classLevel);

        // get property
        @SuppressWarnings("unchecked")
        final Method method = ownerclass.getDeclaredMethod(methodName,
                parameterTypes);
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return method.invoke(owner, parameters);
    }

    /**
     * 设置私有成员
     * 
     * @param owner
     *            : target object
     * @param classLevel
     *            : 0 means itself, 1 means it's father, and so on...
     * @param fieldName
     *            : name of the target field
     * @param value
     *            : new value of the target field
     */
    @SuppressWarnings("rawtypes")
    public static void setObjectProperty(final Object owner,
            final int classLevel, final String fieldName, final Object value)
            throws Exception {
        // get class
        final Class ownerclass = getOwnerclass(owner, classLevel);

        // get property
        final Field field = ownerclass.getDeclaredField(fieldName);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(owner, value);
    }

    /**
     * 获取私有成员
     * 
     * @param owner
     *            : target object
     * @param classLevel
     *            : 0 means itself, 1 means it's father, and so on...
     * @param fieldName
     *            : name of the target field
     * @return value of the target field
     */
    @SuppressWarnings("rawtypes")
    public static Object getObjectProperty(final Object owner,
            final int classLevel, final String fieldName) throws Exception {
        // get class
        final Class ownerclass = getOwnerclass(owner, classLevel);

        // get property

        final Field field = ownerclass.getDeclaredField(fieldName);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return field.get(owner);
    }

    @SuppressWarnings("rawtypes")
    private static Class getOwnerclass(final Object owner, final int classLevel) {
        Class ownerclass = owner.getClass();
        for (int i = 0; i < classLevel; i++) {
            ownerclass = ownerclass.getSuperclass();
        }
        return ownerclass;
    }

}
