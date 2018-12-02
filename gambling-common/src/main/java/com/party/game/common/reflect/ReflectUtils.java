package com.party.game.common.reflect;

import com.google.common.base.Strings;
import com.party.game.common.spring.SpringUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * 反射辅助工具
 * Created by wei.li
 *
 * @date 2017/4/12 0012
 * @time 14:34
 */
public class ReflectUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

    /**
     * 执行方法
     * @param className 类名
     * @param method 方法名
     */
    public static void  invokMethod(String className, String method){
        Object object = null;
        if (!Strings.isNullOrEmpty(className)){
            object = SpringUtils.getBean(className);
        }

        if (null == object){
            logger.error("反射执行方法异常,{}类不存在", className);
            return;
        }

        Class clazz = object.getClass();
        Method m = null;
        try {
            m = clazz.getDeclaredMethod(method);
        } catch (NoSuchMethodException e) {
            logger.error("反射执行方法异常，{}方法不存在", method, e.getMessage());
        }

        if (m != null){
            try {
                m.invoke(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行方法
     * @param className 类名
     * @param method 方法名
     * @param params 参数
     */
    public static void  invokeMethod(String className, String method, Object[] params){
        if (params == null || params.length == 0) {
            invokMethod(className, method);
            return;
        }
        Object object = null;
        if (!Strings.isNullOrEmpty(className)){
            object = SpringUtils.getBean(className);
        }

        if (null == object){
            logger.error("反射执行方法异常,{}类不存在", className);
            return;
        }

        Class clazz = object.getClass();
        Method m = null;
        try {
            Class[] classes = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                classes[i] = params[i].getClass();
            }
            m = clazz.getDeclaredMethod(method, classes);
        } catch (NoSuchMethodException e) {
            logger.error("反射执行方法异常，{}方法不存在", method, e.getMessage());
        }

        if (m != null){
            try {
                m.invoke(object, params);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static Set<Field> getAllField(Class<?> clazz) {
        Set<Field> fieldSet = new HashSet<Field>();
        while (clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                int modifier = field.getModifiers();
                if (Modifier.isFinal(modifier) || Modifier.isStatic(modifier)) {
                    continue;
                }
                fieldSet.add(field);
            }
            clazz = clazz.getSuperclass();
        }
        return fieldSet;
    }

    public static String getKey(Field field){
        String key = field.getName();
        XStreamAlias xStreamAlias = field.getAnnotation(XStreamAlias.class);
        if (null != xStreamAlias){
            key = xStreamAlias.value();
        }
        return key;
    }
}
