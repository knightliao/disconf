package com.baidu.utils;

import java.lang.reflect.Field;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-11
 */
public class ClassUtils {

    /**
     * 由Get Method名称获取Field名
     * 
     * @return
     */
    public static String getFieldNameByGetMethodName(String methodName) {

        int prefixIndex = 0;

        // 必须以get或is开始的
        if (methodName.startsWith("get")) {
            prefixIndex = 3;

        } else if (methodName.startsWith("is")) {

            prefixIndex = 2;

        } else {

            return null;
        }

        String fieldName = methodName.substring(prefixIndex);
        if (fieldName.length() >= 1) {
            String firstCharStr = String.valueOf(fieldName.charAt(0))
                    .toLowerCase();
            if (fieldName.length() > 1) {
                fieldName = firstCharStr + fieldName.substring(1);
            } else {
                fieldName = firstCharStr.toLowerCase();
            }
        }

        return fieldName;
    }

    /**
     * 根据Field类型设置值
     * 
     * @param field
     */
    public static void setFieldValeByType(Field field, Object obj, String value)
            throws Exception {

        Class<?> type = field.getType();

        String typeName = type.getName();

        if (typeName.equals("int")) {

            if (value.equals("")) {
                value = "0";
            }
            field.set(obj, Integer.valueOf(value));

        } else if (typeName.equals("long")) {

            if (value.equals("")) {
                value = "0";
            }
            field.set(obj, Long.valueOf(value));

        } else if (typeName.equals("boolean")) {

            if (value.equals("")) {
                value = "false";
            }
            field.set(obj, Boolean.valueOf(value));

        } else if (typeName.equals("double")) {

            if (value.equals("")) {
                value = "0.0";
            }
            field.set(obj, Double.valueOf(value));

        } else {

            field.set(obj, value);
        }
    }

    /**
     * 根据Field类型返回值
     * 
     * @param field
     */
    public static Object getValeByType(Class<?> type, String value)
            throws Exception {

        String typeName = type.getName();
        typeName = typeName.toLowerCase();

        if (typeName.equals("int") || typeName.equals("java.lang.int")) {

            if (value.equals("")) {
                value = "0";
            }

            return Integer.valueOf(value);

        } else if (typeName.equals("long") || typeName.equals("java.lang.long")) {

            if (value.equals("")) {
                value = "0";
            }

            return Long.valueOf(value);

        } else if (typeName.equals("boolean")
                || typeName.equals("java.lang.boolean")) {

            if (value.equals("")) {
                value = "false";
            }

            return Boolean.valueOf(value);

        } else if (typeName.equals("double")
                || typeName.equals("java.lang.double")) {

            if (value.equals("")) {
                value = "0.0";
            }

            return Double.valueOf(value);

        } else {

            return value;
        }
    }

    /**
     * 
     * @param className
     * @param methodName
     * @throws Exception
     */
    public static void addAnotation4Method(Annotation anntation,
            String className, String methodName) throws Exception {

        // pool creation
        ClassPool pool = ClassPool.getDefault();

        // extracting the class
        CtClass cc = pool.getCtClass(className);

        // looking for the method to apply the annotation on
        CtMethod sayHelloMethodDescriptor = cc.getDeclaredMethod(methodName);

        // create the annotation
        ClassFile ccFile = cc.getClassFile();
        ConstPool constpool = ccFile.getConstPool();
        AnnotationsAttribute attr = new AnnotationsAttribute(constpool,
                AnnotationsAttribute.visibleTag);

        //
        attr.addAnnotation(anntation);
        // add the annotation to the method descriptor
        sayHelloMethodDescriptor.getMethodInfo().addAttribute(attr);

        // transform the ctClass to java class
        Class dynamiqueBeanClass = cc.toClass();

    }
}
