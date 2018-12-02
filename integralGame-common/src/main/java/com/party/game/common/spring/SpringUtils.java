package com.party.game.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author yifeng
 * @version 2016/8/1
 */
@Component
public class SpringUtils implements ApplicationContextAware{
    private static ApplicationContext applicationContext;

    public SpringUtils() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }
}
