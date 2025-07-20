package com.example.productsaleprm.adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productsaleprm.R;
import com.example.productsaleprm.databinding.ItemReviewBinding;
import com.example.productsaleprm.model.Review;
import com.example.productsaleprm.model.ReviewReply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.productsaleprm.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviews = new ArrayList<>();
    private final String currentUser;
    private final String role;
    private final String admin = "ADMIN";
    private final OnReviewActionListener listener;
    private final Map<Integer, List<ReviewReply>> repliesMap = new HashMap<>();

    public interface OnReviewActionListener {
        void onDeleteReview(Review review);
        void onVoteReview(Review review, String voteType);
        void onLoadReplies(Review review);
        void onSubmitReply(Review review, String replyText);
        void onDeleteReply(Review review, ReviewReply reply);
    }

    public ReviewAdapter(String currentUser, String role, OnReviewActionListener listener) {
        this.currentUser = currentUser;
        this.role = role;
        this.listener = listener;
    }

    public void submitList(List<Review> newList) {
        if (newList != null) {
            reviews = newList;
            notifyDataSetChanged();
        }
    }

    public void updateReplies(int reviewId, List<ReviewReply> replies) {
        repliesMap.put(reviewId, replies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemReviewBinding binding = ItemReviewBinding.inflate(inflater, parent, false);
        return new ReviewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bind(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final ItemReviewBinding binding;

        public ReviewViewHolder(ItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Review review) {
            binding.textUserName.setText(review.getUsername());
            binding.textComment.setText(review.getComment());
            binding.textDate.setText(review.getCreatedAt());
            binding.textRating.setText(new String(new char[review.getRating()]).replace("\0", "★"));
            binding.textHelpfulCount.setText(review.getHelpfulCount() + " votes");

            boolean canModify = review.getEmail().equals(currentUser);
            binding.btnMore.setVisibility(canModify ? View.VISIBLE : View.GONE);
            binding.btnMore.setOnClickListener(v -> showPopupMenu(v, review));

            binding.btnUpvote.setOnClickListener(v -> listener.onVoteReview(review, "up"));

            boolean voted = review.isVotedByCurrentUser(currentUser);
            if (voted) {
                binding.btnUpvote.setBackgroundTintList(ContextCompat.getColorStateList(binding.getRoot().getContext(), R.color.colorTextPrimary));
                binding.btnUpvote.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.white));
            } else {
                binding.btnUpvote.setBackgroundTintList(ContextCompat.getColorStateList(binding.getRoot().getContext(), R.color.colorTextSecondary));
                binding.btnUpvote.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.black));
            }


            binding.editReply.setVisibility(View.GONE);
            binding.btnSubmitReply.setVisibility(View.GONE);

            boolean isAdmin = role != null && role.equals(admin);

            binding.btnShowReplies.setOnClickListener(v -> {
                boolean isVisible = binding.replyRecyclerView.getVisibility() == View.VISIBLE;
                if (isVisible) {
                    binding.replyRecyclerView.setVisibility(View.GONE);
                    binding.editReply.setVisibility(View.GONE);
                    binding.btnSubmitReply.setVisibility(View.GONE);
                } else {
                    listener.onLoadReplies(review);
                    binding.replyRecyclerView.setVisibility(View.VISIBLE);

                    if (isAdmin) {
                        binding.editReply.setVisibility(View.VISIBLE);
                        binding.btnSubmitReply.setVisibility(View.VISIBLE);
                    }
                    else {
                        binding.editReply.setVisibility(View.GONE);
                        binding.btnSubmitReply.setVisibility(View.GONE);
                    }
                }
            });

            binding.btnSubmitReply.setOnClickListener(v -> {
                String replyText = binding.editReply.getText().toString();
                if (!replyText.isEmpty()) {
                    listener.onSubmitReply(review, replyText);
                    binding.editReply.setText("");
                }
            });

            List<ReviewReply> replies = repliesMap.get(review.getReviewID());
            boolean hasReplies = replies != null && !replies.isEmpty();
            binding.btnShowReplies.setVisibility(hasReplies ? View.VISIBLE : View.GONE);

            if (hasReplies) {
                binding.replyRecyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
                binding.replyRecyclerView.setAdapter(new ReplyAdapter(replies, currentUser, role, reply -> listener.onDeleteReply(review, reply)));
                binding.replyRecyclerView.setVisibility(View.VISIBLE);
            } else {
                binding.replyRecyclerView.setVisibility(View.GONE);
            }

        }

        private void showPopupMenu(View anchor, Review review) {
            PopupMenu popup = new PopupMenu(anchor.getContext(), anchor);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_review_item, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_delete) {
                    listener.onDeleteReview(review);
                    return true;
                }
                return false;
            });
            popup.show();
        }
    }
}
