package com.yufusoft.payplatform.ccenter.mq.bo.rocketmq;

import com.yufusoft.payplatform.ccenter.mq.bo.MQMessage;
import com.yufusoft.payplatform.util.common.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * rocketMQ抽象消息类
 *
 * @author yzp
 * @date 2018.11.30
 */
public abstract class AbstractRocketMQMessage implements MQMessage {


    /**
     * 生产者集合
     * 生产同一类消息的生产者
     */
    private String group;

    /**
     * 消息主题
     */
    private String topic;

    /**
     * 用于过滤消息
     */
    private String tag;

    /**
     * 用于查询消息
     */
    private String key;

    /**
     * 消息内容
     */
    private String content;


    private Map<String, Object> map = new HashMap<>();


    public AbstractRocketMQMessage(String group, String topic, String tag, String key, String content) {
        this.group = group;
        this.topic = topic;
        this.tag = tag;
        this.key = key;
        this.content = content;
    }


    public AbstractRocketMQMessage() {
    }

    @Override
    public boolean checkParams() {
        if (StringUtil.isEmpty(group) && StringUtil.isEmpty(topic) && StringUtil.isEmpty(key) && StringUtil.isEmpty(tag)) {
            return true;
        }else{
            return false;
        }
    }


    @Override
    public Map<String, Object> getParams() {

        if (this.map.isEmpty()) {
            this.map.put("group", group);
            this.map.put("topic", topic);
            this.map.put("tag", tag);
            this.map.put("key", key);
            this.map.put("content", content);
        }

        return this.map;
    }

    @Override
    public Object get(String name) {
        return getParams().get(name);
    }

    public String getGroup() {
        return group;
    }

    public String getTopic() {
        return topic;
    }

    public String getTag() {
        return tag;
    }

    public String getKey() {
        return key;
    }

    public String getContent() {
        return content;
    }

}
