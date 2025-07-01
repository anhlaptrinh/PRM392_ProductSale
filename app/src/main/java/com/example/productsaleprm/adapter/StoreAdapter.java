package com.example.productsaleprm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productsaleprm.R;
import com.example.productsaleprm.model.StoreLocation;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder>{
    private List<StoreLocation> stores;
    private final OnStoreClickListener listener;

    public interface OnStoreClickListener {
        void onClick(StoreLocation store);
    }

    public StoreAdapter(List<StoreLocation> stores, OnStoreClickListener listener) {
        this.stores = stores;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false);

        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        StoreLocation store = stores.get(position);
        holder.textView.setText("Store at (" + store.getLatitude() + ", " + store.getLongitude() + ")");
        holder.itemView.setOnClickListener(v -> listener.onClick(store));
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    static class StoreViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvStoreName);
        }
    }
}
