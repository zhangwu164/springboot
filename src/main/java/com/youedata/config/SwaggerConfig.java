package com.youedata.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "daas-switch", name = "swagger-open", havingValue = "true")
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        List<Parameter> aParameters = new ArrayList<Parameter>();
//        ParameterBuilder aParameterBuilder = new ParameterBuilder();
//        aParameterBuilder.name("accessToken").defaultValue("d11d29fbaee74a379a694ce68396bc6a")
//                .description("accessToken").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
//        aParameters.add(aParameterBuilder.build());
//        aParameterBuilder = new ParameterBuilder();
//        aParameterBuilder.name("accountId").defaultValue("21d69a98a397458498cd3a9856a4c013")
//                .description("accountId").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
//        aParameters.add(aParameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalOperationParameters(aParameters)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.youedata"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Daas Docs")
                .description("Daas Api文档")
                .contact("daas Developers")
                .version("2.0")
                .build();
    }

}
