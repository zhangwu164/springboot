package com.youedata.config;


import com.youedata.config.factory.ESClientSpringFactory;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 使用javaconfig 配置ES基础配置
 * 工厂启动自动创建相关Bean
 * @author jqx
 *
 */
@Configuration
@ComponentScan(basePackageClasses= ESClientSpringFactory.class)
public class ESConfig {

    @Value("${host}")
    private String host;

    @Value("${port}")
    private String port;

    @Value("${schema}")
    private String schema;

    @Value("${connectNum}")
    private String connectNum;

    @Value("${connectPerRoute}")
    private String connectPerRoute;


    public ESConfig(){
        System.out.println("ESConfig 初始化。。。。。。。。。。");
    }
    @Bean
    public HttpHost httpHost(){
        System.out.println("port:  "+port+"   host:"+host+"    schema:"+schema);
        return new HttpHost(host,Integer.valueOf(port).intValue(),schema);
    }

    @Bean(initMethod="init",destroyMethod="close")
    public ESClientSpringFactory getFactory(){
        return ESClientSpringFactory.build(httpHost(), Integer.valueOf(connectNum), Integer.valueOf(connectPerRoute));
    }

   /* @Bean
    @Scope("singleton")
    public RestClient getRestClient(){
        return getFactory().getClient();
    }*/

    @Bean
    @Scope("singleton")
    public RestHighLevelClient getRHLClient(){
        return getFactory().getRhlClient();
    }
}

