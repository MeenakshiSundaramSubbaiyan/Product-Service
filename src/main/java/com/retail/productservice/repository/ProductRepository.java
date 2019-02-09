package com.retail.productservice.repository;

import com.retail.productservice.vo.ProductResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<ProductResponse, Long> {

    public ProductResponse queryPriceByProductId(long productId);
}
