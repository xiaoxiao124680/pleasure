package com.yufusoft.payplatform.ccenter.mq.config;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class XmlConfig implements FileConfig {

    private File sourceFile;
    private Map<String, Object> map;


    @Override
    public File getFileConfig() {
        return this.sourceFile;
    }

    @Override
    public Config getConfig() {
        return this;
    }

    @Override
    public Map<String, Object> getConfigMap() {
        if (this.map == null) {
            this.map = new HashMap<>();
            Field[] fields = getClass().getDeclaredFields();
            try {
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    map.put(fields[i].getName(), fields[i].get(this));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return this.map;
    }
}
