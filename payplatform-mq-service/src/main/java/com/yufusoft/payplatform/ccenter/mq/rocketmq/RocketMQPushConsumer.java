package com.yufusoft.payplatform.ccenter.mq.rocketmq;

import com.yufusoft.payplatform.ccenter.mq.config.Config;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * rocketMQ消费者
 * @author yzp
 * @date 2018.12.1
 */
public class RocketMQPushConsumer extends RocketMQAbstractPushConsumer {

    public RocketMQPushConsumer(Config config) {
        super(config);
    }

    @Override
    protected DefaultMQPushConsumer createConsumerMirror() {
        try {
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
            return consumer;
        } catch (Exception e) {

        }
        return null;
    }



}
