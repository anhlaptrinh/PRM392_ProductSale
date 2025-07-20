package com.example.productsaleprm.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productsaleprm.R;
import com.example.productsaleprm.databinding.ItemReplyBinding;
import com.example.productsaleprm.model.ReviewReply;

import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

    private List<ReviewReply> replies;
    private final String currentUser;
    private final String role;
    private final String admin = "ADMIN";
    private final OnReplyActionListener listener;

    public interface OnReplyActionListener {
        void onDeleteReply(ReviewReply reply);
    }

    public ReplyAdapter(List<ReviewReply> replies, String currentUser, String role, OnReplyActionListener listener) {
        this.replies = replies;
        this.currentUser = currentUser;
        this.role = role;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReplyBinding binding = ItemReplyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ReplyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        holder.bind(replies.get(position));
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    class ReplyViewHolder extends RecyclerView.ViewHolder {
        private final ItemReplyBinding binding;
        private boolean isDeleteVisible = false;

        public ReplyViewHolder(ItemReplyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ReviewReply reply) {
            binding.textReplyUser.setText(reply.getUsername());
            binding.textReplyContent.setText(reply.getReplyText());
            binding.textReplyDate.setText(reply.getRepliedAt());

            boolean isOwner = currentUser != null
                    && role != null
                    && role.equals(admin)
                    && currentUser.equals(reply.getEmail());
            binding.buttonMore.setVisibility(isOwner ? View.VISIBLE : View.GONE);

            binding.buttonMore.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(binding.getRoot().getContext(), v);
                popup.getMenuInflater().inflate(R.menu.menu_review_item, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.action_delete) {
                        if (listener != null) listener.onDeleteReply(reply);
                        return true;
                    }
                    return false;
                });
                popup.show();
            });
        }
    }
}
