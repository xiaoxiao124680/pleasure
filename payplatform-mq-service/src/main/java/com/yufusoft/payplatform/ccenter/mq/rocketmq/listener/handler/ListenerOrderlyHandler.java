package com.yufusoft.payplatform.ccenter.mq.rocketmq.listener.handler;

import com.yufusoft.payplatform.ccenter.mq.rocketmq.listener.DefaultMessageListenerOrderly;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.common.message.MessageExt;

public abstract class ListenerOrderlyHandler implements ListenerHandler {

    private DefaultMessageListenerOrderly listener;

    public abstract void consume(MessageExt message, ConsumeOrderlyContext context);

    public DefaultMessageListenerOrderly getListener() {
        return listener;
    }

    public void setListener(DefaultMessageListenerOrderly listener) {
        this.listener = listener;
    }
}
