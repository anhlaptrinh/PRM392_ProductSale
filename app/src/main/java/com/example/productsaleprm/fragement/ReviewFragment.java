package com.example.productsaleprm.fragement;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.productsaleprm.R;
import com.example.productsaleprm.adapter.ReviewAdapter;
import com.example.productsaleprm.databinding.FragmentReviewBinding;
import com.example.productsaleprm.model.Review;
import com.example.productsaleprm.model.ReviewReply;
import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.model.resquest.ReviewReplyRequest;
import com.example.productsaleprm.model.resquest.ReviewRequest;
import com.example.productsaleprm.model.resquest.VoteRequest;
import com.example.productsaleprm.retrofit.RetrofitClient;
import com.example.productsaleprm.retrofit.ReviewAPI;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding;
    private ReviewAdapter adapter;
    private List<Review> allReviews = new ArrayList<>();
    private ReviewAPI api;
    private int productId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getInt("PRODUCT_ID", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(inflater, container, false);

        api = RetrofitClient.getClient(requireContext()).create(ReviewAPI.class);
        String currentUser = getCurrentUserEmail();
        String role = getCurrentUserRole();

        adapter = new ReviewAdapter(currentUser, role, new ReviewAdapter.OnReviewActionListener() {
            @Override
            public void onDeleteReview(Review review) {
                deleteReview(review);
            }

            @Override
            public void onVoteReview(Review review, String voteType) {
                voteReview(review, voteType);
            }

            @Override
            public void onLoadReplies(Review review) {
                loadReplies(review);
            }

            @Override
            public void onSubmitReply(Review review, String replyText) {
                submitReply(review, replyText);
            }

            @Override
            public void onDeleteReply(Review review, ReviewReply reply) {
                deleteReply(review, reply);
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);

        DividerItemDecoration divider = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        binding.recyclerView.addItemDecoration(divider);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.rating_filter_options,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.filterSpinner.setAdapter(spinnerAdapter);

        binding.filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterReviewsByRating(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.submitButton.setOnClickListener(v -> {
            submitReview();
        });

        fetchReviews();

        return binding.getRoot();
    }

    private void fetchReviews() {
        api.getReviewsByProduct(productId).enqueue(new Callback<BaseResponse<List<Review>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Review>>> call, Response<BaseResponse<List<Review>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allReviews = response.body().getData();
                    adapter.submitList(allReviews);
                    updateEmptyState(allReviews);

                    // Load replies
                    for (Review review : allReviews) {
                        loadReplies(review);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Review>>> call, Throwable t) {
                t.printStackTrace();
                Log.d("ReviewFragment", "fetchReviews onFailure: " + t.getMessage());
            }
        });
    }

    private void submitReview() {
        String comment = binding.commentInput.getEditText().getText().toString();
        int rating = (int) binding.ratingBar.getRating();

        if (!comment.trim().isEmpty() && rating > 0) {
            ReviewRequest request = new ReviewRequest(productId, rating, comment);
            api.createReview(request).enqueue(new Callback<BaseResponse<Review>>() {
                @Override
                public void onResponse(Call<BaseResponse<Review>> call, Response<BaseResponse<Review>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        BaseResponse<Review> body = response.body();
                        Review review = body.getData();
                        allReviews.add(0, review);
                        adapter.submitList(new ArrayList<>(allReviews));
                        updateEmptyState(allReviews);
                        binding.commentInput.getEditText().setText("");
                        binding.ratingBar.setRating(5f);
                        Toast.makeText(requireContext(), "Commented successful", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 400) {
                        Toast.makeText(requireContext(), "You already reviewed this product", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(requireContext(), "Please try again", Toast.LENGTH_SHORT).show();
                        Log.e("ReviewFragment", "Error: "+response.message());
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<Review>> call, Throwable t) {
                    t.printStackTrace();
                    Log.e("ReviewFragment", "onFailure_submitReview: "+t.getMessage());
                }
            });
        } else {
            Toast.makeText(requireContext(), "Please fill comment and rating", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteReview(Review review) {
        api.deleteReview(review.getReviewID()).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()) {
                    allReviews.remove(review);
                    adapter.submitList(allReviews);
                    updateEmptyState(allReviews);
                    Toast.makeText(requireContext(), "Deleted successful", Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 403) {
                    Toast.makeText(requireContext(), "You are not allowed to delete this review", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("ReviewFragment", "onResponse_error: "+response.errorBody());
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                t.printStackTrace();
                Log.e("ReviewFragment", "onFailure_deleteReview: "+t.getMessage());
            }
        });
    }

    private void voteReview(Review review, String voteType) {
        VoteRequest request = new VoteRequest(review.getReviewID(), voteType);

        api.vote(request).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()) {
                    fetchReviews();
                }
                else if(response.code() == 409) {
                    undoVote(review.getReviewID());
                }
                else Toast.makeText(requireContext(), "Error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                t.printStackTrace();
                Log.e("ReviewFragment", "onFailure_voteReview: "+t.getMessage());
            }
        });
    }

    private void undoVote(int reviewID) {
        api.undoVote(reviewID).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if(response.isSuccessful()) {
                    fetchReviews();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                t.printStackTrace();
                Log.e("ReviewFragment", "onFailure_undoVote: "+t.getMessage());
            }
        });
    }

    private void loadReplies(Review review) {
        api.getReplies(review.getReviewID()).enqueue(new Callback<BaseResponse<List<ReviewReply>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ReviewReply>>> call, Response<BaseResponse<List<ReviewReply>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ReviewReply> replies = response.body().getData();
                    adapter.updateReplies(review.getReviewID(), replies);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<ReviewReply>>> call, Throwable t) {
            }
        });
    }

    private void submitReply(Review review, String replyText) {
        ReviewReplyRequest request = new ReviewReplyRequest(replyText);
        api.createReply(review.getReviewID(), request).enqueue(new Callback<BaseResponse<ReviewReply>>() {
            @Override
            public void onResponse(Call<BaseResponse<ReviewReply>> call, Response<BaseResponse<ReviewReply>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loadReplies(review);
                    Toast.makeText(requireContext(), "Replied successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ReviewReply>> call, Throwable t) {
                t.printStackTrace();
                Log.d("ReviewFragment", "onFailure_submitReply: " + t.getMessage());
            }
        });
    }

    private void deleteReply(Review review, ReviewReply reply) {
        api.deleteReply(reply.getReplyID()).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()) {
                    loadReplies(review);
                    Toast.makeText(requireContext(), "Deleted successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Failed to delete reply", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(requireContext(), "Delete failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCurrentUserEmail() {
        SharedPreferences prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt_token", null);
        if (token == null) return null;

        try {
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                String payload = new String(android.util.Base64.decode(parts[1], android.util.Base64.DEFAULT));
                JSONObject json = new JSONObject(payload);
                return json.getString("sub");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getCurrentUserRole() {
        SharedPreferences prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt_token", null);
        if (token == null) return null;

        try {
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                String payload = new String(android.util.Base64.decode(parts[1], android.util.Base64.DEFAULT));
                JSONObject json = new JSONObject(payload);
                return json.getString("scope");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void filterReviewsByRating(int position) {
        if (position == 0) {
            adapter.submitList(new ArrayList<>(allReviews));
        } else {
            int selectedRating = position;
            List<Review> filtered = new ArrayList<>();
            for (Review review : allReviews) {
                if (review.getRating() == selectedRating) {
                    filtered.add(review);
                }
            }
            adapter.submitList(filtered);
            updateEmptyState(filtered);
        }
    }

    private void updateEmptyState(List<Review> list) {
        if (list.isEmpty()) {
            binding.emptyTextView.setVisibility(View.VISIBLE);
        } else {
            binding.emptyTextView.setVisibility(View.GONE);
        }
    }
}
