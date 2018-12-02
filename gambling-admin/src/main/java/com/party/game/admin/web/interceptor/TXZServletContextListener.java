package com.party.game.admin.web.interceptor;

import com.party.game.common.spring.SpringUtils;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @Description: 容器关闭时, 销毁定时任务线程
 * @Author: yangshoukun
 * @Date: 2018/7/25 9:50
 */
public class TXZServletContextListener implements ServletContextListener {

    private static Logger logger = LoggerFactory.getLogger(TXZServletContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            // Get a reference to the Scheduler and shut it down
            Scheduler scheduler = (Scheduler) SpringUtils.getBean("scheduler");
            scheduler.shutdown(true);
            // Sleep for a bit so that we don't get any errors
            Thread.sleep(1000);
            logger.info("admin scheduler destroyed successfully");
        } catch (Exception e) {
            logger.error("an error occurred when destroying admin scheduler", e);
        }
    }
}
