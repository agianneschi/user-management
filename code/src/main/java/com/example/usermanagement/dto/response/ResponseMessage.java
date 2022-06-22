package com.example.usermanagement.dto.response;

import org.springframework.http.HttpStatus;

public class ResponseMessage {

    private HttpStatus httpStatus;

    private String message;

    public ResponseMessage(HttpStatus httpStatus, String message) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
