package com.yufusoft.payplatform.ccenter.mq.listener;

import com.yufusoft.payplatform.ccenter.mq.rocketmq.config.RocketMQProducerScanner;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.config.RocketMQXmlConsumerLoader;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.config.RocketMQXmlProducerLoader;
import com.yufusoft.payplatform.util.common.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;


@Component
public class SystemInitializeListener implements ApplicationListener<ContextRefreshedEvent> {
    private static Logger logger = LoggerFactory.getLogger(SystemInitializeListener.class);

    private static String RESOURCE_PATH = System.getProperty("resourceDir");

    @Autowired
    private RocketMQXmlProducerLoader rocketMQXmlProducerLoader;
    @Autowired
    private RocketMQXmlConsumerLoader rocketMQXmlConsumerLoader;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            System.setProperty("resourceDir", getResourcePath(event));
            rocketMQXmlProducerLoader.load();
            rocketMQXmlConsumerLoader.load();
            //startProducerScanner();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void startProducerScanner() {
        Thread producerScannerThread = new Thread(new RocketMQProducerScanner());
        producerScannerThread.setDaemon(true);
        producerScannerThread.start();
    }


    /**
     * 获取资源文件路径
     *
     * @param event
     * @return
     */
    private String getResourcePath(ContextRefreshedEvent event) {
        try {
            if (!StringUtil.isEmpty(RESOURCE_PATH)) {
                return RESOURCE_PATH;
            }
            if (event.getApplicationContext() instanceof ClassPathXmlApplicationContext) {
                Field field = AbstractRefreshableConfigApplicationContext.class.getDeclaredField("configLocations");
                field.setAccessible(true);
                String[] configLocations = (String[]) field.get(event.getSource());
                String location = configLocations[0];
                String[] locationStrArrays = location.split("/");
                for (int i = 0; i < locationStrArrays.length; i++) {
                    if (locationStrArrays[i].startsWith("file:")) {
                        location = location.replace(locationStrArrays[i], "");
                    }
                    if (locationStrArrays[i].endsWith(".xml")) {
                        location = location.replace(locationStrArrays[i], "");
                    }
                }
                return location;
            } else {
                return this.getClass().getResource("/").getPath();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
