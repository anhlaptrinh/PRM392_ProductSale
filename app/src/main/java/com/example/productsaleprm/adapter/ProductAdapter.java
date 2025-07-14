package com.example.productsaleprm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.productsaleprm.R;
import com.example.productsaleprm.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> products) {
        this.productList = products;
    }

    public void setProductList(List<Product> products) {
        this.productList = products;
        notifyDataSetChanged();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getProductName());
        holder.tvPrice.setText(product.getPrice().toPlainString() + "đ");
        holder.tvDescription.setText(product.getFullDesc());


        // Hiển thị hình ảnh nếu có
        Glide.with(holder.itemView.getContext())
                .load(product.getImageURL())
                .placeholder(R.drawable.placeholder_image) // thêm ảnh mặc định nếu cần
                .into(holder.ivImage);
        boolean isFavorite = product.isFavorite(); // ví dụ getter

        if (isFavorite) {
            holder.imgHeart.setImageResource(R.drawable.ic_heart_filled);
        } else {
            holder.imgHeart.setImageResource(R.drawable.ic_heart_outline);
        }

        holder.imgHeart.setOnClickListener(v -> {
            boolean newFavoriteState = !product.isFavorite();
            product.setFavorite(newFavoriteState);
            notifyItemChanged(position);


            if (newFavoriteState) {
                Toast.makeText(holder.itemView.getContext(), "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(holder.itemView.getContext(), "Đã bỏ yêu thích", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice ,tvDescription;
        ImageView ivImage;
        public ImageView imgHeart;


        public ProductViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textProductName);
            tvPrice = itemView.findViewById(R.id.textProductPrice);
            tvDescription = itemView.findViewById(R.id.textProductDescription);
            ivImage = itemView.findViewById(R.id.imageProduct);
            imgHeart = itemView.findViewById(R.id.imgHeart);
        }
    }
}
