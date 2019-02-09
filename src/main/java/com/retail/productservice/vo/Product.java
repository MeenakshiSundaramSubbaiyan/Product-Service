package com.retail.productservice.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.retail.productservice.util.ProductDeserializer;

@JsonDeserialize(using = ProductDeserializer.class)
@JsonIgnoreProperties( ignoreUnknown = true)
public class Product {

    @JsonProperty("id")
    private long id;

    private String name;

    public Product(){
    }

    public Product(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
