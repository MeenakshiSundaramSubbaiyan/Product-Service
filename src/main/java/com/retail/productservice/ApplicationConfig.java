package com.retail.productservice;

import com.retail.productservice.repository.ProductRepository;
import com.retail.productservice.vo.Price;
import com.retail.productservice.vo.ProductResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

@Configuration
public class ApplicationConfig {

    private final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    @Autowired
    ProductRepository productRepository;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder.build();
    }

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
}
