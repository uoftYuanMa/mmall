package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class RedissonManager {
    private Config config =new Config();

    private Redisson redisson = null;

    private static String ip1 = PropertiesUtil.getProperty("redis1.ip");
    private static Integer port1 = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));
    private static String ip2 = PropertiesUtil.getProperty("redis2.ip");
    private static Integer port2 = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    public Redisson getRedisson() {
        return redisson;
    }

    @PostConstruct
    private void init(){
        try {
            config.useSingleServer().setAddress(ip1 + ":" + port1);
            redisson = (Redisson) Redisson.create(config);
            log.info("初始化Redisson结束");
        } catch (Exception e) {
            log.error("Redisson init error", e);
            e.printStackTrace();
        }
    }
}
