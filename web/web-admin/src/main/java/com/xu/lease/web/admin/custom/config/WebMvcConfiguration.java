package com.xu.lease.web.admin.custom.config;

import com.xu.lease.web.admin.custom.converter.StringToBaseEnumConverterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//配置
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    StringToBaseEnumConverterFactory stringToBaseEnumConverterFactory;
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(stringToBaseEnumConverterFactory);
    }
}
