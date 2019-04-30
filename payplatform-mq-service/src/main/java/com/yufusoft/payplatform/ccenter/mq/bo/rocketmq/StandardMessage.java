package com.yufusoft.payplatform.ccenter.mq.bo.rocketmq;

import com.yufusoft.payplatform.ccenter.mq.bo.MQMessage;
import com.yufusoft.payplatform.ccenter.mq.consts.MessageModel;
import com.yufusoft.payplatform.ccenter.mq.consts.MessageType;
import org.apache.rocketmq.client.producer.SendCallback;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * 标准消息
 */
public class StandardMessage extends AbstractRocketMQMessage implements Delay, Collection {

    private static final MessageType MESSAGE_TYPE = MessageType.STANDARD_MESSAGE;

    /**
     * 消息发送模型
     */
    @NotNull
    private MessageModel messageModel;

    /**
     * 异步回调方法
     */
    private SendCallback callbackMethod;

    /**
     * 延时等级
     */
    private int delayLevel;

    /**
     * 消息集合
     */
    private Set<MQMessage> messages;

    public StandardMessage(String group, String topic, String tag, String key, String content) {
        super(group, topic, tag, key, content);
    }

    @Override
    public MessageType getMessageType() {
        return MESSAGE_TYPE;
    }

    public SendCallback getCallbackMethod() {
        return callbackMethod;
    }


    public int getDelayLevel() {
        return delayLevel;
    }

    @Override
    public void setDelayLevel(int level) {
        this.delayLevel = level;
    }

    @Override
    public boolean isDelay() {
        if (delayLevel != 0) {
            return true;
        }
        return false;
    }

    @Override
    public void addMessage(MQMessage message) {
        if (messages == null) {
            messages = new HashSet<>();
        }
        if (message instanceof StandardMessage) {
            messages.add(message);
        }
    }

    @Override
    public Set<MQMessage> getMessages() {
        return this.messages;
    }

    @Override
    public boolean checkParams() {
        switch (messageModel) {
            case SYNCH:
                return super.checkParams();
            case ASYNCH:
                if (super.checkParams()) {
                    if (callbackMethod != null) {
                        return true;
                    }
                }
                break;
            case ONEWAY:
                return super.checkParams();
            case COLLECTION:
                if (this.messages != null && !this.messages.isEmpty()) {
                    return true;
                }
        }
        return false;
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(MessageModel messageModel) {
        this.messageModel = messageModel;
    }

    public void setCallbackMethod(SendCallback callbackMethod) {
        this.callbackMethod = callbackMethod;
    }
}
