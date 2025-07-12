package com.example.productsaleprm.retrofit;

import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.model.response.WishListResponse;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WishListAPI {
    @GET("api/mobile/wishlist/find-by-UserId")
    Call<BaseResponse<WishListResponse>> getWishList(@Query("page") int page,
                                                     @Query("size")int size);
    @DELETE("api/mobile/wishlist/{id}")
    Call<Void> deleteItemOrCreateCart(@Path("id") int WishlistId,
                                      @Query("isCreateCart")Boolean isCreateCart);
}
