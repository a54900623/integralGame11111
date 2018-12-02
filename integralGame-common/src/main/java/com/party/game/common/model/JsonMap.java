package com.party.game.common.model;

import java.util.HashMap;
import java.util.Map;

/**
 *  同EnumMap，只是标记其存储数据库转为json存储
 *
 * @author changjiang.tang Date:8/19/15 Time:3:08 PM
 */
public class JsonMap<K, V> extends HashMap<K, V> {

    private static final long serialVersionUID = 2126339960826320940L;

    public JsonMap(Map<K, ? extends V> m) {
        super(m);
    }

    public JsonMap() {
    }

}
