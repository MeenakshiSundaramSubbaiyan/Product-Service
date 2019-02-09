package com.retail.productservice.exception;

import com.retail.productservice.vo.ResponseInfo;


/**
 * Exception to hold more information about the error after the input validation
 */
public class InvalidInputException extends RuntimeException{

    private ResponseInfo error;

    public InvalidInputException(ResponseInfo error){
        this.error = error;
    }

    public ResponseInfo getError() {
        return error;
    }

    public void setError(ResponseInfo error) {
        this.error = error;
    }
}
