package com.example.productsaleprm.retrofit;

import com.example.productsaleprm.model.StoreLocation;
import com.example.productsaleprm.model.response.BaseResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface StoreLocationAPI {
    @GET("api/store-locations")
    Call<BaseResponse<List<StoreLocation>>> getAllStoreLocations();
}
