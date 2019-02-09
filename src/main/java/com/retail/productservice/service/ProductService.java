package com.retail.productservice.service;

import com.retail.productservice.vo.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public interface ProductService {

    /**
     * Fetch the product details from RedSky endpoint and Price details from Mongo DB
     * @param productId
     * @return
     */
    ProductResponse getProductInfo(String productId);

}
