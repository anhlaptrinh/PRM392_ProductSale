package com.example.productsaleprm.model.response;

public class LoginResponse {
    private int code;
    private String message;
    private String data; // token

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }
}


