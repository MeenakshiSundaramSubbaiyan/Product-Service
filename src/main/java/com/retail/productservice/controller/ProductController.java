package com.retail.productservice.controller;

import com.retail.productservice.service.ProductService;
import com.retail.productservice.validator.ProductValidator;
import com.retail.productservice.vo.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping("/api")
public class ProductController {

    public static final String URL = "/v1/products/{id}";

    @Autowired
    ProductValidator productValidator;

    @Autowired
    ProductService productService;

    /**
     * Method to process the input GET request and return Product Details in the Response JSON
     * @param productId
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductResponse getProducts(@PathVariable("id") String productId){
        ProductResponse productResponse = null;
        if(productValidator.isValidProductId(productId)){
            productResponse = productService.getProductInfo(productId);
        }
        return productResponse;
    }

}
