package com.retail.productservice.service;

import com.retail.productservice.repository.ProductRepository;
import com.retail.productservice.vo.Price;
import com.retail.productservice.vo.ProductResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @InjectMocks
    ProductServiceImpl productService;

    @Mock
    ProductClient mockProductClient;

    @Mock
    ProductRepository mockProductRepository;

    @Mock
    RestTemplate mockRestTemplate;


    @Test
    public void retrieveProductPriceTest_ValidProductId(){
        ProductResponse productResponse = new ProductResponse(13860416,
                new Price(BigDecimal.valueOf(5.67), Currency.getInstance(Locale.getDefault()).getCurrencyCode()));
        Mockito.when(mockProductRepository.queryPriceByProductId(ArgumentMatchers.anyLong())).thenReturn(productResponse);
        ProductResponse actualResponse = productService.retrieveProductPrice(13860416);
        assertEquals(actualResponse.getProductId(),productResponse.getProductId());
        assertEquals(actualResponse.getPrice().getValue(),productResponse.getPrice().getValue());
    }

    @Test
    public void retrieveProductPriceTest_InValidProductId(){
        ProductResponse productResponse = new ProductResponse(13860416,
                new Price(BigDecimal.valueOf(5.67), Currency.getInstance(Locale.getDefault()).getCurrencyCode()));
        Mockito.when(mockProductRepository.queryPriceByProductId(ArgumentMatchers.anyLong())).thenReturn(productResponse);
        ProductResponse actualResponse = productService.retrieveProductPrice(12345);
        assertNotEquals(actualResponse.getProductId(),12345);
    }

    @Test
    public void getProductInfoTest(){
        ProductResponse productResponse = new ProductResponse(13860416,
                new Price(BigDecimal.valueOf(5.67), Currency.getInstance(Locale.getDefault()).getCurrencyCode()));
        Mockito.when(mockProductClient.getProductNameById(Long.valueOf("13860416"))).thenReturn("Test");
        ProductResponse actualResponse = productService.getProductInfo("13860416");
        assertEquals(productResponse.getProductId(),actualResponse.getProductId());
    }



}