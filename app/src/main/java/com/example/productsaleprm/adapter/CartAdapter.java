package com.example.productsaleprm.adapter;

import android.annotation.SuppressLint;
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
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private List<CartItem> cartList;
    private OnCartChangedListener listener;
    private OnCartActionListener actionListener;
    private Context context;
    public interface OnCartChangedListener {
        void onCartUpdated();
    }
    public interface  OnCartActionListener{
        void deleteCartItem(CartItem item, int position);
        void updateCartItem(CartItem item, int position, boolean isIncrease);
    }
    public void setOnCartChangedListener(OnCartChangedListener listener) {
        this.listener = listener;
    }
    public void setOnCartActionListener(OnCartActionListener listener) {
        this.actionListener = listener;
    }

    public CartAdapter(Context context, List<CartItem> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false);

        return new CartViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartList.get(position);

        // Gán tên sản phẩm
        holder.tvProductName.setText(item.getProduct().getName());

        // Gán số lượng
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        // Gán giá (định dạng nếu cần)
        holder.tvPrice.setText("$" + item.getPrice());

        // Load ảnh bằng Glide (giả sử bạn có URL ảnh trong item.getImageUrl())
        Glide.with(context)
                .load(item.getProduct().getImagePath()) // hoặc đổi sang resource nếu bạn dùng drawable
                .placeholder(R.drawable.ic_cart) // ảnh tạm khi loading
                .into(holder.imgItemCart);

        // Bắt sự kiện nút tăng giảm số lượng (nếu cần)
        holder.btnPlus.setOnClickListener(v -> {

            if (actionListener != null) {
                actionListener.updateCartItem(item, position, true);
            }
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.updateCartItem(item, position, false);
            }

        });
        holder.btnDelete.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.deleteCartItem(item, position);
            }
        });

    }
    public void removeItemAndNotify(int position) {
        if (position >= 0 && position < cartList.size()) {
            cartList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartList.size());

            // 🔁 Gọi callback về Fragment
            if (listener != null) {
                listener.onCartUpdated();
            }
        }
    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }


    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvQuantity, tvPrice;
        MaterialButton btnPlus, btnMinus;
        ImageView imgItemCart, btnDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            imgItemCart = itemView.findViewById(R.id.imgItemCart);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }
}
