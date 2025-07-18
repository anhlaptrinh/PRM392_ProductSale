package com.example.productsaleprm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productsaleprm.R;
import com.example.productsaleprm.model.Order;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private final List<Order> orderList;
    private final Context context;
    private final OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);

    }

    public OrderHistoryAdapter(Context context, List<Order> orderList, OnOrderClickListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvDate.setText(order.getDate());
        holder.tvTotal.setText("$" + String.format("%.2f", order.getTotal()));
        holder.tvOrderId.setText("Order #" + order.getId());

        // Icon Shipped
        holder.iconShipped.setImageResource(
                order.isShipped() ? R.drawable.ic_radio_button_checked : R.drawable.ic_radio_button_unchecked
        );
        // Icon Arrives
        holder.iconArrives.setImageResource(
                order.isArrived() ? R.drawable.ic_radio_button_checked : R.drawable.ic_radio_button_unchecked
        );

        holder.itemOrderContainer.setOnClickListener(v -> listener.onOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTotal, tvOrderId;
        ImageView btnArrow, iconShipped, iconArrives;
        CardView itemOrderContainer;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvOrderDate);
            tvTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            btnArrow = itemView.findViewById(R.id.btnArrow);
            iconShipped = itemView.findViewById(R.id.iconShipped);
            iconArrives = itemView.findViewById(R.id.iconArrives);
            itemOrderContainer = itemView.findViewById(R.id.itemOrderContainer);
        }
    }

}
