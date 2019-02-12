package com.retail.productservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.retail.productservice.exception.ProductNotFoundException;
import com.retail.productservice.vo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Wrapperclass to retrieve the ProductName from Redsky service using RestTemplate
 */
@Component
public class ProductClient {

    Logger logger = Logger.getLogger(ProductClient.class.getName());

    public static final String REDSKY_PRODUCT_ENDPOINT_URI = "https://redsky.target.com/v1/pdp/tcin/{id}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    private RestTemplate restTemplate;


    /**
     * Method to invoke the Redsky service to fetch product details for the given ProductID
     * @param productId
     * @return
     */
    @HystrixCommand(fallbackMethod = "handleServiceFailure", commandProperties = { @HystrixProperty(name =
            "execution.isolation.thread.timeoutInMilliseconds", value = "3000")})
    public String getProductNameById(Long productId){
        String productName = null;
        try{
            Map<String, Long> uriVariables = new HashMap<>();
            uriVariables.put("id", productId);
            ResponseEntity<Product> result = restTemplate.exchange(REDSKY_PRODUCT_ENDPOINT_URI,HttpMethod.GET,getRequestEntity(),Product.class,uriVariables);
            Product product = result.getBody();
            productName = product.getName();
        }catch (HttpClientErrorException e){
            logger.warning("Exception occured while calling the Redsky service:"+e.getMessage());
            if (e.getMessage().equalsIgnoreCase("404 Not Found")) {
                throw new ProductNotFoundException(productId);
            }
        }
        return productName;
    }

    public HttpEntity<Object> getRequestEntity() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        return requestEntity;
    }

    public String handleServiceFailure(Long ProductId, Throwable e){
        logger.warning("CircuitBreaker enabled : Inside handleServiceFailure:"+e.getMessage());
        return null;
    }
}
