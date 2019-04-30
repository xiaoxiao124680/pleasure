package com.yufusoft.payplatform.ccenter.mq.demo.rocketmq;

import com.yufusoft.payplatform.ccenter.mq.bo.rocketmq.OrderMessage;
import com.yufusoft.payplatform.ccenter.mq.bo.rocketmq.StandardMessage;
import com.yufusoft.payplatform.ccenter.mq.bo.rocketmq.TransactionMessage;
import com.yufusoft.payplatform.ccenter.mq.consts.MessageModel;
import com.yufusoft.payplatform.ccenter.mq.exceptions.ProducerException;
import com.yufusoft.payplatform.ccenter.mq.rocketmq.RocketMQMessageSender;
import com.yufusoft.payplatform.util.common.GsonUtil;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author yzp
 * @date 2019/4/1
 * 1.生产者/集群项目引入maven依赖
 * <dependency>
 * <groupId>com.yufusoft.payplatform</groupId>
 * <artifactId>payplatform-mq-service</artifactId>
 * <version>0.0.1</version>
 * </dependency>
 * 2.配置生产者服务信息
 * 参考resources/RocketMQProducerConfigDemo.xml
 * 通配符*RocketMQProducerConfig*
 * 置于resource根目录或子目录下均可
 * 3.spring注入
 * @Autowired private RocketMQMessageSender sender;
 */
public class ProducerDemo {

    @Autowired
    private RocketMQMessageSender sender;

    //发送普通消息
    public void sendStandardMessage() {
        StandardMessage standardMessage = new StandardMessage("defaultStandardProducer", "TopicTest", "TagA", "key", "測試1");
        standardMessage.setMessageModel(MessageModel.SYNCH);
        try {
            SendResult result = sender.send(standardMessage);
        } catch (ProducerException e) {
            e.printStackTrace();
        }
    }

    //发送单向消息
    public void sendOneWayMessage() {
        StandardMessage standardMessage = new StandardMessage("defaultStandardProducer", "TopicTest", "TagA", "key", "測試1");
        standardMessage.setMessageModel(MessageModel.ONEWAY);
        try {
            SendResult result = sender.send(standardMessage);
        } catch (ProducerException e) {
            e.printStackTrace();
        }
    }

    //发送异步消息
    public void sendAsynchMessage() {
        StandardMessage standardMessage = new StandardMessage("defaultStandardProducer", "TopicTest", "TagA", "key", "測試1");
        standardMessage.setMessageModel(MessageModel.ASYNCH);
        standardMessage.setCallbackMethod(new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println(GsonUtil.objToJson(sendResult));
            }

            @Override
            public void onException(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        try {
            sender.send(standardMessage);
        } catch (ProducerException e) {
            e.printStackTrace();
        }

    }

    //发送顺序消息(同步)
    public void sendSynchOrderMessage() {
        for (int i = 0; i < 13; i++) {
            OrderMessage orderMessage = new OrderMessage("defaultStandardProducer", "TopicTest", "TagA", "key", "測試" + i);
            orderMessage.setMessageModel(MessageModel.SYNCH);
            Integer orderId = i % 4;
            orderMessage.setSelector(new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                    Integer id = (Integer) o;
                    int index = id % list.size();
                    return list.get(index);
                }
            });
            orderMessage.setArgs(orderId);
            try {
                SendResult result = sender.send(orderMessage);
            } catch (ProducerException e) {
                e.printStackTrace();
            }
        }
    }

    //发送顺序消息(异步)
    public void sendAsynchOrderMessage() {
        for (int i = 0; i < 13; i++) {
            OrderMessage orderMessage = new OrderMessage("defaultStandardProducer", "TopicTest", "TagA", "key", "測試" + i);
            orderMessage.setMessageModel(MessageModel.ASYNCH);
            Integer orderId = i % 4;
            orderMessage.setSelector(new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                    Integer id = (Integer) o;
                    int index = id % list.size();
                    return list.get(index);
                }
            });
            orderMessage.setCallbackMethod(new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println(GsonUtil.objToJson(sendResult));
                }

                @Override
                public void onException(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
            orderMessage.setArgs(orderId);
            try {
                sender.send(orderMessage);
            } catch (ProducerException e) {
                e.printStackTrace();
            }
        }
    }


    //发送事务消息
    public void sendTransactionMessage() {
        TransactionMessage transactionMessage = new TransactionMessage("defaultTransactionProducer", "TopicTest", "TagA", "key", "測試事务消息");
        transactionMessage.setArgs(null);
        try {
            SendResult result = sender.send(transactionMessage);
        } catch (ProducerException e) {
            e.printStackTrace();
        }
    }

}
