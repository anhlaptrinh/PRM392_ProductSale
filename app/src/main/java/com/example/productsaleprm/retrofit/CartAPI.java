package com.example.productsaleprm.retrofit;


import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.model.response.CartResponseData;
import com.example.productsaleprm.model.response.CartUpdateResponse;
import com.example.productsaleprm.model.resquest.AddToCartRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CartAPI {
    @POST("api/mobile/cart")
    Call<ResponseBody> addToCart(@Body AddToCartRequest request);
    @GET("api/mobile/cart")
    Call<BaseResponse<CartResponseData>> getCartItems(@Query("page") int page,
                                                      @Query("size") int size);

    @DELETE("api/mobile/cart/{id}")
    Call<Void> deleteCartItem(@Path("id") int cartItemId);

    @PUT("api/mobile/cart/{id}/quantity")
    Call<BaseResponse<CartUpdateResponse>> updateQuantity(@Path("id") int id, @Query("quantity") int quantity);

    @DELETE("api/mobile/cart/clear")
    Call<Void> clearCart();
}
