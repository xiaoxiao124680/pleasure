package com.yufusoft.payplatform.ccenter.mq.rocketmq.command;

import com.yufusoft.payplatform.ccenter.mq.bo.MQMessage;
import com.yufusoft.payplatform.ccenter.mq.bo.rocketmq.StandardMessage;
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
import java.util.HashSet;
import java.util.Set;

@Component("standard_command")
public class StandardCommand extends AbstractCommand {

    @Override
    public SendResult send(MQMessage mqMessage) throws ProducerException {

        try {
            DefaultMQProducer producer = (DefaultMQProducer) getProducer().getProducerMirror();
            if (MessageType.STANDARD_MESSAGE == mqMessage.getMessageType()) {
                StandardMessage message = (StandardMessage) mqMessage;
                Message msg = new Message(message.getTopic(), message.getTag(), message.getContent().getBytes(RemotingHelper.DEFAULT_CHARSET));
                msg.setDelayTimeLevel(message.getDelayLevel());
                switch (message.getMessageModel()) {
                    case SYNCH:
                        return producer.send(msg);
                    case ASYNCH:
                        producer.send(msg, message.getCallbackMethod());
                        return null;
                    case COLLECTION:
                        Set<Message> set = new HashSet<>();
                        Set<MQMessage> messages = message.getMessages();
                        for (MQMessage mqMsg : messages) {
                            StandardMessage standardMessage = (StandardMessage) mqMsg;
                            Message m = new Message(standardMessage.getTopic(), standardMessage.getTag(), standardMessage.getContent().getBytes(RemotingHelper.DEFAULT_CHARSET));
                            set.add(m);
                        }
                        return producer.send(set);
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
