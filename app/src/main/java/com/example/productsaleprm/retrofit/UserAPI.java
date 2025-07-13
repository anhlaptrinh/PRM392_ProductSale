package com.example.productsaleprm.retrofit;

import com.example.productsaleprm.model.User;
import com.example.productsaleprm.model.response.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface UserAPI {

    @GET("api/users/user-info")
    Call<BaseResponse<User>> getCurrentUser();

    @PUT("api/users")
    Call<BaseResponse<User>> updateUser(@Body User user);

}
