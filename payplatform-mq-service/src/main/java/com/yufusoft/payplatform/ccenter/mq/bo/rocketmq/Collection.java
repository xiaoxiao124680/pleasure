package com.yufusoft.payplatform.ccenter.mq.bo.rocketmq;

import com.yufusoft.payplatform.ccenter.mq.bo.MQMessage;

import java.util.Set;

public interface Collection {
    /**
     * 向集合中添加消息
     * @param message
     */
    void addMessage(MQMessage message);

    /**
     * 获取消息集合
     * @return
     */
    Set<MQMessage> getMessages();
}
