package com.retail.productservice.service;

import com.retail.productservice.repository.ProductRepository;
import com.retail.productservice.vo.Price;
import com.retail.productservice.vo.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductClient productClient;

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    ProductRepository productRepository;

    @Override
    public ProductResponse getProductInfo(String productId) {
        ProductResponse productResponse = null;
        long id = Long.parseLong(productId);
        String productName = productClient.getProductNameById(id);
        productResponse = this.retrieveProductPrice(id);
        if(productResponse == null) {
            productResponse = new ProductResponse();
            productResponse.setPrice(new Price());
        }
        productResponse.setProductId(id);
        productResponse.setProductName(productName);
        return productResponse;
    }

    @Override
    public ProductResponse retrieveProductPrice(long productId) {
        return productRepository.queryPriceByProductId(productId);
    }

    @Override
    public void updateProductPrice(ProductResponse productJSON) {
        productClient.getProductNameById(productJSON.getProductId());
        productRepository.save(new ProductResponse(productJSON.getProductId(), productJSON.getPrice()));
    }


}
