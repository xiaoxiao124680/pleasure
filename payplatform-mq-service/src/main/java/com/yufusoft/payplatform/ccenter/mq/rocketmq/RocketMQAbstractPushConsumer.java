package com.yufusoft.payplatform.ccenter.mq.rocketmq;

import com.yufusoft.payplatform.ccenter.mq.config.Config;
import com.yufusoft.payplatform.ccenter.mq.core.consumer.AbstractConsumer;
import org.apache.rocketmq.client.consumer.AllocateMessageQueueStrategy;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.ServiceState;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * push rocketMQ抽象类
 *
 * @author yzp
 * @date 2018.12.1
 */
public abstract class RocketMQAbstractPushConsumer extends AbstractConsumer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected DefaultMQPushConsumer defaultMQPushConsumer;

    protected String namesrvAddr;
    /**
     * 第一次消费位置
     */
    protected ConsumeFromWhere consumeFromWhere;

    /**
     * 消费方式
     * 1 BROADCASTING 广播模式
     * 2 CLUSTERING 集群模式
     */
    protected MessageModel messageModel;
    /**
     * 集群消息分配策略
     */
    protected AllocateMessageQueueStrategy allocateMessageQueueStrategy;
    /**
     * 线程池最小值
     */
    protected int consumeThreadMin;
    /**
     * 线程池最大值
     */
    protected int consumeThreadMax;

    /**
     *
     */
    protected int adjustThreadPoolNumsThreshold;

    /**
     * 单队列并行消费最大跨度，用于流控
     */
    protected int consumeConcurrentlyMaxSpan;
    /**
     * 一个queue最大消费的消息个数，用于流控
     */
    protected int pullThresholdForQueue;
    /**
     * 消息拉取时间间隔，默认为0，即拉完一次立马拉第二次，单位毫秒
     */
    protected int pullInterval;
    /**
     * 并发消费时，一次消费消息的数量，默认为1，假如修改为50，此时若有100条消息，那么会创建两个线程，每个线程分配50条消息
     */
    protected int consumeMessageBatchMaxSize;
    /**
     * 消息拉取一次的数量
     */
    protected int pullBatchSize;

    protected boolean postSubscriptionWhenPull;

    protected boolean unitMode;

    private Config config;

    public RocketMQAbstractPushConsumer(Config config) {
        this.config = config;
        initConfig();
    }

    @Override
    protected void initConfig() {
        if (this.config == null) {
            logger.error("producer init error,the config is null");
            return;
        }
        if (this.defaultMQPushConsumer != null) {
            logger.error("producer init error,the mirror is not null");
            return;
        }
        this.defaultMQPushConsumer = createConsumerMirror();
        Map<String, Object> map = this.config.getConfigMap();
        this.defaultMQPushConsumer.setNamesrvAddr((String) map.get("nameServerAddress"));
        this.defaultMQPushConsumer.setConsumerGroup((String) map.get("consumerGroup"));
        this.defaultMQPushConsumer.setMessageModel(MessageModel.valueOf((String) map.get("messageModel")));
        this.defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.valueOf((String) map.get("consumeFromWhere")));
        this.defaultMQPushConsumer.setConsumeThreadMin((Integer) map.get("consumeThreadMin"));
        this.defaultMQPushConsumer.setConsumeThreadMax((Integer) map.get("consumeThreadMax"));
        this.defaultMQPushConsumer.setAdjustThreadPoolNumsThreshold((Long) map.get("adjustThreadPoolNumsThreshold"));
        this.defaultMQPushConsumer.setConsumeConcurrentlyMaxSpan((Integer) map.get("consumeConcurrentlyMaxSpan"));
        this.defaultMQPushConsumer.setPullThresholdForQueue((Integer) map.get("pullThresholdForQueue"));
        this.defaultMQPushConsumer.setPullInterval((Long) map.get("pullInterval"));
        this.defaultMQPushConsumer.setInstanceName((String) map.get("instanceName"));
        this.defaultMQPushConsumer.setConsumeMessageBatchMaxSize((Integer) map.get("consumeMessageBatchMaxSize"));
        this.defaultMQPushConsumer.setPullBatchSize((Integer) map.get("pullBatchSize"));
        this.defaultMQPushConsumer.setPostSubscriptionWhenPull((Boolean) map.get("postSubscriptionWhenPull"));
        this.defaultMQPushConsumer.setUnitMode((Boolean) map.get("unitMode"));
        this.defaultMQPushConsumer.setMessageListener((MessageListener) map.get("messageListener"));
        String[] topic = (String[]) map.get("topic");
        String[] targ = (String[]) map.get("targ");
        for (int i = 0; i < topic.length; i++) {
            try {
                defaultMQPushConsumer.subscribe(topic[i], targ[i]);
            } catch (MQClientException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public Object getConsumerMirror() {
        return defaultMQPushConsumer;
    }

    @Override
    public void start() {
        try {
            defaultMQPushConsumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        defaultMQPushConsumer.shutdown();
    }

    @Override
    public boolean isRunning() {
        ServiceState serviceState = defaultMQPushConsumer.getDefaultMQPushConsumerImpl().getServiceState();
        if (serviceState == ServiceState.RUNNING) {
            return true;
        }
        return false;
    }

    @Override
    public Config getConfig() {
        return this.config;
    }

    @Override
    public Map<String, Object> getConfigMap() {
        return this.config.getConfigMap();
    }

    abstract protected DefaultMQPushConsumer createConsumerMirror();
}
