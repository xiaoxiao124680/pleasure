package com.yufusoft.payplatform.ccenter.mq.exceptions;

import com.yufusoft.payplatform.util.exception.PayPlatformException;

/**
 * @author yzp
 * @date 2019/4/22
 */
public class LocalTransactionException extends PayPlatformException {

    public LocalTransactionException() {
        super();
    }

    public LocalTransactionException(String code) {
        super(code);
    }

    public LocalTransactionException(String code, String desc) {
        super(code, desc);
    }

    public LocalTransactionException(Throwable e) {
        super(e);
    }
}
