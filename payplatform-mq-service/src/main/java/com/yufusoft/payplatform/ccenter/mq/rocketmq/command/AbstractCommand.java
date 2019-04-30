package com.yufusoft.payplatform.ccenter.mq.rocketmq.command;

import com.yufusoft.payplatform.ccenter.mq.core.producer.Producer;

public abstract class AbstractCommand implements Command, Cloneable {

    private Producer producer;

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public AbstractCommand cloneObj() {
        try {
            return (AbstractCommand) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
