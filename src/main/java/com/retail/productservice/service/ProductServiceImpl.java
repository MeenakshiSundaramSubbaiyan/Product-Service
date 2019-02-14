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

    @Autowired
    ProductRepository productRepository;

    /**
     * Method to fetch the product name from Redsky service and price from DB for the given ProductID
     * @param productId
     * @return
     */
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

    /**
     * Method to fetch the price from mongo DB
     * @param productId
     * @return
     */
    @Override
    public ProductResponse retrieveProductPrice(long productId) {
        return productRepository.queryPriceByProductId(productId);
    }

    /**
     * Method to update the price to mongo DB for the given ProductID
     * @param productJSON
     */
    @Override
    public void updateProductPrice(ProductResponse productJSON) {
        productClient.getProductNameById(productJSON.getProductId());
        productRepository.save(new ProductResponse(productJSON.getProductId(), productJSON.getPrice()));
    }


}
