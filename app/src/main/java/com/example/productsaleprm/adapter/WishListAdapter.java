package com.example.productsaleprm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.productsaleprm.R;
import com.example.productsaleprm.model.WishList;

import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishlistViewHolder> {
    private Context context;
    private List<WishList> wishlistList;

    public WishListAdapter(Context context, List<WishList> wishlistList) {
        this.context = context;
        this.wishlistList = wishlistList;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wishlist,parent,false);
        return new WishlistViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        WishList item = wishlistList.get(position);

        holder.tvName.setText(item.getProductName());
        holder.txtDesc.setText(item.getBriDesc());
        holder.txtPrice.setText("$"+item.getPrice());

        // Nếu ảnh là resource ID
        Glide.with(context)
                .load(item.getImageURL())
                .placeholder(R.drawable.ic_empty_product)
                .into(holder.imgProduct);

        // Gán rating cứng
        holder.ratingBar.setRating(4.9f);
        holder.txtRating.setText("(4.9)");

        holder.btnAdd.setOnClickListener(v -> {
            Toast.makeText(context, "Added to cart: " + item.getProductName(), Toast.LENGTH_SHORT).show();
        });

        holder.imgHeart.setOnClickListener(v -> {
            Toast.makeText(context, "Removed from wishlist: " + item.getProductName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return wishlistList.size();
    }

    static class WishlistViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProduct,imgHeart;
        TextView tvName, txtDesc, txtPrice, txtRating;
        ImageButton btnAdd;
        RatingBar ratingBar;



        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgHeart = itemView.findViewById(R.id.imgHeart);
            tvName = itemView.findViewById(R.id.tvName);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtRating = itemView.findViewById(R.id.txtRating);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }

}
