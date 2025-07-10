package com.example.productsaleprm.retrofit;


import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.model.response.CartResponseData;
import com.example.productsaleprm.model.response.CartUpdateResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CartAPI {
    @GET("api/mobile/cart")
    Call<BaseResponse<CartResponseData>> getCartItems();

    @DELETE("api/mobile/cart/{id}")
    Call<Void> deleteCartItem(@Path("id") int cartItemId);

    @PUT("api/mobile/cart/{id}/quantity")
    Call<BaseResponse<CartUpdateResponse>> updateQuantity(@Path("id") int id, @Query("quantity") int quantity);

}
