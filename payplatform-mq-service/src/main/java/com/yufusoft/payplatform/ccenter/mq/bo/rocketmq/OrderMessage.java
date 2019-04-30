package com.yufusoft.payplatform.ccenter.mq.bo.rocketmq;

import com.yufusoft.payplatform.ccenter.mq.consts.MessageModel;
import com.yufusoft.payplatform.ccenter.mq.consts.MessageType;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;

/**
 * 顺序消息
 */
public class OrderMessage extends AbstractRocketMQMessage implements Delay {

    private static final MessageType MESSAGE_TYPE = MessageType.ORDER_MESSAGE;

    /**
     * 消息发送模型
     */
    private MessageModel messageModel;
    /**
     * 参数
     */
    private Object args;

    /**
     * 队列选择器
     */
    private MessageQueueSelector selector;

    /**
     * 异步回调方法
     */
    private SendCallback callbackMethod;

    /**
     * 延时等级
     */
    private int delayLevel;

    public OrderMessage(String group, String topic, String tag, String key, String content) {
        super(group, topic, tag, key, content);
    }

    @Override
    public MessageType getMessageType() {
        return MESSAGE_TYPE;
    }

    @Override
    public void setDelayLevel(int level) {
        this.delayLevel = level;
    }

    public Object getArgs() {
        return args;
    }

    public void setArgs(Object args) {
        this.args = args;
    }

    public MessageQueueSelector getSelector() {
        return selector;
    }

    public void setSelector(MessageQueueSelector selector) {
        this.selector = selector;
    }

    public SendCallback getCallbackMethod() {
        return callbackMethod;
    }

    public int getDelayLevel() {
        return delayLevel;
    }


    @Override
    public boolean isDelay() {
        if (delayLevel != 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkParams() {
        if (super.checkParams() && selector != null && MessageModel.COLLECTION != messageModel) {
            return true;
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
