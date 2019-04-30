package com.yufusoft.payplatform.ccenter.mq.consts;

/**
 * rocketmq消息类型常量
 *
 * @author yzp
 * @date 2018.11.30
 */
public enum MessageType {

    STANDARD_MESSAGE(1, "标准消息"),
    ORDER_MESSAGE(2, "顺序消息"),
    TRANSACTION_MESSAGE(3, "事务消息");

    private final Integer code;
    private final String desc;

    MessageType(Integer code, String desc) {
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
