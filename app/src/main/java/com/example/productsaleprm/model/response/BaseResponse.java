package com.example.productsaleprm.model.response;

import com.example.productsaleprm.model.StoreLocation;

import java.util.List;

public class BaseResponse {
    private int code;
    private String message;
    private List<StoreLocation> data;

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<StoreLocation> getData() { return data; }
    public void setData(List<StoreLocation> data) { this.data = data; }
}
