package com.yufusoft.payplatform.ccenter.mq.rocketmq;

import com.yufusoft.payplatform.ccenter.mq.config.Config;
import com.yufusoft.payplatform.ccenter.mq.core.producer.AbstractProducer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.ServiceState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class RocketMQAbstractProducer extends AbstractProducer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * rocketMQ client instance
     */
    protected DefaultMQProducer rocketMQProducer;
    protected String nameServerAdress;
    protected String producerGroup;
    protected String createTopicKey;
    protected int defaultTopicQueueNums;
    protected int sendMsgTimeout;
    protected int compressMsgBodyOverHowmuch;
    protected int retryTimesWhenSendFailed;
    protected int retryTimesWhenSendAsyncFailed;
    protected boolean retryAnotherBrokerWhenNotStoreOK;
    protected int maxMessageSize;
    protected String instanceName;
    private Config config;


    public RocketMQAbstractProducer(Config config) {
        this.config = config;
        initConfig();
    }

    @Override
    protected void initConfig() {
        if (this.config == null) {
            logger.error("producer init error,the config is null");
            return;
        }
        if (this.rocketMQProducer != null) {
            logger.error("producer init error,the mirror is not null");
            return;
        }
        this.rocketMQProducer = createProducerMirror();
        Map<String, Object> map = this.config.getConfigMap();
        this.rocketMQProducer.setNamesrvAddr((String) map.get("nameServerAddress"));
        this.rocketMQProducer.setProducerGroup((String) map.get("producerGroup"));
        this.rocketMQProducer.setSendMsgTimeout(Integer.valueOf((String) map.get("sendMsgTimeout")));
        this.rocketMQProducer.setRetryTimesWhenSendFailed(Integer.valueOf((String) map.get("retryTimesWhenSendFailed")));
        this.rocketMQProducer.setRetryAnotherBrokerWhenNotStoreOK(Boolean.valueOf((String) map.get("retryAnotherBrokerWhenNotStoreOK")));
        this.rocketMQProducer.setMaxMessageSize(Integer.valueOf((String) map.get("maxMessageSize")));
        this.rocketMQProducer.setDefaultTopicQueueNums(Integer.valueOf((String) map.get("topicQueueNums")));
        this.rocketMQProducer.setCreateTopicKey((String) map.get("createTopicKey"));
        this.rocketMQProducer.setCompressMsgBodyOverHowmuch(Integer.valueOf((String) map.get("compressMsgBodyOverHowmuch")));
        this.rocketMQProducer.setRetryTimesWhenSendAsyncFailed(Integer.valueOf((String) map.get("retryTimesWhenSendAsyncFailed")));
        this.rocketMQProducer.setInstanceName((String) map.get("instanceName"));
    }

    @Override
    public Object getProducerMirror() {
        return rocketMQProducer;
    }

    @Override
    public void start() {
        try {
            rocketMQProducer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        rocketMQProducer.shutdown();
    }

    @Override
    public boolean isRunning() {
        ServiceState serviceState = rocketMQProducer.getDefaultMQProducerImpl().getServiceState();
        if (serviceState == ServiceState.RUNNING) {
            return true;
        }
        return false;
    }

    @Override
    public Config getConfig() {
        return this.config;
    }

    @Override
    public Map<String, Object> getConfigMap() {
        return this.config.getConfigMap();
    }

    abstract protected DefaultMQProducer createProducerMirror();
}
