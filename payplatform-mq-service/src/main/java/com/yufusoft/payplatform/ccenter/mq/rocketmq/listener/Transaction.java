package com.yufusoft.payplatform.ccenter.mq.rocketmq.listener;

import com.yufusoft.payplatform.ccenter.mq.exceptions.LocalTransactionException;
import org.apache.rocketmq.common.message.Message;

public interface Transaction {

    void excute(Message message, Object o) throws LocalTransactionException;

    boolean checkState(Message message) throws LocalTransactionException;

}
