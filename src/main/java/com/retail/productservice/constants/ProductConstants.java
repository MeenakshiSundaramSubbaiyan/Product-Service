package com.retail.productservice.constants;

/**
 *Constant class to define the response message
 */
public class ProductConstants {

    public static final String RESPONSE_TYPE_SUCCESS = "Success";
    public static final String RESPONSE_TYPE_ERROR = "Error";
    public static final String INVALID_PRODUCT_ID_ERROR_CODE = "Invalid Product Id";
    public static final String INVALID_PRODUCT_ID_ERROR_DESCRIPTION = "The given Product Id in Request URI is not valid";
    public static final String MISMATCH_PRODUCT_ID_ERROR_CODE = "Product Id Mismatch";
    public static final String MISMATCH_PRODUCT_ID_ERROR_DESCRIPTION = "The given Product Id in Request URI and Request Body does not match";
    public static final String MISSING_PRODUCT_PRICE_ERROR_CODE = "Missing Product Price Info";
    public static final String MISSING_PRODUCT_PRICE_ERROR_DESCRIPTION = "Product Price Informaiton is Missing";
    public static final String INVALID_PRODUCT_PRICE_ERROR_CODE = "Invalid Product Price Value";
    public static final String INVALID_PRODUCT_PRICE_ERROR_DESCRIPTION = "The given Product Price value is not valid";
    public static final String INVALID_CURRENCY_ERROR_CODE = "Invalid Currency Code";
    public static final String INVALID_CURRENCY_ERROR_DESCRIPTION = "The given Currency Code is not valid";
    public static final String MISSING_CURRENCY_ERROR_CODE = "Missing Currency Code";
    public static final String MISSING_CURRENCY_ERROR_DESCRIPTION = "The Currency Code is Missing";
    public static final String PRODUCT_UPDATE_SUCCESS = "Product Price Updated";
    public static final String PRODUCT_NOT_FOUND = "Product [%s] not found";
    public static final String PRODUCT_END_POINT_UNREACHABLE = "RedSky Service Endpoint is Unreachable, Please contact Administrator";
    public static final String MISSING_AUTHENTICATION_TOKEN = "BadCredentialsException: Missing Authentication Token";
}
