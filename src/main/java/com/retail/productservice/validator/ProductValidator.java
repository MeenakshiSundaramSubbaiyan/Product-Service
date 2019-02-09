package com.retail.productservice.validator;



import com.retail.productservice.constants.ProductConstants;
import com.retail.productservice.exception.InvalidInputException;
import com.retail.productservice.vo.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {

    Logger logger = LoggerFactory.getLogger(ProductValidator.class);

    public boolean isValidProductId(String productId){
        boolean valid = true;
        try{
            Long.parseLong(productId);
        }catch(NumberFormatException | NullPointerException e){
            valid = false;
            logger.warn("Invalid Product Id");
            throw new InvalidInputException(new ErrorResponse(ProductConstants.INVALID_PRODUCT_ID_ERROR_CODE,
                    ProductConstants.INVALID_PRODUCT_ID_ERROR_DESCRIPTION,ProductConstants.RESPONSE_TYPE_ERROR));
        }
        return valid;
    }
}
