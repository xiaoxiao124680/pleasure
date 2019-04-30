package com.yufusoft.payplatform.ccenter.mq.rocketmq.listener;

import com.yufusoft.payplatform.ccenter.mq.rocketmq.listener.handler.ListenerHandler;

public interface MessageListener {

    /**
     * 添加handler
     * @param handler
     */
    void addhandler(ListenerHandler handler);
    /**
     * 在元素前面添加handler
     * @param handler
     */
    void addhandlerBefore(String name, ListenerHandler handler);
    /**
     * 在元素后面添加handler
     * @param handler
     */
    void addhandlerAfter(String name, ListenerHandler handler);

}
