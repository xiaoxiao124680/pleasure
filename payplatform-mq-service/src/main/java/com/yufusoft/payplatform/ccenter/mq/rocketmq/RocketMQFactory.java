package com.yufusoft.payplatform.ccenter.mq.rocketmq;


import com.yufusoft.payplatform.ccenter.mq.core.consumer.Consumer;
import com.yufusoft.payplatform.ccenter.mq.core.factory.MQFactoryCreator;
import com.yufusoft.payplatform.ccenter.mq.core.producer.Producer;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class RocketMQFactory implements MQFactoryCreator {


    private ConcurrentHashMap<String, Producer> producers = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, Consumer> consumers = new ConcurrentHashMap<>();


    public RocketMQFactory() {

    }


    @Override
    public Producer getProducer(String id) {
        Producer producer = producers.get(id);
        return producer;
    }

    @Override
    public Consumer getConsumer(String id) {
        return consumers.get(id);
    }

    public void setProducer(String id, Producer producer) {
        this.producers.put(id, producer);
    }

    public void setConsumer(String id, Consumer consumer) {
        this.consumers.put(id, consumer);
    }

    public void removeProducer(String id) {
        producers.remove(id);
    }

    public void removeConsumer(String id) {
        consumers.remove(id);
    }
}
