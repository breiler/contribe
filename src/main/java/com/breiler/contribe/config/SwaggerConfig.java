package com.breiler.contribe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.breiler"))
                .paths(regex("/.*"))
                .build();
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo("REST api documentation for book store",
                "This is the REST api documentation for a book store. It has support for creating books and managing its stock. A user can also create a cart and fill it with books and be used to create an order.",
                "1.0",
                "urn:tos",
                (Contact) null,
                "GPLv3", "https://www.gnu.org/licenses/gpl-3.0.en.html");
    }
}