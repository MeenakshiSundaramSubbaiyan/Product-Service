package com.retail.productservice.security.handler;

import com.retail.productservice.security.vo.Response;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        AuthenticationException e)
            throws IOException, ServletException {

        String errorMessage = ExceptionUtils.getMessage(e);

        sendError(httpServletResponse, HttpServletResponse.SC_UNAUTHORIZED, errorMessage, e);
    }


    private void sendError(HttpServletResponse response, int code, String message, Exception e) throws IOException {
        SecurityContextHolder.clearContext();

        Response<String> exceptionResponse =
                new Response<>(Response.STATUS_FAILURE, message, ExceptionUtils.getStackTrace(e));

        exceptionResponse.send(response, code);
    }
}
