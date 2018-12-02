package com.party.game.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;


/**
 * @author yifeng
 * @version 2016/8/1
 */
public class SpringContextUtils {

    public static Object getBean(String beanName) throws BeansException {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return wac.getBean(beanName);
    }
}
