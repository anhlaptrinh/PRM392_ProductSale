package com.example.productsaleprm.model.response;

import com.example.productsaleprm.model.Product;


public class ProductResponse {

    private String status;
    private Product data;

    public String getStatus() {
        return status;
    }
    public Product getData() {
        return data;
    }
}
