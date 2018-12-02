package com.party.game.common.utils;

import java.math.BigDecimal;

/**
 * 数字精度工具类
 *
 * @author changjiang.tang Date:8/19/15 Time:2:24 PM
 * @author wenqiang.luo date: 15-9-17 增加数字精准运算方法
 */
public class BigDecimalUtils {

    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 5;

    /**
     * 私有构造方法，禁用new创建对象
     */
    private BigDecimalUtils() {

    }

    /**
     * 保留两位小数
     * @param number 数值
     * @return 数值，精确到小数点后两位
     */
    public static BigDecimal saleUp2(BigDecimal number) {
        return number == null ? null : number.setScale(2, BigDecimal.ROUND_UP);
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     *
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static float add(float v1, float v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).floatValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static float sub(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).floatValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    public static double div(float v1, float v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static float round(float v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 将浮点数放大
     *
     * @param v 需放大的数字
     * @param n 10的幂数
     * @return 放大后的结果
     */
    public static double amplify(double v, int n) {
        BigDecimal b = new BigDecimal(Double.toString(v));
        return b.movePointRight(n).doubleValue();
    }

    /**
     * 将浮点数放大后取整
     *
     * @param v 需放大的数字
     * @param n 10的幂数
     * @return 放大后取整的结果
     */
    public static long amplify2long(double v, int n) {
        BigDecimal b = new BigDecimal(Double.toString(v));
        return b.movePointRight(n).longValue();
    }

    /**
     * 将浮点数放大后取整
     *
     * @param v 需放大的数字
     * @param n 10的幂数
     * @return 放大后取整的结果
     */
    public static int amplify2int(double v, int n) {
        BigDecimal b = new BigDecimal(Double.toString(v));
        return b.movePointRight(n).intValue();
    }

    /**
     * 将浮点数缩小
     *
     * @param v 需缩小的数字
     * @param n 10的幂数
     * @return 缩小后的结果
     */
    public static double shrink(double v, int n) {
        BigDecimal b = new BigDecimal(Double.toString(v));
        return b.movePointLeft(n).doubleValue();
    }

    /**
     * 将整数数缩小成浮点数
     *
     * @param v 需缩小的数字
     * @param n 10的幂数
     * @return 缩小成浮点数的结果
     */
    public static double shrink2double(long v, int n) {
        BigDecimal b = new BigDecimal(Long.toString(v));
        return b.movePointLeft(n).doubleValue();
    }

    /**
     * 将整数数缩小成浮点数
     *
     * @param v 需缩小的数字
     * @param n 10的幂数
     * @return 缩小成浮点数的结果
     */
    public static double shrink2double(int v, int n) {
        BigDecimal b = new BigDecimal(Integer.toString(v));
        return b.movePointLeft(n).doubleValue();
    }

}
