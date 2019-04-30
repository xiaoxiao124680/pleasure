package com.yufusoft.payplatform.ccenter.mq.rocketmq.command;

import com.yufusoft.payplatform.ccenter.mq.bo.MQMessage;
import com.yufusoft.payplatform.ccenter.mq.bo.rocketmq.OrderMessage;
import com.yufusoft.payplatform.ccenter.mq.consts.MessageQueueRespConst;
import com.yufusoft.payplatform.ccenter.mq.consts.MessageType;
import com.yufusoft.payplatform.ccenter.mq.exceptions.ProducerException;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component("order_command")
public class OrderCommand extends AbstractCommand {

    @Override
    public SendResult send(MQMessage mqMessage) throws ProducerException {

        try {
            DefaultMQProducer producer = (DefaultMQProducer) getProducer().getProducerMirror();
            if (MessageType.ORDER_MESSAGE == mqMessage.getMessageType()) {
                OrderMessage message = (OrderMessage) mqMessage;
                Message msg = new Message(message.getTopic(), message.getTag(), message.getContent().getBytes(RemotingHelper.DEFAULT_CHARSET));
                msg.setDelayTimeLevel(message.getDelayLevel());
                switch (message.getMessageModel()) {
                    case SYNCH:
                        return producer.send(msg, message.getSelector(), message.getArgs());
                    case ASYNCH:
                        producer.send(msg, message.getSelector(), message.getArgs(), message.getCallbackMethod());
                        return null;
                    default:
                        throw new ProducerException(MessageQueueRespConst.EXCEPTION);
                }
            } else {
                throw new ProducerException(MessageQueueRespConst.EXCEPTION);
            }
        } catch (UnsupportedEncodingException e) {
            throw new ProducerException(e);
        } catch (MQClientException e) {
            throw new ProducerException(e);
        } catch (RemotingException e) {
            throw new ProducerException(e);
        } catch (MQBrokerException e) {
            throw new ProducerException(e);
        } catch (InterruptedException e) {
            throw new ProducerException(e);
        }
    }
}
