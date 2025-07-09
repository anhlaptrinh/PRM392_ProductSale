package com.example.productsaleprm.retrofit;


import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.model.response.CartResponseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CartAPI {
    @GET("api/mobile/cart")
    Call<BaseResponse<CartResponseData>> getCartItems();

    @DELETE("api/mobile/cart/{id}")
    Call<Void> deleteCartItem(@Path("id") int cartItemId);

}
