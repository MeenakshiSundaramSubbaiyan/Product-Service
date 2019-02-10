package com.retail.productservice.controller;

import com.retail.productservice.constants.ProductConstants;
import com.retail.productservice.service.ProductService;
import com.retail.productservice.validator.ProductValidator;
import com.retail.productservice.vo.ProductResponse;
import com.retail.productservice.vo.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public void setProductValidator(ProductValidator productValidator) {
        this.productValidator = productValidator;
    }


    @InitBinder("productResponse")
    public void setProductsBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(productValidator);
    }

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

    /**
     * Method to process the input GET request with multiple Product Id's and return Product Details in the Response JSON
     * @param productList
     */
/*    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductResponse> getMultipleProducts(@PathVariable("id") List<String> productList){
        List<ProductResponse> productResponseList = new ArrayList<>();
        if(!productList.isEmpty()){
            productList.forEach( productId -> {
                if(productValidator.isValidProductId(productId)){
                    ProductResponse productResponse = productService.getProductInfo(productId);
                    if(productResponse != null){
                        productResponseList.add(productResponse);
                    }
                }
            });
        }

        return productResponseList;
    }*/

    @PutMapping(value = URL, produces =  MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseInfo> updateProductPrice(@Valid @RequestBody ProductResponse productJSON,
                                                           @PathVariable("id") String productId){
        if(productValidator.isValidProductMatch(productJSON, productId)){
            productService.updateProductPrice(productJSON);
        }

        return ResponseEntity.ok(new ResponseInfo(HttpStatus.OK.getReasonPhrase(),
                ProductConstants.PRODUCT_UPDATE_SUCCESS, ProductConstants.RESPONSE_TYPE_SUCCESS));
    }

}
