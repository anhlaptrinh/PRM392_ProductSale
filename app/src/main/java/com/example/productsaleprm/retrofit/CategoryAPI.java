package com.example.productsaleprm.retrofit;

import com.example.productsaleprm.model.response.CategoryListResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryAPI {
    @GET("api/categories")
    Call<CategoryListResponse> getAllCategories();
}
