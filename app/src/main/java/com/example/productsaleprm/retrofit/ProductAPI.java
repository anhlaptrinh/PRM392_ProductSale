package com.example.productsaleprm.retrofit;

import com.example.productsaleprm.model.response.ProductListResponse;
import com.example.productsaleprm.model.response.ProductResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductAPI {
    // Lấy 1 product theo id
    @GET("api/products/{id}")
    Call<ProductResponse> getProductById(@Path("id") int productId);

    // Lấy tất cả products
    @GET("api/products")
    Call<ProductListResponse> getAllProducts();

}
