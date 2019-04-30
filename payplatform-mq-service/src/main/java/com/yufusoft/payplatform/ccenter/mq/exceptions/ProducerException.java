package com.yufusoft.payplatform.ccenter.mq.exceptions;

import com.yufusoft.payplatform.ccenter.mq.consts.MessageQueueRespConst;
import com.yufusoft.payplatform.util.exception.PayPlatformException;

/**
 * rocketMQ异常类
 *
 * @author yzp
 * @date 2018.11.30
 */
public class ProducerException extends PayPlatformException {


    public ProducerException(String code) {
        super(code);
    }

    public ProducerException(String code, String desc) {
        super(code, desc);
    }

    public ProducerException(Throwable e) {
        super(e);
    }

    public ProducerException(MessageQueueRespConst e) {
        super(e.getCode(), e.getDesc());
    }
}
