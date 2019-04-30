package com.yufusoft.payplatform.ccenter.mq.rocketmq;

import com.yufusoft.payplatform.ccenter.mq.bo.MQMessage;
import com.yufusoft.payplatform.ccenter.mq.bo.rocketmq.AbstractRocketMQMessage;
import com.yufusoft.payplatform.ccenter.mq.core.producer.MessageSender;
import com.yufusoft.payplatform.ccenter.mq.core.producer.Producer;
import com.yufusoft.payplatform.ccenter.mq.exceptions.ProducerException;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.command.AbstractCommand;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.command.CommandFactory;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息发送器
 *
 * @author yzp
 * @date 2018.12.22
 */
@Component
public class RocketMQMessageSender implements MessageSender {

    @Autowired
    CommandFactory commandFactory;
    @Autowired
    RocketMQFactory rocketMQFactory;

    @Override
    public SendResult send(MQMessage msg) throws ProducerException {
        AbstractRocketMQMessage message = (AbstractRocketMQMessage) msg;
        Producer producer = rocketMQFactory.getProducer(message.getGroup());
        AbstractCommand command = (AbstractCommand) commandFactory.getCommand(msg);
        command.setProducer(producer);
        return (SendResult) command.send(msg);
    }


}
