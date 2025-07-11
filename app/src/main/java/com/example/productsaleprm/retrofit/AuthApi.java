package com.example.productsaleprm.retrofit;

import com.example.productsaleprm.model.response.GenericResponse;
import com.example.productsaleprm.model.response.LoginResponse;
import com.example.productsaleprm.model.response.RegisterResponse;
import com.example.productsaleprm.model.resquest.RegisterRequest;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {
    @POST("/api/authentication/login")
    Call<LoginResponse> login(@Query("email") String email, @Query("password") String password);

    @POST("api/authentication/verification-code")
    Call<Void> sendVerificationCode(@Query("email") String email);

    @POST("api/authentication/register")
    Call<RegisterResponse> register(
            @Query("verificationCode") String verificationCode,
            @Body RegisterRequest registerRequest
    );

    @POST("api/authentication/forgot-password")
    Call<GenericResponse> forgotPassword(@Query("email") String email);


}

