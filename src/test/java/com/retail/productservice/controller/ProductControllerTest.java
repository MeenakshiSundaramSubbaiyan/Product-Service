package com.retail.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.productservice.constants.ProductConstants;
import com.retail.productservice.service.ProductService;
import com.retail.productservice.validator.ProductValidator;
import com.retail.productservice.vo.Price;
import com.retail.productservice.vo.ProductResponse;
import com.retail.productservice.vo.ResponseInfo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest {

    @Autowired
    private ProductController productController;

    ObjectMapper objMapper = new ObjectMapper();

    private ProductService productService;

    private MockMvc mockMvc;

    /**
     * Mock Setup the dependencies
     */
    @Before
    public void setup() {
        productService = mock(ProductService.class);
        productController = new ProductController();
        productController.setProductService(productService);
        productController.setProductValidator(new ProductValidator());
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    /**
     * Validating getProducts() method with valid Product ID
     *
     * @throws Exception
     */
    @Test
    public void getProductsTest_ValidProduct() throws Exception {
        ProductResponse productResponse = new ProductResponse(13860416,"Progressive power yoga:Sedona experie (DVD)",
                new Price(BigDecimal.valueOf(5.67), Currency.getInstance(Locale.getDefault()).getCurrencyCode()));
        given(productController.getProducts("13860416")).willReturn(productResponse);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/products/13860416/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        String resultString = mvcResult.getResponse().getContentAsString();
        objMapper = new ObjectMapper();
        ProductResponse result = objMapper.readValue(resultString, ProductResponse.class);
        assertEquals(result.getProductId(), 13860416);
        assertEquals(result.getProductName(), "Progressive power yoga:Sedona experie (DVD)");
        assertEquals(result.getPrice().getValue(), BigDecimal.valueOf(5.67));
        assertEquals(result.getPrice().getCurrencyCode(), Currency.getInstance(Locale.getDefault()).getCurrencyCode());
    }

    /**
     * Validating getProducts() method with invalid Product ID
     *
     * @throws Exception
     */

    @Test
    public void getProductsTest_InValidProduct() throws Exception {
        ProductResponse productResponse = new ProductResponse(13860416,"Progressive power yoga:Sedona experie (DVD)",
                new Price(BigDecimal.valueOf(5.67), Currency.getInstance(Locale.getDefault()).getCurrencyCode()));
        given(productController.getProducts("13860416")).willReturn(productResponse);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/products/13860416/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        String resultString = mvcResult.getResponse().getContentAsString();
        objMapper = new ObjectMapper();
        ProductResponse result = objMapper.readValue(resultString, ProductResponse.class);
        assertNotEquals(result.getProductId(), 13860418);
    }

    /**
     * Validating the product price update with valid product id in the request
     *
     * @throws Exception
     */
    @Test
    public void updateProductResponse_ValidProductId() throws Exception {
        ProductResponse productResponse = new ProductResponse(13860416,
                new Price(BigDecimal.valueOf(5.67), Currency.getInstance(Locale.getDefault()).getCurrencyCode()));
        String priceJson = objMapper.writeValueAsString(productResponse);

        mockMvc.perform(put("/api/v1/products/13860416/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(priceJson))
                .andExpect(status().is2xxSuccessful());

    }

    /**
     * Validating the product price update with invalid product id in the request
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void updateProductInfoTest_ForInValidProductID() throws Exception {
        ProductResponse productPrice = new ProductResponse(13860416,
                new Price(BigDecimal.valueOf(5.67), Currency.getInstance(Locale.getDefault()).getCurrencyCode()));
        String priceJson = objMapper.writeValueAsString(productPrice);
        MockHttpServletResponse response = mockMvc
                .perform(put("/api/v1/products/123456/").contentType(MediaType.APPLICATION_JSON).content(priceJson))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();
        ResponseInfo result = objMapper.readValue(response.getContentAsString(), ResponseInfo.class);
        assertEquals(result.getCode(), ProductConstants.MISSING_PRODUCT_PRICE_ERROR_CODE);
        assertEquals(result.getMessage(), ProductConstants.MISSING_PRODUCT_PRICE_ERROR_DESCRIPTION);
        assertEquals(result.getResponseType(), ProductConstants.RESPONSE_TYPE_ERROR);
    }

    /**
     * Validating the product price update with invalid currency code in the request
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void updateProductInfoTest_WithInValidCurrencyCode() throws Exception {
        ProductResponse productPrice = new ProductResponse(13860416, new Price(BigDecimal.valueOf(2000), "ABCD"));
        String priceJson = objMapper.writeValueAsString(productPrice);
        mockMvc.perform(put("/api/v1/products/13860416/").contentType(MediaType.APPLICATION_JSON).content(priceJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn().getResponse();
    }

    /**
     * Validating the product price update with no currency code information in the request
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void updateProductInfoTest_WithBlankCurrencyCode() throws Exception {
        ProductResponse productPrice = new ProductResponse(13860416, new Price(BigDecimal.valueOf(2000), null));
        String priceJson = objMapper.writeValueAsString(productPrice);
        mockMvc.perform(put("/api/v1/products/13860416/").contentType(MediaType.APPLICATION_JSON).content(priceJson))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn().getResponse();
    }

    /**
     * Validating the product price update with invalid price information in the request
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void updateProductInfoTest_WithInValidPrice() throws Exception {
        ProductResponse productPrice = new ProductResponse(13860416,
                new Price(BigDecimal.valueOf(-1), Currency.getInstance(Locale.getDefault()).getCurrencyCode()));
        MockHttpServletResponse response = mockMvc
                .perform(put("/api/v1/products/13860416/").contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(productPrice)))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();
        ResponseInfo result = objMapper.readValue(response.getContentAsString(), ResponseInfo.class);
        assertEquals(result.getCode(), ProductConstants.INVALID_PRODUCT_PRICE_ERROR_CODE);
        assertEquals(result.getMessage(), ProductConstants.INVALID_PRODUCT_PRICE_ERROR_DESCRIPTION);
        assertEquals(result.getResponseType(), ProductConstants.RESPONSE_TYPE_ERROR);
    }

    /**
     * Validating the product price update with no price information in the request
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void updateProductInfoTest_WithNoPrice() throws Exception {
        ProductResponse productPrice = new ProductResponse(13860416, null);
        MockHttpServletResponse response = mockMvc
                .perform(put("/api/v1/products/13860416/").contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(productPrice)))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();
        ResponseInfo result = objMapper.readValue(response.getContentAsString(), ResponseInfo.class);
        assertEquals(result.getCode(), ProductConstants.MISSING_PRODUCT_PRICE_ERROR_CODE);
        assertEquals(result.getMessage(), ProductConstants.MISSING_PRODUCT_PRICE_ERROR_DESCRIPTION);
        assertEquals(result.getResponseType(), ProductConstants.RESPONSE_TYPE_ERROR);
    }

    /**
     * Validating the product Id mismatch between the URI and Request Body
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void updateProductInfoTest_URIandBodyProductId_Mismatch() throws Exception {
        ProductResponse productPrice = new ProductResponse(13860416, new Price(BigDecimal.valueOf(2000), Currency.getInstance(Locale.getDefault()).getCurrencyCode()));
        String priceJson = objMapper.writeValueAsString(productPrice);
        MockHttpServletResponse response = mockMvc
                .perform(put("/api/v1/products/13860422/").contentType(MediaType.APPLICATION_JSON).content(priceJson))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();
        ResponseInfo result = objMapper.readValue(response.getContentAsString(), ResponseInfo.class);
        assertEquals(result.getCode(), ProductConstants.MISMATCH_PRODUCT_ID_ERROR_CODE);
        assertEquals(result.getMessage(), ProductConstants.MISMATCH_PRODUCT_ID_ERROR_DESCRIPTION);
        assertEquals(result.getResponseType(), ProductConstants.RESPONSE_TYPE_ERROR);
    }

}