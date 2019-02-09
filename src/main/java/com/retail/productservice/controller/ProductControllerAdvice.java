package com.retail.productservice.controller;

import com.retail.productservice.exception.InvalidInputException;
import com.retail.productservice.vo.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    public ErrorResponse invalidInput(InvalidInputException e){ return e.getError();}

}
