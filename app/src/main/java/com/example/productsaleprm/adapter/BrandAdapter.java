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
import com.example.productsaleprm.model.Brand;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder> {

    private Context context;
    private List<Brand> brandList;

    public BrandAdapter(Context context, List<Brand> brandList) {
        this.context = context;
        this.brandList = brandList;
    }

    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_brand, parent, false);
        return new BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {
        Brand brand = brandList.get(position);
        holder.tvName.setText(brand.getName());

        Glide.with(context)
                .load(brand.getImageUrl()) // sửa từ getImageResId() → getImageUrl()

                .into(holder.ivBrand);
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    public static class BrandViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBrand;
        TextView tvName;

        public BrandViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBrand = itemView.findViewById(R.id.ivBrand);
            tvName = itemView.findViewById(R.id.tvBrandName);
        }
    }
}
