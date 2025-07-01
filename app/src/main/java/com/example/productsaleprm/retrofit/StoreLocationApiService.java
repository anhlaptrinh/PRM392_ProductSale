package com.example.productsaleprm.retrofit;

import com.example.productsaleprm.model.response.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface StoreLocationApiService {
    @GET("api/store-locations")
    Call<BaseResponse> getAllStoreLocations();
}
