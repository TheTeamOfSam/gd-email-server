package com.sam.graduation.design.gdemailserver.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author sam199510 273045049@qq.com
 * @version 创建时间：2018/1/29 14:10:57
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration implements ApplicationContextAware {

    @Autowired
    private Environment environment;
    @Bean
    public Docket createRestApi() {
        Docket docket =  new Docket(DocumentationType.SWAGGER_2);
        docket.groupName(this.environment.getProperty("info.name")).apiInfo(apiInfo());
        ApiSelectorBuilder selector =  docket.select();
        selector.apis(RequestHandlerSelectors.basePackage("com.sam.graduation.design.gdemailserver.controller"));
        selector.paths(PathSelectors.any());
        return selector.build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(environment.getProperty("info.component"))
                .version("v0.0.1")
                .build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //force the bean to get loaded as soon as possible
        applicationContext.getBean("requestMappingHandlerAdapter");
    }

}
