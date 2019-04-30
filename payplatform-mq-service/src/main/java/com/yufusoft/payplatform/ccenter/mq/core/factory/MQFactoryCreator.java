package com.yufusoft.payplatform.ccenter.mq.core.factory;


import com.yufusoft.payplatform.ccenter.mq.core.consumer.Consumer;
import com.yufusoft.payplatform.ccenter.mq.core.producer.Producer;

public interface MQFactoryCreator {

    Producer getProducer(String id);

    Consumer getConsumer(String id);

}
