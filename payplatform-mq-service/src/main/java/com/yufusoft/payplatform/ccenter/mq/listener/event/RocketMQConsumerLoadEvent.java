package com.yufusoft.payplatform.ccenter.mq.listener.event;

import com.yufusoft.payplatform.ccenter.mq.rocketmq.RocketMQAbstractPushConsumer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author yzp
 * @date 2019/2/15
 */
public class RocketMQConsumerLoadEvent extends ApplicationEvent {

    private ApplicationContext context;

    private List<RocketMQAbstractPushConsumer> consumers;

    public RocketMQConsumerLoadEvent(ApplicationContext context, List<RocketMQAbstractPushConsumer> consumers) {
        super(context);
        this.context = context;
        this.consumers = consumers;
    }

    public ApplicationContext getContext() {
        return context;
    }

    public List<RocketMQAbstractPushConsumer> getConsumers() {
        return consumers;
    }
}
