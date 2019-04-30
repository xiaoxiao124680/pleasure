package com.yufusoft.payplatform.ccenter.mq.rocketmq.command;

import com.yufusoft.payplatform.ccenter.mq.bo.MQMessage;
import com.yufusoft.payplatform.ccenter.mq.bo.rocketmq.TransactionMessage;
import com.yufusoft.payplatform.ccenter.mq.consts.MessageQueueRespConst;
import com.yufusoft.payplatform.ccenter.mq.consts.MessageType;
import com.yufusoft.payplatform.ccenter.mq.exceptions.ProducerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component("transaction_command")
public class TransactionCommand extends AbstractCommand {

    @Autowired
    private TransactionListener transactionListener;

    @Override
    public SendResult send(MQMessage mqMessage) throws ProducerException {

        try {
            TransactionMQProducer producer = (TransactionMQProducer) getProducer().getProducerMirror();
            if (MessageType.TRANSACTION_MESSAGE == mqMessage.getMessageType()) {
                TransactionMessage message = (TransactionMessage) mqMessage;
                Message msg = new Message(message.getTopic(), message.getTag(), message.getContent().getBytes("utf-8"));
                return producer.sendMessageInTransaction(msg, message.getArgs());
            } else {
                throw new ProducerException(MessageQueueRespConst.EXCEPTION);
            }
        } catch (UnsupportedEncodingException e) {
            throw new ProducerException(e);
        } catch (MQClientException e) {
            e.printStackTrace();
            throw new ProducerException(e);
        }

    }


}
