package com.yufusoft.payplatform.ccenter.mq.consts;

import java.util.EnumSet;

/**
 * 错误码常量
 * @author yzp
 * @date 2018.11.30
 */
public enum MessageQueueRespConst {

    SUCCESS("0000000","成功"),
    EXCEPTION("0019999","系统异常"),
    PARAMETERS_ERROR("0019998","参数错误");

    private final String code;
    private final String desc;

    private MessageQueueRespConst(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static MessageQueueRespConst getEnum(String code) {
        EnumSet<MessageQueueRespConst> currEnumSet = EnumSet.allOf(MessageQueueRespConst.class);
        for (MessageQueueRespConst a : currEnumSet) {
            if (a.getCode().equals(code)) {
                return a;
            }
        }
        return null;
    }
}
