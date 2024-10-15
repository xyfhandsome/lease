package com.xu.lease.web.admin.config.mybatisplusconfig;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringBootConfiguration;

@SpringBootConfiguration
@MapperScan("com.xu.lease.web.admin.mapper")
public class MybatisPlusConfig {
}
