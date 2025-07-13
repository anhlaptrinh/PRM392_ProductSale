package com.example.productsaleprm.retrofit;

import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.model.response.PaymentResponse;
import com.example.productsaleprm.model.resquest.CreateOrderRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderApi {
    @POST("/api/orders") // chỉnh endpoint phù hợp backend bạn
    Call<BaseResponse<PaymentResponse>> createOrder(@Body CreateOrderRequest request);
}
