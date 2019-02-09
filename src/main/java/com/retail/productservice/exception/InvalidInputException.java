package com.retail.productservice.exception;

import com.retail.productservice.vo.ErrorResponse;


/**
 * Exception to hold more information about the error after the input validation
 */
public class InvalidInputException extends RuntimeException{

    private ErrorResponse error;

    public InvalidInputException(ErrorResponse error){
        this.error = error;
    }

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }
}
