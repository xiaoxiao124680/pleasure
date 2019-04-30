package com.yufusoft.payplatform.ccenter.mq.rocketmq;

import com.yufusoft.payplatform.ccenter.mq.config.Config;
import com.yufusoft.payplatform.ccenter.mq.core.consumer.AbstractConsumer;
import com.yufusoft.payplatform.ccenter.mq.exceptions.ConsumerException;
import org.apache.rocketmq.client.consumer.AllocateMessageQueueStrategy;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.MessageQueueListener;
import org.apache.rocketmq.client.consumer.store.OffsetStore;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.Set;

/**
 * pull rocketMQ抽象类
 *
 * @author yzp
 * @date 2018.12.1
 */
public abstract class RocketMQAbstractPullConsumer extends AbstractConsumer {

    protected DefaultMQPullConsumer defaultMQPullConsumer;

    protected String consumerGroup;
    protected long brokerSuspendMaxTimeMillis;
    protected long consumerTimeoutMillisWhenSuspend;
    protected long consumerPullTimeoutMillis;
    protected MessageModel messageModel;
    protected MessageQueueListener messageQueueListener;
    protected OffsetStore offsetStore;
    protected Set<String> registerTopics;
    protected AllocateMessageQueueStrategy allocateMessageQueueStrategy;
    protected boolean unitMode;
    protected int maxReconsumeTimes;


    @Override
    protected void initConfig() throws ConsumerException {

    }

    @Override
    public Object getConsumerMirror() {
        return defaultMQPullConsumer;
    }

    @Override
    public void start() {
        try {
            defaultMQPullConsumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        defaultMQPullConsumer.shutdown();
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public Config getConfig() {
        return null;
    }

    abstract protected DefaultMQPullConsumer createConsumerMirror();
}
