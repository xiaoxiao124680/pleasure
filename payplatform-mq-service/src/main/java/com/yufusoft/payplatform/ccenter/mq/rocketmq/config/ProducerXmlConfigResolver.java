package com.yufusoft.payplatform.ccenter.mq.rocketmq.config;

import com.yufusoft.payplatform.ccenter.mq.config.XmlConfig;
import com.yufusoft.payplatform.ccenter.mq.config.XmlConfigResolver;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 生产者xml转换器
 * @author yzp
 * @date 2018.12.1
 */
@Component("producerXmlConfigResolver")
public class ProducerXmlConfigResolver extends XmlConfigResolver {

    @Override
    public XmlConfig resolver(File xmlFile) throws Exception {
        try {
            ProducerXmlConfig producerXmlConfig = new ProducerXmlConfig();
            SAXReader reader = new SAXReader();
            Document document = reader.read(xmlFile);
            List<ProducerXmlConfig> list = new ArrayList<>();
            producerXmlConfig.setList(list);
            List<Element> elements = document.getRootElement().elements();
            for (Element element : elements) {
                ProducerXmlConfig config = new ProducerXmlConfig();
                config.setId(element.attributeValue("id"));
                config.setClassName(element.attributeValue("class"));
                //可以递归赋值复杂变量，待优化
                List<Element> attributes = element.element("attributes").elements("attribute");
                for (Element attribute : attributes) {
                    Field field = config.getClass().getDeclaredField(attribute.attributeValue("name"));
                    if (field != null) {
                        field.setAccessible(true);
                        field.set(config, getObjectField(field, attribute.attributeValue("value")));
                    }
                }
                list.add(config);
            }
            return producerXmlConfig;
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
