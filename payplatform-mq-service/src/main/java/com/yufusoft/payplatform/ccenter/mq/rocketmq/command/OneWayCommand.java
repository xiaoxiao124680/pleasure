package com.yufusoft.payplatform.ccenter.mq.rocketmq.command;

import com.yufusoft.payplatform.ccenter.mq.bo.MQMessage;
import com.yufusoft.payplatform.ccenter.mq.bo.rocketmq.OrderMessage;
import com.yufusoft.payplatform.ccenter.mq.bo.rocketmq.StandardMessage;
import com.yufusoft.payplatform.ccenter.mq.consts.MessageQueueRespConst;
import com.yufusoft.payplatform.ccenter.mq.consts.MessageType;
import com.yufusoft.payplatform.ccenter.mq.exceptions.ProducerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component("oneway_command")
public class OneWayCommand extends AbstractCommand {


    @Override
    public SendResult send(MQMessage mqMessage) throws ProducerException {
        try {
            DefaultMQProducer producer = (DefaultMQProducer) getProducer().getProducerMirror();
            if (MessageType.STANDARD_MESSAGE == mqMessage.getMessageType()) {
                StandardMessage message = (StandardMessage) mqMessage;
                Message msg = new Message(message.getTopic(), message.getTag(), message.getContent().getBytes(RemotingHelper.DEFAULT_CHARSET));
                msg.setDelayTimeLevel(message.getDelayLevel());
                producer.sendOneway(msg);
            } else if (MessageType.ORDER_MESSAGE == mqMessage.getMessageType()) {
                OrderMessage message = (OrderMessage) mqMessage;
                Message msg = new Message(message.getTopic(), message.getTag(), message.getContent().getBytes(RemotingHelper.DEFAULT_CHARSET));
                msg.setDelayTimeLevel(message.getDelayLevel());
                producer.sendOneway(msg, message.getSelector(), message.getArgs());
            } else {
                throw new ProducerException(MessageQueueRespConst.EXCEPTION);
            }
            return null;
        } catch (UnsupportedEncodingException e) {
            throw new ProducerException(e);
        } catch (MQClientException e) {
            throw new ProducerException(e);
        } catch (RemotingException e) {
            throw new ProducerException(e);
        } catch (InterruptedException e) {
            throw new ProducerException(e);
        }

    }

}
