package com.yufusoft.payplatform.ccenter.mq.exceptions;

import com.yufusoft.payplatform.ccenter.mq.consts.MessageQueueRespConst;
import com.yufusoft.payplatform.util.exception.PayPlatformException;

/**
 * rocketMQ异常类
 *
 * @author yzp
 * @date 2018.11.30
 */
public class ConsumerException extends PayPlatformException {


    public ConsumerException(String code) {
        super(code);
    }

    public ConsumerException(String code, String desc) {
        super(code, desc);
    }

    public ConsumerException(Throwable e) {
        super(e);
    }

    public ConsumerException(MessageQueueRespConst e) {
        super(e.getCode(), e.getDesc());
    }
}
