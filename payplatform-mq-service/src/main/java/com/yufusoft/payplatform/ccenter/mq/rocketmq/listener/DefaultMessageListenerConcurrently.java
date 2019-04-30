package com.yufusoft.payplatform.ccenter.mq.rocketmq.listener;

import com.yufusoft.payplatform.ccenter.mq.core.annotations.MessageListenerHandler;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.listener.handler.ListenerConcurrentlyHandler;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.listener.handler.ListenerHandler;
import com.yufusoft.payplatform.util.common.StringUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 同步消息监听器
 *
 * @author yzp
 * @date 2018.12.1
 */
public class DefaultMessageListenerConcurrently implements MessageListener, MessageListenerConcurrently {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private LinkedList<ListenerConcurrentlyHandler> handlers = new LinkedList();

    private Map<String, ListenerConcurrentlyHandler> handlerMap = new HashMap<>();

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {

        if (null != list && !list.isEmpty()) {
            for (MessageExt msg : list) {
                try {
                    Iterator<ListenerConcurrentlyHandler> it = handlers.iterator();
                    while (it.hasNext()) {
                        ListenerConcurrentlyHandler handler = it.next();
                        String[] targs = AopUtils.getTargetClass(handler).getAnnotation(MessageListenerHandler.class).targ();
                        if (Arrays.asList(targs).contains(msg.getTags())) {
                            handler.consume(msg, context);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }


    @Override
    public void addhandler(ListenerHandler handler) {
        if (handler instanceof ListenerConcurrentlyHandler) {
            String beanName = AopUtils.getTargetClass(handler).getAnnotation(Component.class).value();
            if (!StringUtil.isEmpty(beanName)) {
                handlerMap.put(beanName, (ListenerConcurrentlyHandler) handler);
            } else {
                beanName = handler.getClass().getSimpleName();
                char firstChar = beanName.charAt(0);
                if (firstChar >= 'A' && firstChar <= 'Z') {
                    beanName = beanName.replace(firstChar, String.valueOf(firstChar).toLowerCase().charAt(0));
                }
                handlerMap.put(beanName, (ListenerConcurrentlyHandler) handler);
            }
            handlers.add((ListenerConcurrentlyHandler) handler);
        }
    }

    @Override
    public void addhandlerBefore(String name, ListenerHandler handler) {
        if (handler instanceof ListenerConcurrentlyHandler) {
            ListenerConcurrentlyHandler sourceHandler = handlerMap.get(name);
            if (sourceHandler != null) {
                int index = handlers.indexOf(sourceHandler);
                handlers.add(index, (ListenerConcurrentlyHandler) handler);
                handlerMap.put(name, (ListenerConcurrentlyHandler) handler);
            }
        }
    }

    @Override
    public void addhandlerAfter(String name, ListenerHandler handler) {
        if (handler instanceof ListenerConcurrentlyHandler) {
            ListenerConcurrentlyHandler sourceHandler = handlerMap.get(name);
            if (sourceHandler != null) {
                int index = handlers.indexOf(sourceHandler);
                handlers.add(index + 1, (ListenerConcurrentlyHandler) handler);
                handlerMap.put(name, (ListenerConcurrentlyHandler) handler);
            }
        }
    }
}
