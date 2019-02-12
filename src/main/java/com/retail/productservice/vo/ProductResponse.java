package com.retail.productservice.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Value object to hold the response details for the ProductID
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "retrieveprice")
public class ProductResponse {

    @Id
    @JsonProperty("id")
    private long productId;

    @Transient
    @JsonProperty("name")
    private String productName;

    @JsonProperty("price")
    private Price price;

    public ProductResponse(){

    }

    public ProductResponse(long productId, String productName, Price price){
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }

    public ProductResponse(long productId, Price price) {
        this.productId = productId;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
