package com.example.productsaleprm.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.productsaleprm.R;
import com.example.productsaleprm.adapter.OrderAdapter;
import com.example.productsaleprm.databinding.ActivityOrderBinding;
import com.example.productsaleprm.model.CartItem;
import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.model.response.CartResponseData;
import com.example.productsaleprm.model.response.PaymentResponse;
import com.example.productsaleprm.model.resquest.CreateOrderRequest;
import com.example.productsaleprm.retrofit.CartAPI;
import com.example.productsaleprm.retrofit.OrderApi;
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
    private final int pageSize = 5;
    private BigDecimal totalAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Nút quay lại
        binding.btnBack.setOnClickListener(v -> finish());

        // Setup RecyclerView
        orderAdapter = new OrderAdapter(this, cartList);
        binding.recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerCart.setAdapter(orderAdapter);

        // Lấy dữ liệu giỏ hàng
        fetchCartData(0);

        // Nút thanh toán
        binding.btnCheckout.setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(OrderActivity.this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedId = binding.rgPaymentMethod.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(OrderActivity.this, "Vui lòng chọn phương thức thanh toán!", Toast.LENGTH_SHORT).show();
                return;
            }

            String paymentMethod = "";
            if (selectedId == R.id.rb_cod) {
                paymentMethod = "COD";
            } else if (selectedId == R.id.rb_momo) {
                paymentMethod = "MOMO";
            }

            final String finalPaymentMethod = paymentMethod;

            int userId = getIntent().getIntExtra("USER_ID", 3); // ví dụ bạn truyền userId từ màn khác
            if (userId == -1) {
                Toast.makeText(OrderActivity.this, "Không tìm thấy User!", Toast.LENGTH_SHORT).show();
                return;
            }

            CreateOrderRequest request = new CreateOrderRequest(userId, totalAmount, paymentMethod);

            // Gọi API
            OrderApi orderAPI = RetrofitClient.getClient(this).create(OrderApi.class);
            Call<BaseResponse<PaymentResponse>> call = orderAPI.createOrder(request);

            // 4. Xử lý response
            call.enqueue(new Callback<BaseResponse<PaymentResponse>>() {
                @Override
                public void onResponse(Call<BaseResponse<PaymentResponse>> call, Response<BaseResponse<PaymentResponse>> response) {
                    if (response.isSuccessful()) {
                        if ("MOMO".equalsIgnoreCase(finalPaymentMethod)) {
                            PaymentResponse payment = response.body().getData();
                            String payUrl = payment.getPaymentUrl();
                            String qrCodeUrl = payment.getQrCodeUrl();

                            Intent intent = new Intent(OrderActivity.this, MomoPaymentActivity.class);
                            intent.putExtra("QR_CODE_URL", qrCodeUrl);
                            intent.putExtra("PAY_URL", payUrl);
                            startActivity(intent);
                        } else {
                            // COD ➜ backend trả 204 No Content ➜ sẽ không chạy ở đây vì Retrofit coi 204 là empty
                            Intent intent = new Intent(OrderActivity.this, PaymentSuccessActivity.class);
                            intent.putExtra("PAYMENT_METHOD", "COD");
                            startActivity(intent);
                        }
                    } else if (response.code() == 204) {
                        // Trường hợp COD ➜ backend trả 204
                        Intent intent = new Intent(OrderActivity.this, PaymentSuccessActivity.class);
                        intent.putExtra("PAYMENT_METHOD", "COD");
                        startActivity(intent);
                    } else {
                        Toast.makeText(OrderActivity.this, "Đặt hàng thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<PaymentResponse>> call, Throwable t) {
                    Toast.makeText(OrderActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void fetchCartData(int page) {
        CartAPI api = RetrofitClient.getClient(this).create(CartAPI.class);
        binding.progressBar.setVisibility(View.VISIBLE);

        api.getCartItems(page, pageSize).enqueue(new Callback<BaseResponse<CartResponseData>>() {
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
        totalAmount = BigDecimal.ZERO;
        for (CartItem item : cartList) {
            totalAmount = totalAmount.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        binding.tvTotalAmount.setText("$" + totalAmount);
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
