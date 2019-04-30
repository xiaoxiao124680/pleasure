package com.yufusoft.payplatform.ccenter.mq.config;

import java.util.Map;

/**
 * 资源配置接口
 *
 * @author yzp
 * @date 2018.12.1
 */
public interface Config {

    /**
     * 获取配置
     *
     * @return
     */
    Config getConfig();

    /**
     * 获取K-V形式的config
     *
     * @return map
     */
    Map<String, Object> getConfigMap();
}
