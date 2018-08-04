package com.ibm.cio.sets.procurement.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.cio.sets.procurement.identify.IdentityFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Bean
    IdentityFilter identityFilter() {
        return new IdentityFilter();
    }

    @Bean
    FilterRegistrationBean identityRegistration() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(identityFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("identityFilter");
        return filterRegistrationBean;
    }

    @Primary
    @Bean("objectMapper")
    Jackson2ObjectMapperFactoryBean mapperFactory() {
        Jackson2ObjectMapperFactoryBean mapperFactory = new Jackson2ObjectMapperFactoryBean();
        mapperFactory.setIndentOutput(true);
        mapperFactory.setFailOnEmptyBeans(false);
        mapperFactory.setAutoDetectFields(false);
        mapperFactory.setObjectMapper(new ObjectMapper());
        return mapperFactory;
    }
}
