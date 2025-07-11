package com.example.productsaleprm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.productsaleprm.adapter.OrderAdapter;
import com.example.productsaleprm.databinding.ActivityOrderBinding;
import com.example.productsaleprm.model.CartItem;
import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.model.response.CartResponseData;
import com.example.productsaleprm.retrofit.CartAPI;
import com.example.productsaleprm.retrofit.RetrofitClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private ActivityOrderBinding binding;
    private OrderAdapter orderAdapter;
    private List<CartItem> cartList = new ArrayList<>();
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        token = getSharedPreferences("auth", MODE_PRIVATE)
                .getString("jwt_token", "");

        // Nút quay lại
        binding.btnBack.setOnClickListener(v -> finish());

        // Setup RecyclerView
        orderAdapter = new OrderAdapter(this, cartList);
        binding.recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerCart.setAdapter(orderAdapter);

        // Lấy dữ liệu giỏ hàng
        fetchCartData();

        // Nút thanh toán
        binding.btnCheckout.setOnClickListener(v -> {
            Toast.makeText(this, "Đang xử lý thanh toán...", Toast.LENGTH_SHORT).show();
            // TODO: Thêm logic xử lý thanh toán thật
        });
    }

    private void fetchCartData() {
        CartAPI api = RetrofitClient.getClient(token).create(CartAPI.class);
        binding.progressBar.setVisibility(View.VISIBLE);

        api.getCartItems().enqueue(new Callback<BaseResponse<CartResponseData>>() {
            @Override
            public void onResponse(Call<BaseResponse<CartResponseData>> call, Response<BaseResponse<CartResponseData>> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    CartResponseData data = response.body().getData();
                    List<CartItem> items = data.getCartItems() != null ? data.getCartItems() : new ArrayList<>();
                    cartList.clear();
                    cartList.addAll(items);
                    orderAdapter.notifyDataSetChanged();
                    updateTotalAmount();
                    checkEmptyCart();
                } else {
                    Toast.makeText(OrderActivity.this, "Không thể lấy giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<CartResponseData>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(OrderActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartList) {
            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        binding.tvTotalAmount.setText("$" + total);
    }

    private void checkEmptyCart() {
        if (orderAdapter.getItemCount() == 0) {
            binding.recyclerCart.setVisibility(View.GONE);
            binding.emptyLayout.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerCart.setVisibility(View.VISIBLE);
            binding.emptyLayout.setVisibility(View.GONE);
        }
    }
}
