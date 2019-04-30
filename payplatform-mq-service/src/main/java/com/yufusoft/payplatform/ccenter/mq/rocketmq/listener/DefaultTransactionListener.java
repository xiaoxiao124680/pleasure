package com.yufusoft.payplatform.ccenter.mq.rocketmq.listener;

import com.yufusoft.payplatform.ccenter.mq.exceptions.LocalTransactionException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("defaultTransactionListener")
public class DefaultTransactionListener implements TransactionListener {

    @Autowired
    private Map<String, Transaction> transactionExecutor = new HashMap<>();

    /**
     * 本地事务执行器键
     */
    private static final String LOCAL_TRANSACTION_EXECUTOR_KEY = "local_transaction_executor";

    public DefaultTransactionListener() {
    }

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        Transaction executor = transactionExecutor.get(message.getProperty(LOCAL_TRANSACTION_EXECUTOR_KEY));
        if (executor == null) {

        }
        try {
            executor.excute(message, o);
            return LocalTransactionState.COMMIT_MESSAGE;
        } catch (LocalTransactionException e) {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        } catch (Throwable e) {
            return LocalTransactionState.UNKNOW;
        }
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt message) {
        Transaction executor = transactionExecutor.get(message.getProperty(LOCAL_TRANSACTION_EXECUTOR_KEY));
        if (executor == null) {

        }
        try {
            boolean result = executor.checkState(message);
            if (result) {
                return LocalTransactionState.COMMIT_MESSAGE;
            }
            return LocalTransactionState.ROLLBACK_MESSAGE;
        } catch (Exception e) {
            return LocalTransactionState.UNKNOW;
        }
    }
}
