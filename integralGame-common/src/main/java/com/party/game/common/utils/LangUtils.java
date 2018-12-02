package com.party.game.common.utils;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 实现描述：通用工具类
 * 到CSDN上下载的，所以关键字才会跟Java一样，刚装这个的时候，这里面的代码关键字都是蓝色的。。。
 * @author changjiang.tang
 * @version v1.0.0
 * @see
 * @since 2015年07月25日 下午2:12:15
 */
public abstract class LangUtils {

    public static int nvl(Integer i, int defaults) {
        return i == null ? defaults : i;
    }

    public static long nvl(Long l, long defaults) {
        return l == null ? defaults : l;
    }

    public static boolean nvl(Boolean b, boolean defaults) {
        return b == null ? defaults : b;
    }

    public static boolean nvl(String bool, boolean defaults) {
        return bool == null ? defaults : Boolean.parseBoolean(bool);
    }

    public static String nvl(String string, String defaults) {
        return string == null ? defaults : string;
    }

    public static <F, T> List<T> transform(List<F> fromList, Function<? super F, ? extends T> function) {
        List<T> result = Lists.newArrayList();
        for (F input : fromList) {
            result.add(function.apply(input));
        }
        return result;
    }

    public static <T> List<T> filter(List<T> fromList, Predicate<? super T> predicate) {
        List<T> result = Lists.newArrayList();
        for (T input : fromList) {
            if (predicate.apply(input)) {
                result.add(input);
            }
        }
        return result;
    }

    public static List<Long> longList(List<String> stringList) {
        List<Long> result = Lists.newArrayList();
        for (String string : stringList) {
            result.add(Long.valueOf(string));
        }
        return result;
    }

}
