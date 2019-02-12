package com.retail.productservice.service;

import com.retail.productservice.vo.ProductResponse;

public interface ProductService {

    /**
     * Method to fetch the product name from Redsky service and price from DB for the given ProductID
     * @param productId
     * @return
     */
    ProductResponse getProductInfo(String productId);

    /**
     * Method to fetch the price from mongo DB
     * @param productId
     * @return
     */
    ProductResponse retrieveProductPrice(long productId);

    /**
     * Method to update the price to mongo DB for the given ProductID
     * @param productJSON
     */
    void updateProductPrice(ProductResponse productJSON);

}
