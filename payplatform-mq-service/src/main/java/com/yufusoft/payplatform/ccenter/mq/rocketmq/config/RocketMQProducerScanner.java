package com.yufusoft.payplatform.ccenter.mq.rocketmq.config;

import org.springframework.beans.factory.annotation.Value;

public class RocketMQProducerScanner extends RocketMQXmlProducerLoader implements Runnable{

    @Value("#{configProperties['rocketmqProducerScanIntervalTime']}")
    private int scanIntervalTime;

    @Override
    public void run() {
        try {
            while (true){
                Thread.sleep(scanIntervalTime);
                load();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
