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

        // 1️⃣ Set tất cả icon mặc định là "unchecked" và hiển thị
        holder.iconPending.setVisibility(View.VISIBLE);
        holder.iconPending.setImageResource(R.drawable.ic_radio_button_unchecked);

        holder.iconShipped.setVisibility(View.VISIBLE);
        holder.iconShipped.setImageResource(R.drawable.ic_radio_button_unchecked);

        holder.iconArrives.setVisibility(View.VISIBLE);
        holder.iconArrives.setImageResource(R.drawable.ic_radio_button_unchecked);

        holder.iconCancelled.setVisibility(View.VISIBLE);
        holder.iconCancelled.setImageResource(R.drawable.ic_radio_button_unchecked);

        // Chỉ hiển thị icon phù hợp
        switch (order.getStatus()) {
            case "pending":
                holder.iconPending.setVisibility(View.VISIBLE);
                holder.iconPending.setImageResource(R.drawable.ic_radio_button_checked);
                break;
            case "shipping":
                holder.iconShipped.setVisibility(View.VISIBLE);
                holder.iconShipped.setImageResource(R.drawable.ic_radio_button_checked);
                break;
            case "arrived":
                holder.iconArrives.setVisibility(View.VISIBLE);
                holder.iconArrives.setImageResource(R.drawable.ic_radio_button_checked);
                break;
            case "cancelled":
                holder.iconCancelled.setVisibility(View.VISIBLE);
                holder.iconCancelled.setImageResource(R.drawable.ic_radio_button_checked);
                break;
        }


        holder.itemOrderContainer.setOnClickListener(v -> listener.onOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTotal, tvOrderId;
        ImageView btnArrow, iconShipped, iconArrives, iconPending, iconCancelled;
        CardView itemOrderContainer;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvOrderDate);
            tvTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            btnArrow = itemView.findViewById(R.id.btnArrow);
            iconPending = itemView.findViewById(R.id.iconPending);
            iconShipped = itemView.findViewById(R.id.iconShipped);
            iconArrives = itemView.findViewById(R.id.iconArrives);
            iconCancelled = itemView.findViewById(R.id.iconCancelled);
            itemOrderContainer = itemView.findViewById(R.id.itemOrderContainer);
        }
    }

}
