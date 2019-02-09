package com.retail.productservice.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.retail.productservice.vo.Product;

import java.io.IOException;

/**
 * Custom Serializer class to get only the required attributes from the RedSky response JSON
 */
public class ProductDeserializer extends JsonDeserializer<Product> {
    @Override
    public Product deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = parser.getCodec().readTree(parser);
        Long id = Long.parseLong((node.get("product").get("available_to_promise_network").get("product_id")).textValue());
        String productName = node.get("product").get("item").get("product_description").get("title").textValue();
        return new Product(id, productName);
    }
}
