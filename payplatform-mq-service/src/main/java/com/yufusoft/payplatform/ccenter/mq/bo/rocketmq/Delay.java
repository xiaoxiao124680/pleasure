package com.yufusoft.payplatform.ccenter.mq.bo.rocketmq;

/**
 * 延时发送
 */
public interface Delay {

    /**
     * 设置延时等级
     * @param level
     */
    void setDelayLevel(int level);

    /**
     * 是否延迟
     * @return
     */
    boolean isDelay();
}
