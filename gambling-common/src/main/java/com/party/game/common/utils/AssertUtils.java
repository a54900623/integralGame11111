package com.party.game.common.utils;

import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * 断言工具
 *
 * @author wenqiang.luo date:15-8-20
 */
public abstract class AssertUtils {

    /**
     * 断言对象是否为空，为空抛出异常
     *
     * @author wenqiang.luo 2015-08-21
     * @param object 断言对象
     * @see java.lang.IllegalArgumentException
     */
    public static void notNull(Object object) {
        notNull(object,
                "[Assertion failed] - this argument must not be null");
    }

    /**
     * 断言对象是否为空，为空抛出异常
     *
     * @author wenqiang.luo 2015-08-21
     * @param object 断言对象
     * @param message 异常信息
     * @see java.lang.IllegalArgumentException
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言字符列是否为空字符，为空字符抛出异常
     * @param charSequence 字符序列
     */
    public static void notEmpty(CharSequence charSequence) {
        notNull(charSequence,
                "[Assertion failed] - this argument must not be empty");
    }

    /**
     * 断言字符列是否为空字符，为空字符抛出异常
     * @param charSequence 字符序列
     * @param message 异常信息
     */
    public static void notEmpty(CharSequence charSequence, String message) {
        notNull(charSequence, message);

        if (charSequence.length() == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言列表是否为空，为空抛出异常
     * @param collection 列表集合
     */
    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection,
                "[Assertion failed] - this argument must not be empty");
    }

    /**
     * 断言列表是否为空，为空抛出异常
     * @param collection 列表集合
     * @param message 异常信息
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }
}
