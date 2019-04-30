package com.yufusoft.payplatform.ccenter.mq.core.producer;

import com.yufusoft.payplatform.ccenter.mq.bo.MQMessage;
import com.yufusoft.payplatform.ccenter.mq.exceptions.ProducerException;

public interface MessageSend {

    Object send(MQMessage msg) throws ProducerException;
}
