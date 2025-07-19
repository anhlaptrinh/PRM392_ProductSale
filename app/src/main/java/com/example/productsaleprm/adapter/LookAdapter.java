package com.example.productsaleprm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.productsaleprm.R;
import com.example.productsaleprm.model.Look;

import java.util.List;

public class LookAdapter extends RecyclerView.Adapter<LookAdapter.LookViewHolder> {

    private Context context;
    private List<Look> lookList;

    public LookAdapter(Context context, List<Look> lookList) {
        this.context = context;
        this.lookList = lookList;
    }

    @NonNull
    @Override
    public LookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_look, parent, false);
        return new LookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LookViewHolder holder, int position) {
        Look look = lookList.get(position);
        holder.tvTitle.setText(look.getTitle());
        holder.tvSubtitle.setText(look.getSubtitle());

        // Load ảnh từ URL bằng Glide
        Glide.with(context)
                .load(look.getImageUrl())

                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return lookList.size();
    }

    public static class LookViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle, tvSubtitle;

        public LookViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivLookImage);
            tvTitle = itemView.findViewById(R.id.tvLookTitle);
            tvSubtitle = itemView.findViewById(R.id.tvLookSubtitle);
        }
    }
}
