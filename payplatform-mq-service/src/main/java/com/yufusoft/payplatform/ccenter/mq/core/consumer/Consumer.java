package com.yufusoft.payplatform.ccenter.mq.core.consumer;

import org.springframework.context.Lifecycle;

/**
 * 消费者者接口
 *
 * @author yzp
 * @date 2018.12.1
 */
public interface Consumer extends Lifecycle {

    /**
     * 获取消费者镜像
     * MQ服务器client实例
     *
     * @return client instance
     */
    Object getConsumerMirror();
}
