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
import com.example.productsaleprm.model.CartItem;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final Context context;
    private final List<CartItem> orderList;

    public OrderAdapter(Context context, List<CartItem> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        CartItem item = orderList.get(position);

        holder.tvProductName.setText(item.getProduct().getName());
        holder.tvQuantity.setText("Qty: " + item.getQuantity());
        holder.tvPrice.setText("$" + item.getPrice());

        Glide.with(context)
                .load(item.getProduct().getImagePath())
                .placeholder(R.drawable.ic_cart)
                .into(holder.imgItem);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvQuantity, tvPrice;
        ImageView imgItem;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgItem = itemView.findViewById(R.id.imgItem);
        }
    }
}
