package com.example.productsaleprm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.productsaleprm.R;
import com.example.productsaleprm.model.Categories;

import java.util.List;


public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>{
    private List<Categories> categoriesList;

    public CategoriesAdapter(List<Categories> categoriesList) {
        this.categoriesList = categoriesList;
    }

    @NonNull
    @Override
    public CategoriesAdapter.CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categories, parent, false);


        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.CategoriesViewHolder holder, int position) {
        Categories categories = categoriesList.get(position);
        holder.productName.setText(categories.getName());
        Glide.with(holder.productImage.getContext())
                .load(categories.getImgUrl()) // product.getImgUrl() trả về String URL hoặc tên file drawable
                .placeholder(R.drawable.image_24px) // ảnh hiện khi đang tải
                .error(R.drawable.image_24px) // ảnh khi load lỗi
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }
    static class CategoriesViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.image_product);
            productName = itemView.findViewById(R.id.txt_product_name);
        }
    }
}
