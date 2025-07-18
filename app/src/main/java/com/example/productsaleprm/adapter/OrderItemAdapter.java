package com.example.productsaleprm.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.productsaleprm.R;
import com.example.productsaleprm.databinding.ItemOrderBinding;
import com.example.productsaleprm.model.response.CartItemResponse;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
    private final List<CartItemResponse> itemList;

    public OrderItemAdapter(List<CartItemResponse> itemList) {
        this.itemList = itemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrderBinding binding;

        public ViewHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public OrderItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemOrderBinding binding = ItemOrderBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItemResponse item = itemList.get(position);

        holder.binding.tvProductName.setText(item.getProductName());
        holder.binding.tvQuantity.setText("Qty: " + item.getQuantity());
        holder.binding.tvPrice.setText("$" + item.getPrice());

        if (item.getImageURL() != null) {
            Glide.with(holder.binding.imgItem.getContext())
                    .load(item.getImageURL())
                    .into(holder.binding.imgItem);
        } else {
            holder.binding.imgItem.setImageResource(R.drawable.ic_empty_product);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
