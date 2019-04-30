package com.yufusoft.payplatform.ccenter.mq.rocketmq.config;

import com.yufusoft.payplatform.ccenter.mq.config.XmlConfig;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 生产者的xml配置类
 *
 * @author yzp
 * @date 2018.12.1
 */
public class ProducerXmlConfig extends XmlConfig {

    private String id;
    private String className;
    private Boolean isAvailable;
    private String nameServerAddress;
    private String producerGroup;
    private String instanceName;
    private String createTopicKey;
    private String topicQueueNums;
    private String sendMsgTimeout;
    private String compressMsgBodyOverHowmuch;
    private String retryTimesWhenSendFailed;
    private String retryTimesWhenSendAsyncFailed;
    private String retryAnotherBrokerWhenNotStoreOK;
    private String maxMessageSize;
    private File sourceFile;
    private Map<String, Object> map;
    private List<ProducerXmlConfig> list;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public String getNameServerAddress() {
        return nameServerAddress;
    }

    public void setNameServerAddress(String nameServerAddress) {
        this.nameServerAddress = nameServerAddress;
    }

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getCreateTopicKey() {
        return createTopicKey;
    }

    public void setCreateTopicKey(String createTopicKey) {
        this.createTopicKey = createTopicKey;
    }

    public String getTopicQueueNums() {
        return topicQueueNums;
    }

    public void setTopicQueueNums(String topicQueueNums) {
        this.topicQueueNums = topicQueueNums;
    }

    public String getSendMsgTimeout() {
        return sendMsgTimeout;
    }

    public void setSendMsgTimeout(String sendMsgTimeout) {
        this.sendMsgTimeout = sendMsgTimeout;
    }

    public String getCompressMsgBodyOverHowmuch() {
        return compressMsgBodyOverHowmuch;
    }

    public void setCompressMsgBodyOverHowmuch(String compressMsgBodyOverHowmuch) {
        this.compressMsgBodyOverHowmuch = compressMsgBodyOverHowmuch;
    }

    public String getRetryTimesWhenSendFailed() {
        return retryTimesWhenSendFailed;
    }

    public void setRetryTimesWhenSendFailed(String retryTimesWhenSendFailed) {
        this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
    }

    public String getRetryTimesWhenSendAsyncFailed() {
        return retryTimesWhenSendAsyncFailed;
    }

    public void setRetryTimesWhenSendAsyncFailed(String retryTimesWhenSendAsyncFailed) {
        this.retryTimesWhenSendAsyncFailed = retryTimesWhenSendAsyncFailed;
    }

    public String getRetryAnotherBrokerWhenNotStoreOK() {
        return retryAnotherBrokerWhenNotStoreOK;
    }

    public void setRetryAnotherBrokerWhenNotStoreOK(String retryAnotherBrokerWhenNotStoreOK) {
        this.retryAnotherBrokerWhenNotStoreOK = retryAnotherBrokerWhenNotStoreOK;
    }

    public String getMaxMessageSize() {
        return maxMessageSize;
    }

    public void setMaxMessageSize(String maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public List<ProducerXmlConfig> getList() {
        return list;
    }

    public void setList(List<ProducerXmlConfig> list) {
        this.list = list;
    }
}
