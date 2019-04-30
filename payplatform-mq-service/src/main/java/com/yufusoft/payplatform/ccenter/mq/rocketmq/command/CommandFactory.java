package com.yufusoft.payplatform.ccenter.mq.rocketmq.command;


import com.yufusoft.payplatform.ccenter.mq.bo.MQMessage;
import com.yufusoft.payplatform.ccenter.mq.bo.rocketmq.OrderMessage;
import com.yufusoft.payplatform.ccenter.mq.bo.rocketmq.StandardMessage;
import com.yufusoft.payplatform.ccenter.mq.consts.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CommandFactory {

    @Autowired
    private Map<String, AbstractCommand> commands = new HashMap<>();

    private ConcurrentHashMap<String, AbstractCommand> commandCache = new ConcurrentHashMap();

    /**
     * 标准发送命令
     */
    private static final String STANDARD_COMMAND = "standard_command";

    /**
     * 顺序发送命令
     */
    private static final String ORDER_COMMAND = "order_command";

    /**
     * 单向发送命令
     */
    private static final String ONEWAY_COMMAND = "oneway_command";


    /**
     * 事务发送命令
     */
    private static final String TRANSACTION_COMMAND = "transaction_command";


    public CommandFactory() {
    }

    public Command getCommand(MQMessage message) {
        String group = (String) message.get("group");
        String cacheKey = null;
        switch (message.getMessageType()) {
            case STANDARD_MESSAGE:
                StandardMessage standardMessage = (StandardMessage) message;
                if (MessageModel.ONEWAY == standardMessage.getMessageModel()) {
                    cacheKey = ONEWAY_COMMAND;
                } else {
                    cacheKey = STANDARD_COMMAND;
                }
                break;
            case ORDER_MESSAGE:
                OrderMessage orderMessage = (OrderMessage) message;
                if (MessageModel.ONEWAY == orderMessage.getMessageModel()) {
                    cacheKey = ONEWAY_COMMAND;
                } else {
                    cacheKey = ORDER_COMMAND;
                }
                break;
            case TRANSACTION_MESSAGE:
                cacheKey = TRANSACTION_COMMAND;
                break;
        }
        return getCommandFromCache(group, cacheKey);
    }

    private AbstractCommand getCommandFromCache(String group, String cacheKey) {
        AbstractCommand command = commandCache.get(group + "_" + cacheKey);
        if (command == null) {
            command = commands.get(cacheKey).cloneObj();
            commandCache.put(group + "_" + cacheKey, command);
        }
        return command;
    }
}
