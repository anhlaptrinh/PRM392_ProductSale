package com.example.productsaleprm.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.productsaleprm.R;
import com.example.productsaleprm.activity.ProductDetailActivity;
import com.example.productsaleprm.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    // Callback cho trái tim (wishlist)
    public interface OnProductWishlistClick {
        void onHeartClick(Product product, int position);
    }

    // Callback cho click vào item
    public interface OnProductItemClickListener {
        void onProductClick(Product product);
    }

    private OnProductWishlistClick wishlistClickListener;
    private OnProductItemClickListener productClickListener;

    public void setOnProductWishlistClick(OnProductWishlistClick listener) {
        this.wishlistClickListener = listener;
    }

    public void setOnProductItemClickListener(OnProductItemClickListener listener) {
        this.productClickListener = listener;
    }

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.ivProductImage.setOnClickListener(v -> {
            int productId = product.getProductID();
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", productId);
            context.startActivity(intent);
        });
        holder.tvName.setText(product.getProductName());
        holder.tvPrice.setText(product.getPrice().toPlainString() + " ₫");
        Glide.with(context).load(product.getImageURL()).into(holder.ivProductImage);

        // Set icon yêu thích
        if (product.isFavorite()) {
            holder.ivHeart.setImageResource(R.drawable.ic_heart_filled);
        } else {
            holder.ivHeart.setImageResource(R.drawable.ic_heart_outline);
        }

        // Bấm trái tim (toggle wishlist)
        holder.ivHeart.setOnClickListener(v -> {
            if (wishlistClickListener != null) {
                wishlistClickListener.onHeartClick(product, holder.getAdapterPosition());
            }
        });

        // Bấm item → chuyển sang màn chi tiết
        holder.itemView.setOnClickListener(v -> {
            if (productClickListener != null) {
                productClickListener.onProductClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage, ivHeart;
        TextView tvName, tvPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            ivHeart = itemView.findViewById(R.id.ivHeart); // trái tim
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }
}
