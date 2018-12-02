package com.party.game.admin.web.interceptor;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Description: spring容器初始化完成后自动执行的类
 * @Author: yangshoukun
 * @Date: 2018/4/2 14:40
 */
@Component
public class TXZApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }
}

