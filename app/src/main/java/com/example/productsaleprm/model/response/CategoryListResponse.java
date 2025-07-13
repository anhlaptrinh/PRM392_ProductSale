package com.example.productsaleprm.model.response;

import com.example.productsaleprm.model.Category;
import java.util.List;

public class CategoryListResponse {
    private int code;
    private String message;
    private List<Category> data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<Category> getData() {
        return data;
    }
}
