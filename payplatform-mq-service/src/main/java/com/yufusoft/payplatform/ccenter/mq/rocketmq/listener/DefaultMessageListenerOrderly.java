package com.yufusoft.payplatform.ccenter.mq.rocketmq.listener;

import com.yufusoft.payplatform.ccenter.mq.core.annotations.MessageListenerHandler;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.listener.handler.ListenerHandler;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.listener.handler.ListenerOrderlyHandler;
import com.yufusoft.payplatform.util.common.StringUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 顺序消息监听器
 *
 * @author yzp
 * @date 2018.12.1
 */
public class DefaultMessageListenerOrderly implements MessageListener, MessageListenerOrderly {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private LinkedList<ListenerOrderlyHandler> handlers = new LinkedList<>();

    private Map<String, ListenerOrderlyHandler> handlerMap = new HashMap<>();

    public DefaultMessageListenerOrderly() {
    }

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        context.setAutoCommit(false);
        System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");

        for (MessageExt msg : msgs) {
            try {
                Iterator<ListenerOrderlyHandler> it = handlers.iterator();
                while (it.hasNext()) {
                    ListenerOrderlyHandler handler = it.next();
                    String[] targs = AopUtils.getTargetClass(handler).getAnnotation(MessageListenerHandler.class).targ();
                    if (Arrays.asList(targs).contains(msg.getTags())) {
                        handler.consume(msg, context);
                    }
                }
            } catch (Exception e) {
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }


    @Override
    public void addhandler(ListenerHandler handler) {
        if (handler instanceof ListenerOrderlyHandler) {
            String beanName = AopUtils.getTargetClass(handler).getAnnotation(Component.class).value();
            if (!StringUtil.isEmpty(beanName)) {
                handlerMap.put(beanName, (ListenerOrderlyHandler) handler);
            } else {
                beanName = handler.getClass().getSimpleName();
                char firstChar = beanName.charAt(0);
                if (firstChar >= 'A' && firstChar <= 'Z') {
                    beanName = beanName.replace(firstChar, String.valueOf(firstChar).toLowerCase().charAt(0));
                }
                handlerMap.put(beanName, (ListenerOrderlyHandler) handler);
            }

            handlers.add((ListenerOrderlyHandler) handler);
        }
    }

    @Override
    public void addhandlerBefore(String name, ListenerHandler handler) {
        if (handler instanceof ListenerOrderlyHandler) {
            ListenerOrderlyHandler sourceHandler = handlerMap.get(name);
            if (sourceHandler != null) {
                int index = handlers.indexOf(sourceHandler);
                handlers.add(index, (ListenerOrderlyHandler) handler);
                handlerMap.put(name, (ListenerOrderlyHandler) handler);
            }
        }
    }

    @Override
    public void addhandlerAfter(String name, ListenerHandler handler) {
        if (handler instanceof ListenerOrderlyHandler) {
            ListenerOrderlyHandler sourceHandler = handlerMap.get(name);
            if (sourceHandler != null) {
                int index = handlers.indexOf(sourceHandler);
                handlers.add(index + 1, (ListenerOrderlyHandler) handler);
                handlerMap.put(name, (ListenerOrderlyHandler) handler);
            }
        }
    }

}
