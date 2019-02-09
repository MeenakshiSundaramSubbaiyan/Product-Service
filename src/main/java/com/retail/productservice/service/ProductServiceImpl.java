package com.retail.productservice.service;

import com.retail.productservice.vo.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductClient productClient;

    @Override
    public ProductResponse getProductInfo(String productId) {
        ProductResponse productResponse = new ProductResponse();
        long id = Long.parseLong(productId);
        String productName = productClient.getProductNameById(id);
        productResponse.setProductId(id);
        productResponse.setProductName(productName);

        return productResponse;
    }
}
