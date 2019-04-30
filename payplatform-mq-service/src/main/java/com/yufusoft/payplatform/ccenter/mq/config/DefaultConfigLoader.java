package com.yufusoft.payplatform.ccenter.mq.config;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

public class DefaultConfigLoader extends AbstractConfigLoader{

    @Autowired
    private Set<Config> configs;
    @Autowired
    private Map<String,ConfigResolver> resolvers;

    @Override
    protected void loadToMedium(Config config) {

    }

    @Override
    protected void checkConfig(Config config) {

    }

    @Override
    public Config resolver(Object source) throws Exception {
        return null;
    }

    @Override
    public void load() {

    }
}
