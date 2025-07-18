package com.example.productsaleprm.retrofit;


import com.example.productsaleprm.model.Review;
import com.example.productsaleprm.model.ReviewReply;
import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.model.resquest.ReviewReplyRequest;
import com.example.productsaleprm.model.resquest.ReviewRequest;
import com.example.productsaleprm.model.resquest.VoteRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReviewAPI {

    @GET("/api/reviews/product/{productID}")
    Call<BaseResponse<List<Review>>> getReviewsByProduct(@Path("productID") int productId);

    @POST("/api/reviews")
    Call<BaseResponse<Review>> createReview(@Body ReviewRequest review);

    @DELETE("/api/reviews/{reviewID}")
    Call<BaseResponse<String>> deleteReview(@Path("reviewID") int reviewId);

    @POST("/api/reviews/review-vote")
    Call<BaseResponse<String>> vote(@Body VoteRequest voteRequest);

    @DELETE("/api/reviews/review-vote/{reviewID}")
    Call<BaseResponse<String>> undoVote(@Path("reviewID") int reviewId);

    @GET("/api/reviews/{reviewID}/replies")
    Call<BaseResponse<List<ReviewReply>>> getReplies(@Path("reviewID") int reviewId);

    @POST("/api/reviews/{reviewID}/replies")
    Call<BaseResponse<ReviewReply>> createReply(@Path("reviewID") int reviewId,
                                                @Body ReviewReplyRequest request);
}
