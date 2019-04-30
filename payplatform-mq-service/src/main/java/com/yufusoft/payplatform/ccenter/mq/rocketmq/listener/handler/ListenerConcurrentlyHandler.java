package com.yufusoft.payplatform.ccenter.mq.rocketmq.listener.handler;

import com.yufusoft.payplatform.ccenter.mq.rocketmq.listener.DefaultMessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

public abstract class ListenerConcurrentlyHandler implements ListenerHandler {


    private DefaultMessageListenerConcurrently listener;

    public abstract void consume(MessageExt message, ConsumeConcurrentlyContext context);

    public DefaultMessageListenerConcurrently getListener() {
        return listener;
    }

    public void setListener(DefaultMessageListenerConcurrently listener) {
        this.listener = listener;
    }
}
