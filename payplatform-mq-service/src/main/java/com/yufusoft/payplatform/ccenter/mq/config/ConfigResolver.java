package com.yufusoft.payplatform.ccenter.mq.config;

/**
 * 资源转换器
 *
 * @author yzp
 * @date 2018.12.1
 */
public interface ConfigResolver {

    /**
     * 转化配置
     *
     * @param source all files ,like propertis,xml,dbBean,TDO,...
     * @return Config
     */
    Config resolver(Object source) throws Exception;
}
