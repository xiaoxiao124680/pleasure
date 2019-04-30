package com.yufusoft.payplatform.ccenter.mq.rocketmq.config;

import com.yufusoft.payplatform.ccenter.mq.config.AbstractConfigLoader;
import com.yufusoft.payplatform.ccenter.mq.config.Config;
import com.yufusoft.payplatform.ccenter.mq.config.FileHelper;
import com.yufusoft.payplatform.ccenter.mq.config.XmlConfigResolver;
import com.yufusoft.payplatform.ccenter.mq.exceptions.ProducerException;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.RocketMQAbstractProducer;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.RocketMQFactory;
import com.yufusoft.payplatform.util.common.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * RocketMQXml 生产者加载器
 *
 * @author yzp
 * @date 2018.12.1
 */
@Component
public class RocketMQXmlProducerLoader extends AbstractConfigLoader {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static String PRODUCER_XML_ADDRESS = System.getProperty("resourceDir");

    private static final String PRODUCER_XML_CONFIG_KEYWORD = "RocketMQProducerConfig.xml";

    private static final int PRODUCER_START_OVER_TIME = 10;

    private static final ExecutorService executorService = new ThreadPoolExecutor(3, 10, 300, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20));


    @Resource(name = "producerXmlConfigResolver")
    private XmlConfigResolver xmlConfigResolver;

    @Autowired
    private RocketMQFactory rocketMQFactory;


    @Override
    public void load() {
        try {
            if (StringUtil.isEmpty(PRODUCER_XML_ADDRESS)) {
                PRODUCER_XML_ADDRESS = System.getProperty("resourceDir");
            }
            List<File> files = FileHelper.getFile(PRODUCER_XML_ADDRESS, PRODUCER_XML_CONFIG_KEYWORD);
            if (files.size() == 1) {
                Config config = resolver(files.get(0));
                load(config);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void loadToMedium(Config config) {
        if (!(config instanceof ProducerXmlConfig)) {
            return;
        }
        ProducerXmlConfig xmlConfig = (ProducerXmlConfig) config;
        List<ProducerXmlConfig> list = xmlConfig.getList();
        try {
            for (ProducerXmlConfig producerXmlConfig : list) {
                if (producerXmlConfig.isAvailable()) {
                    Class clzz = Class.forName(producerXmlConfig.getClassName());
                    Constructor c = clzz.getConstructor(Config.class);
                    RocketMQAbstractProducer producer = (RocketMQAbstractProducer) c.newInstance(producerXmlConfig);
                    Thread thread = new Thread(new producerStartManager(producerXmlConfig.getId(), rocketMQFactory, producer));
                    executorService.execute(thread);
                }
            }
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
    public Config resolver(Object source) throws Exception {
        try {
            return xmlConfigResolver.resolver(source);
        } catch (ProducerException e) {
            throw e;
        }
    }

    @Override
    protected void checkConfig(Config config) {

    }


    private class producerStartManager implements Runnable {
        String producerId;
        RocketMQFactory rocketMQFactory;
        RocketMQAbstractProducer producer;

        public producerStartManager(String producerId, RocketMQFactory rocketMQFactory, RocketMQAbstractProducer producer) {
            this.producerId = producerId;
            this.rocketMQFactory = rocketMQFactory;
            this.producer = producer;
        }

        @Override
        public void run() {
            int checkNum = 0;
            producer.start();
            for (; ; ) {
                if (!producer.isRunning()) {
                    try {
                        if (checkNum > PRODUCER_START_OVER_TIME) {
                            logger.error("producer start over time,the producer id is " + producerId);
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
            rocketMQFactory.setProducer(producerId, producer);
        }
    }
}
