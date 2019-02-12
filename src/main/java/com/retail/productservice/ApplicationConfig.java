package com.retail.productservice;

import ch.qos.logback.access.tomcat.LogbackValve;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;
import com.retail.productservice.repository.ProductRepository;
import com.retail.productservice.vo.Price;
import com.retail.productservice.vo.ProductResponse;
import org.apache.catalina.valves.AccessLogValve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Locale;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 *Java Configuration class to define beans and their dependencies
 */
@Configuration
public class ApplicationConfig {

    private final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    @Autowired
    ProductRepository productRepository;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    /**
     * Bean definition to to populate the data in Mongo DB with sample Product ID
     * with price information during application startup
     * @return
     */
    @Bean
    CommandLineRunner runner(){
        logger.info("Prepopulating mongo db for product price values");
        return args -> {
            productRepository.save(new ProductResponse(13860416,
                    new Price(BigDecimal.valueOf(5.67), Currency.getInstance(Locale.getDefault()).getCurrencyCode())));
            productRepository.save(new ProductResponse(13860418,
                    new Price(BigDecimal.valueOf(4.54), Currency.getInstance(Locale.getDefault()).getCurrencyCode())));
            productRepository.save(new ProductResponse(13860420,
                    new Price(BigDecimal.valueOf(5.34), Currency.getInstance(Locale.getDefault()).getCurrencyCode())));
            productRepository.save(new ProductResponse(13860421,
                    new Price(BigDecimal.valueOf(11.99), Currency.getInstance(Locale.getDefault()).getCurrencyCode())));
            productRepository.save(new ProductResponse(13860424,
                    new Price(BigDecimal.valueOf(12.99), Currency.getInstance(Locale.getDefault()).getCurrencyCode())));
            productRepository.save(new ProductResponse(13860425,
                    new Price(BigDecimal.valueOf(5.99), Currency.getInstance(Locale.getDefault()).getCurrencyCode())));
            productRepository.save(new ProductResponse(13860428,
                    new Price(BigDecimal.valueOf(7.89), Currency.getInstance(Locale.getDefault()).getCurrencyCode())));

        };
    }

    /**
     * Method to override the default tomcat access log and instead point to logback-access xml for configuration
     * @return
     */
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();

        LogbackValve logbackValve = new LogbackValve();
        logbackValve.setFilename("logback-access.xml");

        AccessLogValve accessLogValve = new AccessLogValve();
        accessLogValve.setEnabled(false);
        accessLogValve.setRenameOnRotate(false);
        tomcatServletWebServerFactory.addContextValves(accessLogValve);
        tomcatServletWebServerFactory.addContextValves(logbackValve);

        return tomcatServletWebServerFactory;
    }

    /**
     * Method to define the Swagger docs specification for the product service
     * @return
     */
    @Bean
    public Docket productDetailsApi() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework")))
                .paths(PathSelectors.any()).build().directModelSubstitute(LocalDate.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .alternateTypeRules(newRule(
                        typeResolver.resolve(DeferredResult.class,
                                typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                        typeResolver.resolve(WildcardType.class)))
                .useDefaultResponseMessages(false).enableUrlTemplating(true)
                .tags(new Tag("Product Service",
                        "REST API to fetch/update the product details from Redsky and Mongo DB"));
    }

    @Autowired
    private TypeResolver typeResolver;

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder().deepLinking(true).displayOperationId(false).defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1).defaultModelRendering(ModelRendering.EXAMPLE).displayRequestDuration(true)
                .docExpansion(DocExpansion.NONE).filter(false).maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA).showExtensions(false).tagsSorter(TagsSorter.ALPHA)
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS).validatorUrl(null).build();
    }


}
