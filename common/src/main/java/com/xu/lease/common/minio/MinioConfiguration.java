package com.xu.lease.common.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableConfigurationProperties(MinioProperties.class)  等同下面的方法
@ConfigurationPropertiesScan("com.xu.lease.common.minio")
public class MinioConfiguration {
//    第一种自定义配置注入配置类方法
//    @Value("${minio.endpoint}")
//    private  String endpoint;

    @Autowired
    private  MinioProperties properties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder().endpoint(properties.getEndpoint()).credentials(properties.getAccessKey(), properties.getSecretKey()).build();
    }



}
