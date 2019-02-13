package com.retail.productservice;

import com.retail.productservice.constants.ProductConstants;
import com.retail.productservice.security.config.TokenAuthenticationFilter;
import com.retail.productservice.security.vo.Response;
import com.retail.productservice.vo.Price;
import com.retail.productservice.vo.ProductResponse;
import com.retail.productservice.vo.ResponseInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Integration test cases for the Product Service
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductServiceApplicationTests {

    @Test
    public void contextLoads() {
    }

    private String hostName = "localhost";

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String authToken;

    /**
     * Setup the login and retrieve the Auth token to be used submitting the
     * requests to product End point
     */
    @Before
    public void setUp(){

        URI loginUri = UriComponentsBuilder.newInstance().scheme("http").host(hostName).port(port)
                .path("auth/login").build().toUri();
        HttpHeaders headers =new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("username", "test");
        map.add("password", "test");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = this.restTemplate.postForEntity(loginUri, request, String.class);
        authToken = response.getBody();

    }

    /**
     * Validating the getProduct Request for the invalid product ID
     *
     * @throws Exception
     */
    @Test
    public void getProductInfoTest_ForValidProductID() throws Exception {
        String productId = "13860416";
        ResponseEntity<ProductResponse> result = this.restTemplate.exchange(getURI(productId), HttpMethod.GET,
                getRequestEntity(null), ProductResponse.class);
        assertThat(result.getBody().getProductId()).isEqualTo(Long.valueOf(productId));
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
    }

    /**
     * Validating the getProduct Request for the invalid product ID
     *
     * @throws Exception
     */
    @Test
    public void getProductInfoTest_ForInValidProductID() throws Exception {
        String productId = "test";
        ResponseEntity<ResponseInfo> result = this.restTemplate.exchange(getURI(productId), HttpMethod.GET,
                getRequestEntity(null), ResponseInfo.class);
        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
        assertThat(result.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEquals(result.getBody().getCode(), ProductConstants.INVALID_PRODUCT_ID_ERROR_CODE);
        assertEquals(result.getBody().getMessage(), ProductConstants.INVALID_PRODUCT_ID_ERROR_DESCRIPTION);
        assertEquals(result.getBody().getResponseType(), ProductConstants.RESPONSE_TYPE_ERROR);
    }

    /**
     * Validating the getProduct Request for the product does not exist
     *
     * @throws Exception
     */
    @Test
    public void getProductInfoTest_ForProduct404() throws Exception {
        String productId = "12324";
        ResponseEntity<ResponseInfo> result = this.restTemplate.exchange(getURI(productId), HttpMethod.GET,
                getRequestEntity(null), ResponseInfo.class);
        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
        assertThat(result.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEquals(result.getBody().getCode(), HttpStatus.NOT_FOUND.getReasonPhrase());
        assertEquals(result.getBody().getMessage(), String.format(ProductConstants.PRODUCT_NOT_FOUND, productId));
        assertEquals(result.getBody().getResponseType(), ProductConstants.RESPONSE_TYPE_ERROR);
    }

    /**
     * Validating the product price update with valid product id in the request
     *
     * @throws Exception
     */
    @Test
    public void updateProductInfoTest_ForValidProductID() throws Exception {
        ProductResponse productPrice = new ProductResponse(13860416,
                new Price(BigDecimal.valueOf(2000), Currency.getInstance(Locale.getDefault()).getCurrencyCode()));
        String productId = "13860416";
        ResponseEntity<ResponseInfo> result = this.restTemplate.exchange(getURI(productId), HttpMethod.PUT,
                getRequestEntity(productPrice), ResponseInfo.class);
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEquals(result.getBody().getCode(), HttpStatus.OK.getReasonPhrase());
        assertEquals(result.getBody().getMessage(), ProductConstants.PRODUCT_UPDATE_SUCCESS);
        assertEquals(result.getBody().getResponseType(), ProductConstants.RESPONSE_TYPE_SUCCESS);
    }

    /**
     * Validating the product price update with invalid product id in the request
     *
     * @throws Exception
     */
    @Test
    public void updateProductInfoTest_ForInValidProductID() throws Exception {
        ProductResponse productPrice = new ProductResponse(13860416,
                new Price(BigDecimal.valueOf(2000), Currency.getInstance(Locale.getDefault()).getCurrencyCode()));
        String productId = "123456";
        ResponseEntity<ResponseInfo> result = this.restTemplate.exchange(getURI(productId), HttpMethod.PUT,
                getRequestEntity(productPrice), ResponseInfo.class);
        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
        assertThat(result.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
    }

    /**
     * Validating the product price update with valid currency code in the request
     *
     * @throws Exception
     */
    @Test
    public void updateProductInfoTest_WithInValidCurrencyCode() throws Exception {
        ProductResponse productPrice = new ProductResponse(13860416, new Price(BigDecimal.valueOf(2000), "ABCD"));
        String productId = "13860416";
        ResponseEntity<ResponseInfo> result = this.restTemplate.exchange(getURI(productId), HttpMethod.PUT,
                getRequestEntity(productPrice), ResponseInfo.class);
        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
        assertThat(result.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEquals(result.getBody().getCode(), ProductConstants.INVALID_CURRENCY_ERROR_CODE);
        assertEquals(result.getBody().getMessage(), ProductConstants.INVALID_CURRENCY_ERROR_DESCRIPTION);
        assertEquals(result.getBody().getResponseType(), ProductConstants.RESPONSE_TYPE_ERROR);
    }

    /**
     * Validating the product price update with no currency code information in the
     * request
     *
     * @throws Exception
     */
    @Test
    public void updateProductInfoTest_WithBlankCurrencyCode() throws Exception {
        ProductResponse productPrice = new ProductResponse(13860416, new Price(BigDecimal.valueOf(2000), null));
        String productId = "13860416";
        ResponseEntity<ResponseInfo> result = this.restTemplate.exchange(getURI(productId), HttpMethod.PUT,
                getRequestEntity(productPrice), ResponseInfo.class);
        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
        assertThat(result.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEquals(result.getBody().getCode(), ProductConstants.MISSING_CURRENCY_ERROR_CODE);
        assertEquals(result.getBody().getMessage(), ProductConstants.MISSING_CURRENCY_ERROR_DESCRIPTION);
        assertEquals(result.getBody().getResponseType(), ProductConstants.RESPONSE_TYPE_ERROR);
    }

    /**
     * Validating the product price update with invalid price information in the
     * request
     *
     * @throws Exception
     */
    @Test
    public void updateProductInfoTest_WithInValidPrice() throws Exception {
        ProductResponse productPrice = new ProductResponse(13860416,
                new Price(BigDecimal.valueOf(-1), Currency.getInstance(Locale.getDefault()).getCurrencyCode()));
        String productId = "13860416";
        ResponseEntity<ResponseInfo> result = this.restTemplate.exchange(getURI(productId), HttpMethod.PUT,
                getRequestEntity(productPrice), ResponseInfo.class);
        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
        assertThat(result.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEquals(result.getBody().getCode(), ProductConstants.INVALID_PRODUCT_PRICE_ERROR_CODE);
        assertEquals(result.getBody().getMessage(), ProductConstants.INVALID_PRODUCT_PRICE_ERROR_DESCRIPTION);
        assertEquals(result.getBody().getResponseType(), ProductConstants.RESPONSE_TYPE_ERROR);
    }

    /**
     * Validating the product price update with no price information in the request
     *
     * @throws Exception
     */
    @Test
    public void updateProductInfoTest_WithNoPrice() throws Exception {
        ProductResponse productPrice = new ProductResponse(13860416, null);
        String productId = "13860416";
        ResponseEntity<ResponseInfo> result = this.restTemplate.exchange(getURI(productId), HttpMethod.PUT,
                getRequestEntity(productPrice), ResponseInfo.class);
        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
        assertThat(result.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEquals(result.getBody().getCode(), ProductConstants.MISSING_PRODUCT_PRICE_ERROR_CODE);
        assertEquals(result.getBody().getMessage(), ProductConstants.MISSING_PRODUCT_PRICE_ERROR_DESCRIPTION);
        assertEquals(result.getBody().getResponseType(), ProductConstants.RESPONSE_TYPE_ERROR);
    }

    /**
     * Validating the product Id mismatch between the URI and
     * Request Body
     *
     * @throws Exception
     */
    @Test
    public void updateProductInfoTest_URIandBodyProductId_Mismatch() throws Exception {
        ProductResponse productPrice = new ProductResponse(13860428,
                new Price(BigDecimal.valueOf(2000), Currency.getInstance(Locale.getDefault()).getCurrencyCode()));
        String productId = "13860416";
        ResponseEntity<ResponseInfo> result = this.restTemplate.exchange(getURI(productId), HttpMethod.PUT,
                getRequestEntity(productPrice), ResponseInfo.class);
        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
        assertThat(result.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
        assertEquals(result.getBody().getCode(), ProductConstants.MISMATCH_PRODUCT_ID_ERROR_CODE);
        assertEquals(result.getBody().getMessage(), ProductConstants.MISMATCH_PRODUCT_ID_ERROR_DESCRIPTION);
        assertEquals(result.getBody().getResponseType(), ProductConstants.RESPONSE_TYPE_ERROR);
    }

    public HttpEntity<ProductResponse> getRequestEntity(ProductResponse productPrice) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, TokenAuthenticationFilter.BEARER + " " + authToken);
        HttpEntity<ProductResponse> requestEntity = new HttpEntity<ProductResponse>(productPrice, headers);
        return requestEntity;
    }

    public URI getURI(String productId) {
        String hostName = "localhost";
        return UriComponentsBuilder.newInstance().scheme("http").host(hostName).port(port)
                .path("/api/v1/products/{productId}").build(productId);
    }

    /**
     * Validating the authentication with getProduct Request by not providing the
     * auth token
     *
     * @throws Exception
     */
    @Test
    public void authenticationTest_withNoToken() throws Exception {
        String productId = "13860416";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Response> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Response> result = this.restTemplate.exchange(getURI(productId), HttpMethod.GET,
                requestEntity, Response.class);
        assertEquals(result.getBody().getMessage(), ProductConstants.MISSING_AUTHENTICATION_TOKEN);
    }



}

