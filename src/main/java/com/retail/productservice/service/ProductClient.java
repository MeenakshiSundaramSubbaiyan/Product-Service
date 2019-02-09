package com.retail.productservice.service;

import com.retail.productservice.vo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class ProductClient {

    Logger logger = Logger.getLogger(ProductClient.class.getName());

    public static final String REDSKY_PRODUCT_ENDPOINT_URI = "https://redsky.target.com/v1/pdp/tcin/{id}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";

    @Autowired
    private RestTemplate restTemplate;


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
        }
        return productName;
    }

    public HttpEntity<Object> getRequestEntity() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        return requestEntity;
    }
}
