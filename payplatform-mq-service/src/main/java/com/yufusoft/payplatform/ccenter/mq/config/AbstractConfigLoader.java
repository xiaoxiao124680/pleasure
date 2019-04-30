package com.yufusoft.payplatform.ccenter.mq.config;

/**
 * 抽象配置加载器
 *
 * @author yzp
 * @date 2018.12.1
 */
public abstract class AbstractConfigLoader implements ConfigLoader, ConfigResolver {


    @Override
    public void load(Config config) {

        checkConfig(config);
        loadToMedium(config);
    }

    /**
     * 加载配置到介质 like memory,disk,db,cache,file,....
     *
     * @param config
     */
    protected abstract void loadToMedium(Config config);

    /**
     * 检查配置
     *
     * @param config
     */
    protected abstract void checkConfig(Config config);


}
