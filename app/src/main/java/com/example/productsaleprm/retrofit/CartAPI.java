package com.example.productsaleprm.retrofit;


import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.model.response.CartResponseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CartAPI {
    @GET("api/cart")
    Call<BaseResponse<List<CartResponseData>>> getCartItems();

}
