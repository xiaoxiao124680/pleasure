package com.yufusoft.payplatform.ccenter.mq.rocketmq.config;

import com.yufusoft.payplatform.ccenter.mq.config.AbstractConfigLoader;
import com.yufusoft.payplatform.ccenter.mq.config.Config;
import com.yufusoft.payplatform.ccenter.mq.config.FileHelper;
import com.yufusoft.payplatform.ccenter.mq.config.XmlConfigResolver;
import com.yufusoft.payplatform.ccenter.mq.exceptions.ConsumerException;
import com.yufusoft.payplatform.ccenter.mq.listener.event.RocketMQConsumerLoadEvent;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.RocketMQAbstractPushConsumer;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.RocketMQFactory;
import com.yufusoft.payplatform.util.common.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * RocketMQXml 消费者加载器
 *
 * @author yzp
 * @date 2018.12.1
 */
@Component
public class RocketMQXmlConsumerLoader extends AbstractConfigLoader implements ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static ApplicationContext context;

    private static String CONSUMER_XML_ADDRESS = System.getProperty("resourceDir");

    private static final String CONSUMER_XML_CONFIG_KEYWORD = "RocketMQConsumerConfig.xml";

    private static final int CONSUMER_START_OVER_TIME = 10;

    private static final ExecutorService executorService = new ThreadPoolExecutor(3, 20, 300, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20));

    @Resource(name = "concumerXmlConfigResolver")
    private XmlConfigResolver xmlConfigResolver;

    @Autowired
    private RocketMQFactory rocketMQFactory;

    @Override
    protected void loadToMedium(Config config) {
        if (!(config instanceof ConsumerXmlConfig)) {
            return;
        }
        ConsumerXmlConfig xmlConfig = (ConsumerXmlConfig) config;
        List<ConsumerXmlConfig> list = xmlConfig.getList();
        try {
            List<RocketMQAbstractPushConsumer> consumers = new ArrayList<>();
            for (ConsumerXmlConfig consumerXmlConfig : list) {
                if (consumerXmlConfig.isAvailable()) {
                    Class clzz = Class.forName(consumerXmlConfig.getClassName());
                    Constructor c = clzz.getConstructor(Config.class);
                    RocketMQAbstractPushConsumer consumer = (RocketMQAbstractPushConsumer) c.newInstance(consumerXmlConfig);
                    consumers.add(consumer);
                    Thread thread = new Thread(new ConcumerStartManager(consumerXmlConfig.getId(), rocketMQFactory, consumer));
                    executorService.execute(thread);
                }
            }
            context.publishEvent(new RocketMQConsumerLoadEvent(context, consumers));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void checkConfig(Config config) {

    }

    @Override
    public Config resolver(Object source) throws Exception {
        try {
            return xmlConfigResolver.resolver(source);
        } catch (ConsumerException e) {
            throw e;
        }
    }

    @Override
    public void load() {
        try {
            if (StringUtil.isEmpty(CONSUMER_XML_ADDRESS)) {
                CONSUMER_XML_ADDRESS = System.getProperty("resourceDir");
            }
            List<File> files = FileHelper.getFile(CONSUMER_XML_ADDRESS, CONSUMER_XML_CONFIG_KEYWORD);
            if (files.size() == 1) {
                Config config = resolver(files.get(0));
                load(config);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class ConcumerStartManager implements Runnable {
        String concumerId;
        RocketMQFactory rocketMQFactory;
        RocketMQAbstractPushConsumer consumer;

        public ConcumerStartManager(String concumerId, RocketMQFactory rocketMQFactory, RocketMQAbstractPushConsumer consumer) {
            this.concumerId = concumerId;
            this.rocketMQFactory = rocketMQFactory;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            int checkNum = 0;
            consumer.start();
            for (; ; ) {
                if (!consumer.isRunning()) {
                    try {
                        if (checkNum > CONSUMER_START_OVER_TIME) {
                            logger.error("producer start over time,the producer id is " + concumerId);
                            Thread.interrupted();
                            return;
                        }
                        checkNum++;
                        Thread.sleep(10000);
                        continue;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
                break;
            }
            rocketMQFactory.setConsumer(concumerId, consumer);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
