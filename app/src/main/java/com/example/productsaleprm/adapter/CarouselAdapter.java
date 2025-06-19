package com.example.productsaleprm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.productsaleprm.R;
import com.example.productsaleprm.interfaceui.OnItemCarouselClick;

import java.util.ArrayList;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {
    Context context;
    ArrayList<String> listItems;
    OnItemCarouselClick onItemCarouselClick;

    public CarouselAdapter(Context context, ArrayList<String> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_carousel_material,parent,false);
        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        Glide.with(context).load(listItems.get(position)).into(holder.imageView);
        holder.imageView.setOnClickListener(v -> onItemCarouselClick.onClick(holder.imageView,listItems.get(position)));

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void setOnItemCarouselClick(OnItemCarouselClick onItemCarouselClick) {
        this.onItemCarouselClick = onItemCarouselClick;
    }

    public static class CarouselViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.list_item_carousel);


        }
    }

}
