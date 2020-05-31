package ru.iesorokin.ordermanager.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration for Swagger and Swagger UI
 * <p>
 * You can try it via url /swagger-ui.html
 */
@Configuration
@EnableSwagger2
@Profile("!prod")
public class SwaggerConfig {
    @Value("${build.version}")
    private String buildVersion;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                       .useDefaultResponseMessages(false)
                       .apiInfo(getApiInfo())
                       .select()
                       .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                       .paths(PathSelectors.any())
                       .build()
                       .pathMapping("/om");
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                       .title("Order manager gateway API Documentation")
                       .description("Documentation generated automatically")
                       .version(buildVersion)
                       .contact(ApiInfo.DEFAULT_CONTACT)
                       .build();
    }
}
