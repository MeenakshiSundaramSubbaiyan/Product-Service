package com.retail.productservice.validator;



import com.retail.productservice.constants.ProductConstants;
import com.retail.productservice.exception.InvalidInputException;
import com.retail.productservice.vo.ProductResponse;
import com.retail.productservice.vo.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.util.Currency;


/**
 * Validator class to validate the input parameters
 */
@Component
public class ProductValidator implements Validator {

    Logger logger = LoggerFactory.getLogger(ProductValidator.class);

    public boolean isValidProductId(String productId){
        boolean valid = true;
        try{
            Long.parseLong(productId);
        }catch(NumberFormatException | NullPointerException e){
            valid = false;
            logger.warn("Invalid Product Id");
            throw new InvalidInputException(new ResponseInfo(ProductConstants.INVALID_PRODUCT_ID_ERROR_CODE,
                    ProductConstants.INVALID_PRODUCT_ID_ERROR_DESCRIPTION,ProductConstants.RESPONSE_TYPE_ERROR));
        }
        return valid;
    }

    /**
     * Validates whether the productID given in the URI matches with the one
     * provided in the request body
     *
     * @param productJSON
     * @param productId
     * @return
     */
    public boolean isValidProductMatch(ProductResponse productJSON, String productId) {
        boolean valid = true;
        if (!isValidProductId(productId) || Long.parseLong(productId) != productJSON.getProductId()) {
            valid = false;
            throw new InvalidInputException(new ResponseInfo(ProductConstants.MISMATCH_PRODUCT_ID_ERROR_CODE,
                    ProductConstants.MISMATCH_PRODUCT_ID_ERROR_DESCRIPTION, ProductConstants.RESPONSE_TYPE_ERROR));
        }
        return valid;
    }

    /**
     * validates whether the given price is valid or not
     *
     * @param price
     * @return
     */
    public boolean isValidProductPrice(BigDecimal price) {
        boolean valid = true;
        if (price == null || price.doubleValue() <= 0.0) {
            valid = false;
        }
        return valid;
    }

    /**
     * Validates the currency Code against ISO 4217 currency code list
     *
     * @param currencyCode
     * @return
     */
    public boolean isValidCurrencyCode(String currencyCode) {
        boolean valid = true;
        try {
            Currency.getInstance(currencyCode);
        } catch (IllegalArgumentException illegalArgumentException) {
            valid = false;
        }
        return valid;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductResponse.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductResponse productPrice = (ProductResponse) target;
        if (productPrice.getPrice() == null) {
            logger.warn(ProductConstants.MISSING_PRODUCT_PRICE_ERROR_CODE);
            throw new InvalidInputException(new ResponseInfo(ProductConstants.MISSING_PRODUCT_PRICE_ERROR_CODE,
                    ProductConstants.MISSING_PRODUCT_PRICE_ERROR_DESCRIPTION, ProductConstants.RESPONSE_TYPE_ERROR));
        } else if (!this.isValidProductPrice(productPrice.getPrice().getValue())) {
            logger.warn(ProductConstants.INVALID_PRODUCT_PRICE_ERROR_DESCRIPTION);
            throw new InvalidInputException(new ResponseInfo(ProductConstants.INVALID_PRODUCT_PRICE_ERROR_CODE,
                    ProductConstants.INVALID_PRODUCT_PRICE_ERROR_DESCRIPTION, ProductConstants.RESPONSE_TYPE_ERROR));
        } else if (productPrice.getPrice().getCurrencyCode() == null
                || productPrice.getPrice().getCurrencyCode().trim().equals("")) {
            logger.warn(ProductConstants.MISSING_CURRENCY_ERROR_CODE);
            throw new InvalidInputException(new ResponseInfo(ProductConstants.MISSING_CURRENCY_ERROR_CODE,
                    ProductConstants.MISSING_CURRENCY_ERROR_DESCRIPTION, ProductConstants.RESPONSE_TYPE_ERROR));
        } else if (!isValidCurrencyCode(productPrice.getPrice().getCurrencyCode().trim())) {
            logger.warn(ProductConstants.INVALID_CURRENCY_ERROR_CODE);
            throw new InvalidInputException(new ResponseInfo(ProductConstants.INVALID_CURRENCY_ERROR_CODE,
                    ProductConstants.INVALID_CURRENCY_ERROR_DESCRIPTION, ProductConstants.RESPONSE_TYPE_ERROR));
        }
    }
}
