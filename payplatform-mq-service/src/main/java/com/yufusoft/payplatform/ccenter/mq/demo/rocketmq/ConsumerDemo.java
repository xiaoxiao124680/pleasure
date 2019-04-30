package com.yufusoft.payplatform.ccenter.mq.demo.rocketmq;

import com.yufusoft.payplatform.ccenter.mq.core.annotations.MessageListenerHandler;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.listener.handler.ListenerConcurrentlyHandler;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.listener.handler.ListenerOrderlyHandler;
import com.yufusoft.payplatform.util.common.GsonUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author yzp
 * @date 2019/4/1
 * 1.消费者/集群项目引入maven依赖
 * <dependency>
 * <groupId>com.yufusoft.payplatform</groupId>
 * <artifactId>payplatform-mq-service</artifactId>
 * <version>0.0.1</version>
 * </dependency>
 * 2.配置消费者服务信息
 * 参考resources/RocketMQConsumerConfigDemo.xml
 * 通配符*RocketMQConsumerConfig*
 * 置于resource根目录或子目录下均可
 * 3.实现自己的MessageListener
 */
public class ConsumerDemo {

    /**
     * 标准消息监听器
     * 配置文件中配置messageListener为concurrently
     * 配置此监听器监听的主题和标识 topic，targ
     * consumer会根据配置文件中的主题与标识和监听器配置的主题与标识取交际来分发消息
     * 需将本监听器实例注册到监听处理列表
     * getListener().addhandler(this);
     */
    @Component("myConcurrentlyListener")
    @MessageListenerHandler(topic = "TopicTest", targ = {"TagA", "TagB"})
    public class MessageListenerConcurrently extends ListenerConcurrentlyHandler implements Serializable {

        @Override
        public void registerListener() {
            getListener().addhandler(this);
        }

        @Override
        public void consume(MessageExt message, ConsumeConcurrentlyContext context) {
            System.out.println(new String(message.getBody()));
            System.out.println(GsonUtil.objToJson(message));
        }

    }


    /**
     * 顺序消息监听
     * 配置文件中配置messageListener为orderly
     * 配置此监听器监听的主题和标识 topic，targ
     * consumer会根据配置文件中的主题与标识和监听器配置的主题与标识取交际来分发消息
     * 需将本监听器实例注册到监听处理列表
     * getListener().addhandler(this);
     */
    @Component
    @MessageListenerHandler(topic = "TopicTest,TopicTest", targ = {"TagC"})
    public class MessageListenerOrderly extends ListenerOrderlyHandler {


        @Override
        public void registerListener() {
            getListener().addhandler(this);
        }

        @Override
        public void consume(MessageExt message, ConsumeOrderlyContext context) {
            System.out.println(GsonUtil.objToJson(message));
        }
    }
}
