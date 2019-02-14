package com.retail.productservice.controller;

import com.retail.productservice.constants.ProductConstants;
import com.retail.productservice.exception.InvalidInputException;
import com.retail.productservice.exception.ProductNotFoundException;
import com.retail.productservice.vo.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

/**
 * Controller Advice class to handle exceptions thrown from the ProductController
 */
@RestControllerAdvice
public class ProductControllerAdvice {

    Logger logger = LoggerFactory.getLogger(ProductControllerAdvice.class);

    /**
     * Handles exception thrown because of the invalid input
     * @param e
     * @return
     */
    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseInfo invalidInput(InvalidInputException e){ return e.getError();}

    /**
     * Handles ProductNotFoundException thrown from controller if there are no product returned
     * from redsky product endpoint
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseInfo productNotFound(ProductNotFoundException e) {
        long productId = e.getProductId();
        return new ResponseInfo(HttpStatus.NOT_FOUND.getReasonPhrase(),
                String.format(ProductConstants.PRODUCT_NOT_FOUND, productId), ProductConstants.RESPONSE_TYPE_ERROR);
    }

    /**
     * Handles ResourceAccessException thrown from controller if there are any issues in accessing
     * the redsky product endpoint to fetch product information
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ResourceAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseInfo productServiceNotReachable(Exception e) {
        logger.error(ProductConstants.PRODUCT_END_POINT_UNREACHABLE);
        return new ResponseInfo(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ProductConstants.PRODUCT_END_POINT_UNREACHABLE, ProductConstants.RESPONSE_TYPE_ERROR);
    }

    /**
     * Advice to be applied in case of the Exception thrown from controller
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String serverError(Exception e) {
        e.printStackTrace();
        return e.getMessage();
    }

    /**
     * Exception handling for input data validation failure cases
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseInfo invalidJsonMessage(HttpMessageNotReadableException e) {
        return new ResponseInfo(HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage(),
                ProductConstants.RESPONSE_TYPE_ERROR);
    }

}
