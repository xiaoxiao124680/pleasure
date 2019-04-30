package com.yufusoft.payplatform.ccenter.mq.config;

/**
 * 配置加载器
 *
 * @author yzp
 * @date 2018.12.1
 */
public interface ConfigLoader extends Loader {

    /**
     * 根据配置加载
     *
     * @param config
     */
    void load(Config config);
}
