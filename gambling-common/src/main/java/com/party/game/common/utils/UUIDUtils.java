package com.party.game.common.utils;

import java.util.UUID;

/**
 * @author wenqiang.luo date:16/4/12
 */
public final class UUIDUtils {

    /**
     * 私有构造方法
     */
    private UUIDUtils() { }

    /**
     * 生成随机UUID序列
     * @return UUID字符串
     */
    public static String generateRandomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
