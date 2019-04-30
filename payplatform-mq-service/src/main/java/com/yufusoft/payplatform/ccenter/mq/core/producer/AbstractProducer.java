package com.yufusoft.payplatform.ccenter.mq.core.producer;

import com.yufusoft.payplatform.ccenter.mq.config.Config;

/**
 * 生产者抽象类
 *
 * @author yzp
 * @date 2018.12.1
 */
public abstract class AbstractProducer implements Producer, Config {

    /**
     * 初始化配置
     */
    protected abstract void initConfig();


}
