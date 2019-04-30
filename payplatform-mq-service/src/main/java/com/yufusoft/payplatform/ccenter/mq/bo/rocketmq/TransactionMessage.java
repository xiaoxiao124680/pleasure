package com.yufusoft.payplatform.ccenter.mq.bo.rocketmq;

import com.yufusoft.payplatform.ccenter.mq.consts.MessageModel;
import com.yufusoft.payplatform.ccenter.mq.consts.MessageType;

/**
 * 事务消息
 */
public class TransactionMessage extends AbstractRocketMQMessage {

    private static final MessageType MESSAGE_TYPE = MessageType.TRANSACTION_MESSAGE;

    private MessageModel messageModel = MessageModel.SYNCH;

    private Object args;

    public TransactionMessage(String group, String topic, String tag, String key, String content) {
        super(group, topic, tag, key, content);
    }

    @Override
    public MessageType getMessageType() {
        return MESSAGE_TYPE;
    }

    public Object getArgs() {
        return args;
    }

    public void setArgs(Object args) {
        this.args = args;
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }
}
