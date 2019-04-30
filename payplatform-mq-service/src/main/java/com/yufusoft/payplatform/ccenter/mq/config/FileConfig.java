package com.yufusoft.payplatform.ccenter.mq.config;

import java.io.File;

/**
 * 文件资源配置接口
 *
 * @author yzp
 * @date 2018.12.1
 */
public interface FileConfig extends Config {

    /**
     * 获取文件类型的配置
     * @return
     */
    File getFileConfig();
}
