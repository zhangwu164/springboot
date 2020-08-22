package com.youedata.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>Description:
 *
 * </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company:成都优易数据有限公司</p>
 *
 * @author wgj
 * @version V1.0
 * @PackageName:com.youedata.config
 * @ProjectName:SpringBootTemp
 * @date 2019/9/17 17/06
 */
@Component
@Configuration
public class Config implements Serializable {
    private Logger logger = LoggerFactory.getLogger(Config.class);

    @Value("${server.port}")
    private String serverPort;

    @Value("${quartz.cron.test}")
    private String cronTest;


    public String getCronTest() {
        return cronTest;
    }

    public void setCronTest(String cronTest) {
        this.cronTest = cronTest;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }
}
