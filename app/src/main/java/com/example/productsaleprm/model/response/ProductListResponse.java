package com.example.productsaleprm.model.response;

import com.example.productsaleprm.model.Product;

import java.util.List;

public class ProductListResponse {

    private String status;
    private List<Product> data;

    public String getStatus() {
        return status;
    }

    public List<Product> getData() {
        return data;
    }
}
