package com.retail.productservice.service;

import com.retail.productservice.exception.ProductNotFoundException;
import com.retail.productservice.vo.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(MockitoJUnitRunner.class)
public class ProductClientTest {

    @InjectMocks
    ProductClient mockProductClient;

    @Mock
    RestTemplate mockRestTemplate;

    @Test
    public void getProductNameById_Success(){
        Product product = new Product(13860416,"Progressive power yoga:Sedona experie (DVD)");
        ResponseEntity<Product> response = new ResponseEntity<>(product, HttpStatus.ACCEPTED);
        Mockito.when(mockRestTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.<HttpMethod>any(),
                ArgumentMatchers.<HttpEntity<?>>any(), ArgumentMatchers.<Class<?>>any(), ArgumentMatchers.<String, String>anyMap()))
                .thenReturn((ResponseEntity) response);
        String productDescription = mockProductClient.getProductNameById(Long.valueOf(13860416));
        assertEquals(productDescription, product.getName());
    }

    @Test(expected = Exception.class)
    public void getProductNameById_Failure(){
        Product product = new Product(13860416,"Progressive power yoga:Sedona experie (DVD)");
         Mockito.when(mockRestTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.<HttpMethod>any(),
                ArgumentMatchers.<HttpEntity<?>>any(), ArgumentMatchers.<Class<?>>any(), ArgumentMatchers.<String, String>anyMap()))
                .thenThrow(new ProductNotFoundException(12345));
        String productDescription = mockProductClient.getProductNameById(Long.valueOf(12345));
        assertNotEquals(productDescription, product.getName());
    }

}