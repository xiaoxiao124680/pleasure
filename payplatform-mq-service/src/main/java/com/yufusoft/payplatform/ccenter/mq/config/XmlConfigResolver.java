package com.yufusoft.payplatform.ccenter.mq.config;


import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * XML config 解析器
 *
 * @author yzp
 * @date 2018.12.1
 */
public abstract class XmlConfigResolver implements ConfigResolver {

    @Override
    public Config resolver(Object source) throws Exception {
        if (!(source instanceof File)) {
            throw new Exception();
        }
        return resolver((File) source);
    }

    /**
     * 根据属性生成对应类型的对象
     * @param field
     * @param value
     * @return
     * @throws Exception
     */
    protected Object getObjectField(Field field, String value) throws Exception {
        Class typeClass = field.getType();
        Constructor con = typeClass.getConstructor(value.getClass());
        return con.newInstance(value);
    }


    public abstract XmlConfig resolver(File xmlFile) throws Exception;


}
