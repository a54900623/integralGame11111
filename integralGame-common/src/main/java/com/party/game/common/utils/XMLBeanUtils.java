package com.party.game.common.utils;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.util.Map;


/**
 * XML与JavaBean相互转换工具类
 */
public final class XMLBeanUtils {
    /**
     * 将Bean转换为XML 
     *
     * @param clazzMap 别名-类名映射Map 
     * @param bean     要转换为xml的bean对象 
     * @return XML字符串
     */
    public static String bean2xml(Map<String, Class> clazzMap, Object bean) {
        XStream xstream = getXStreamObject(clazzMap);
        return xstream.toXML(bean);
    }

    /**
     * 将XML转换为Bean 
     *
     * @param clazzMap 别名-类名映射Map 
     * @param xml      要转换为bean对象的xml字符串 
     * @return Java Bean对象 
     */
    public static Object xml2Bean(Map<String, Class> clazzMap, String xml) {
        XStream xstream = getXStreamObject(clazzMap);
        return xstream.fromXML(xml);
    }

    /**
     * 获取XStream对象 
     *
     * @param clazzMap 别名-类名映射Map 
     * @return XStream对象
     */
    public static XStream getXStreamObject(Map<String, Class> clazzMap) {
        XStream xstream = new XStream(new DomDriver("UTF-8", new NoNameCoder()));
        xstream.autodetectAnnotations(true);
        xstream.ignoreUnknownElements();

        for (Map.Entry<String, Class> entry : clazzMap.entrySet()) {
            xstream.alias(entry.getKey(), entry.getValue());
        }

        return xstream;
    }
}