package com.yufusoft.payplatform.ccenter.mq.core.consumer;

import com.yufusoft.payplatform.ccenter.mq.config.Config;
import com.yufusoft.payplatform.ccenter.mq.exceptions.ConsumerException;

/**
 * 消费者抽象类
 *
 * @author yzp
 * @date 2018.12.1
 */
public abstract class AbstractConsumer implements Consumer, Config {

    /**
     * 初始化配置
     *
     * @throws ConsumerException
     */
    protected abstract void initConfig() throws ConsumerException;
}
