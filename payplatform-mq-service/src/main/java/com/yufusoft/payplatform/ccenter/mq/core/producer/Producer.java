package com.yufusoft.payplatform.ccenter.mq.core.producer;

import org.springframework.context.Lifecycle;

/**
 * 生产者接口
 * @author yzp
 * @date 2018.12.1
 */
public interface Producer extends Lifecycle {

    /**
     * 获取生产者镜像
     * MQ服务器client实例
     * @return client instance
     */
    Object getProducerMirror();

}
