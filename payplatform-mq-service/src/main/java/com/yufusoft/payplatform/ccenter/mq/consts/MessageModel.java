package com.yufusoft.payplatform.ccenter.mq.consts;

/**
 * rocketmq消息发送模型
 *
 * @author yzp
 * @date 2018.11.30
 */
public enum MessageModel {

    SYNCH(1, "同步"),
    ASYNCH(2, "异步"),
    ONEWAY(3, "单向"),
    COLLECTION(4, "集合");

    private final Integer code;
    private final String desc;

    MessageModel(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
