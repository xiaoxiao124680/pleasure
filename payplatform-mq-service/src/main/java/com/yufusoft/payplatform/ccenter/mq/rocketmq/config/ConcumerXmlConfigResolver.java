package com.yufusoft.payplatform.ccenter.mq.rocketmq.config;

import com.yufusoft.payplatform.ccenter.mq.config.XmlConfig;
import com.yufusoft.payplatform.ccenter.mq.config.XmlConfigResolver;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 消费者xml转化器
 *
 * @author yzp
 * @date 2018.12.1
 */
@Component("concumerXmlConfigResolver")
public class ConcumerXmlConfigResolver extends XmlConfigResolver {

    private static ApplicationContext context;

    private static final String MESSAGE_LISTENER_CONCURRENTLY_CLASSNAME = "com.yufusoft.payplatform.ccenter.mq.rocketmq.listener.DefaultMessageListenerConcurrently";

    private static final String MESSAGE_LISTENER_ORDERLY_CLASSNAME = "com.yufusoft.payplatform.ccenter.mq.rocketmq.listener.DefaultMessageListenerOrderly";

    @Override
    public XmlConfig resolver(File xmlFile) throws Exception {
        try {
            ConsumerXmlConfig consumerXmlConfig = new ConsumerXmlConfig();
            SAXReader reader = new SAXReader();
            Document document = reader.read(xmlFile);
            List<ConsumerXmlConfig> list = new ArrayList<>();
            consumerXmlConfig.setList(list);
            List<Element> elements = document.getRootElement().elements();
            for (Element element : elements) {
                ConsumerXmlConfig config = new ConsumerXmlConfig();
                config.setId(element.attributeValue("id"));
                config.setClassName(element.attributeValue("class"));
                //可以递归赋值复杂变量，待优化
                List<Element> attributes = element.element("attributes").elements("attribute");
                for (Element attribute : attributes) {
                    Field field = config.getClass().getDeclaredField(attribute.attributeValue("name"));
                    if (field != null) {
                        field.setAccessible(true);
                        if ("messageListener".equals(attribute.attributeValue("name"))) {
                            String listenerType = attribute.attributeValue("type");
                            if ("concurrently".equals(listenerType)) {
                                field.set(config, Class.forName(MESSAGE_LISTENER_CONCURRENTLY_CLASSNAME).newInstance());
                            } else if ("orderly".equals(listenerType)) {
                                field.set(config, Class.forName(MESSAGE_LISTENER_ORDERLY_CLASSNAME).newInstance());
                            }
                        } else if ("topic".equals(attribute.attributeValue("name"))) {
                            config.setTopic(attribute.attributeValue("value").split(","));
                        } else if ("targ".equals(attribute.attributeValue("name"))) {
                            config.setTarg(attribute.attributeValue("value").split(","));
                        } else {
                            field.set(config, getObjectField(field, attribute.attributeValue("value")));
                        }
                    }
                }
                list.add(config);
            }
            return consumerXmlConfig;
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}
