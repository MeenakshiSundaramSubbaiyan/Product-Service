package com.retail.productservice.security.vo;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Setter
@Getter
@AllArgsConstructor
public class Response<T> {

    public static final String STATUS_FAILURE = "failure";

    private String status;
    private String message;
    private T data;

    private static final Logger logger = LoggerFactory.getLogger(Response.class);

    public String toJson() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage());
            throw e;
        }
    }

    public void send(HttpServletResponse response, int code) throws IOException {
        response.setStatus(code);
        response.setContentType("application/json");
        String errorMessage;

        errorMessage = toJson();

        response.getWriter().println(errorMessage);
        response.getWriter().flush();
    }
}
