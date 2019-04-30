package com.yufusoft.payplatform.ccenter.mq.bo;

import com.yufusoft.payplatform.ccenter.mq.consts.MessageType;

import java.io.Serializable;
import java.util.Map;

/**
 * 发送消息参数类
 * @author yzp
 * @date 2018.11.30
 */
public interface MQMessage extends Serializable {

    boolean checkParams();

    MessageType getMessageType();

    Map<String,Object> getParams();

    Object get(String name);

}
