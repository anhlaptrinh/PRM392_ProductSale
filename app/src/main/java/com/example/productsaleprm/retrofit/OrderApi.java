package com.example.productsaleprm.retrofit;

import com.example.productsaleprm.model.CartItem;
import com.example.productsaleprm.model.Order;
import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.model.response.CartItemResponse;
import com.example.productsaleprm.model.response.OrderDetailResponse;
import com.example.productsaleprm.model.response.PaymentResponse;
import com.example.productsaleprm.model.resquest.CreateOrderRequest;
import com.example.productsaleprm.model.resquest.ReorderRequest;
import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderApi {
    @POST("/api/orders")
    Call<BaseResponse<JsonElement>> createOrder(@Body CreateOrderRequest request);

    @GET("/api/orders/user/{id}")
    Call<List<Order>> getOrders(@Path("id") int userId);

    @GET("/api/orders/{orderId}/items")
    Call<List<CartItemResponse>> getOrderItems(@Path("orderId") int orderId);

    @GET("/api/orders/{id}")
    Call<BaseResponse<OrderDetailResponse>> getOrderById(@Path("id") int orderId);

    @PUT("/api/orders/{id}")
    Call<BaseResponse<Void>> updateOrder(@Path("id") int id);

    @POST("/api/orders/reorder")
    Call<BaseResponse<Boolean>> reorder(@Body ReorderRequest request);
}
