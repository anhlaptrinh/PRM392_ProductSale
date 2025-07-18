package com.example.productsaleprm.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productsaleprm.databinding.ItemReplyBinding;
import com.example.productsaleprm.model.ReviewReply;

import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

    private List<ReviewReply> replies;

    public ReplyAdapter(List<ReviewReply> replies) {
        this.replies = replies;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemReplyBinding binding = ItemReplyBinding.inflate(inflater, parent, false);
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

    static class ReplyViewHolder extends RecyclerView.ViewHolder {
        private final ItemReplyBinding binding;

        public ReplyViewHolder(ItemReplyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ReviewReply reply) {
            binding.textReplyUser.setText(reply.getUsername());
            binding.textReplyContent.setText(reply.getReplyText());
            binding.textReplyDate.setText(reply.getRepliedAt());
        }
    }
}
