package com.yufusoft.payplatform.ccenter.mq.listener;

import com.yufusoft.payplatform.ccenter.mq.core.annotations.MessageListenerHandler;
import com.yufusoft.payplatform.ccenter.mq.listener.event.RocketMQConsumerLoadEvent;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.RocketMQAbstractPushConsumer;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.config.ConsumerXmlConfig;
import com.yufusoft.payplatform.util.bean.BeanUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * @author yzp
 * @date 2019/2/15
 */
@Component
public class RocketMQConsumerLoadListener implements ApplicationListener<RocketMQConsumerLoadEvent> {

    @Override
    public void onApplicationEvent(RocketMQConsumerLoadEvent event) {
        try {
            //获取标记有ListenerHandler注解的所有handlers
            Map<String, Object> beans = event.getContext().getBeansWithAnnotation(MessageListenerHandler.class);
            //遍历handlers
            for (Map.Entry<String, Object> entry : beans.entrySet()) {
                //获取每个handler的注解中的topic数组
                String[] topics = AopUtils.getTargetClass(entry.getValue()).getAnnotation(MessageListenerHandler.class).topic();
                //遍历客户端列表
                for (RocketMQAbstractPushConsumer consumer : event.getConsumers()) {
                    //获取客户端配置
                    ConsumerXmlConfig config = (ConsumerXmlConfig) consumer.getConfig();
                    //遍历handler的topic数组
                    for (String topic : topics) {
                        //客户端的topic数组与topic匹配
                        if (Arrays.asList(config.getTopic()).contains(topic)) {
                            if (consumer.getConsumerMirror() instanceof DefaultMQPushConsumer) {
                                BeanUtil.setFiledValue(entry.getValue(), "listener", ((DefaultMQPushConsumer) consumer.getConsumerMirror()).getMessageListener());
                            } else {
                                //预留
                                return;
                            }
                            Method method = entry.getValue().getClass().getMethod("registerListener");
                            method.invoke(entry.getValue());
                        }
                    }

                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
