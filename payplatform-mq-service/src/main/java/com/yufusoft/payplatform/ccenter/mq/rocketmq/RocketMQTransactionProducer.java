package com.yufusoft.payplatform.ccenter.mq.rocketmq;

import com.yufusoft.payplatform.ccenter.mq.config.Config;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.listener.DefaultTransactionListener;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.*;

/**
 * rocketMQ事务生产者
 *
 * @author yzp
 * @date 2018.12.1
 */
public class RocketMQTransactionProducer extends RocketMQAbstractProducer implements ApplicationContextAware {


    private ApplicationContext applicationContext;

    public RocketMQTransactionProducer(Config config) {
        super(config);
    }


    @Override
    protected DefaultMQProducer createProducerMirror() {
        try {
            TransactionMQProducer producer = new TransactionMQProducer();
            ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("client-transaction-msg-check-thread");
                    return thread;
                }
            });

            producer.setExecutorService(executorService);
            DefaultTransactionListener transactionListener = (DefaultTransactionListener) applicationContext.getBean("defaultTransactionListener");
            if (transactionListener == null) {
                transactionListener = new DefaultTransactionListener();
            }
            producer.setTransactionListener(transactionListener);
            return producer;
        } catch (Exception e) {

        }
        return null;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
