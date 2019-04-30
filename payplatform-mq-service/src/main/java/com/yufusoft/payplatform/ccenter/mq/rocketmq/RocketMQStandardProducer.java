package com.yufusoft.payplatform.ccenter.mq.rocketmq;

import com.yufusoft.payplatform.ccenter.mq.config.Config;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

/**
 * 标准rocketMQ生产者
 *
 * @author yzp
 * @date 2018.12.1
 */
public class RocketMQStandardProducer extends RocketMQAbstractProducer {

    public RocketMQStandardProducer(Config config) {
        super(config);
    }

    @Override
    protected DefaultMQProducer createProducerMirror() {
        try {
            DefaultMQProducer producer = new DefaultMQProducer();
            producer.setRetryTimesWhenSendAsyncFailed(0);
            return producer;
        } catch (Exception e) {

        }
        return null;
    }


}
