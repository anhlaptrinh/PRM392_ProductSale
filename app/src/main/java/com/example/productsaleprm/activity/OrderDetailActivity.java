package com.example.productsaleprm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.productsaleprm.adapter.OrderItemAdapter;
import com.example.productsaleprm.databinding.ActivityOrderDetailBinding;
import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.model.response.CartItemResponse;
import com.example.productsaleprm.model.response.OrderDetailResponse;
import com.example.productsaleprm.model.resquest.ReorderRequest;
import com.example.productsaleprm.retrofit.OrderApi;
import com.example.productsaleprm.retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    private OrderItemAdapter adapter;
    private final List<CartItemResponse> cartItemList = new ArrayList<>();
    private OrderApi orderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnBack.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("ORDER_UPDATED", true);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
        int orderId = getIntent().getIntExtra("ORDER_ID", -1);
        String orderStatus = getIntent().getStringExtra("ORDER_STATUS");
        if (orderId == -1) {
            Toast.makeText(this, "Order ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        orderService = RetrofitClient.getClient(this).create(OrderApi.class);
        setupRecyclerView();
        loadOrderItems(orderId);
        loadOrderDetail(orderId);
        binding.btnArrived.setOnClickListener(view -> {
            String isoDateNow = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault()).format(new Date());
            orderService.updateOrder(orderId , "arrived", isoDateNow).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(OrderDetailActivity.this, "Đã đánh dấu đơn hàng hoàn thành", Toast.LENGTH_SHORT).show();
                        binding.tvStatus.setText("Status: Arrived");
                        loadOrderDetail(orderId);
                    } else {
                        Toast.makeText(OrderDetailActivity.this, "Cập nhật trạng thái thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Toast.makeText(OrderDetailActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.btnCancel.setOnClickListener(v -> {
            String isoDateNow = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault()).format(new Date());
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Xác nhận hủy đơn")
                    .setMessage("Bạn có muốn hủy đơn hàng này không?")
                    .setPositiveButton("Hủy đơn", (dialog, which) -> {
                        // Gọi API cập nhật status thành "cancelled"
                        orderService.updateOrder(orderId ,"cancelled",isoDateNow).enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                                if (response.isSuccessful()) {
                                    Toast.makeText(OrderDetailActivity.this, "Đơn hàng đã được hủy", Toast.LENGTH_SHORT).show();
                                    loadOrderDetail(orderId); // reload lại giao diện
                                } else {
                                    Toast.makeText(OrderDetailActivity.this, "Hủy đơn thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                Toast.makeText(OrderDetailActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    })
                    .setNegativeButton("Đóng", null)
                    .show();
        });


        binding.btnReorder.setOnClickListener(v -> {
            binding.loadingOverlay.setVisibility(View.VISIBLE); // 👉 show loading

            List<ReorderRequest.Item> reorderItems = new ArrayList<>();
            for (CartItemResponse item : cartItemList) {
                reorderItems.add(new ReorderRequest.Item(item.getProductId(), item.getQuantity()));
            }

            ReorderRequest request = new ReorderRequest(reorderItems);
            OrderApi orderApi = RetrofitClient.getClient(OrderDetailActivity.this).create(OrderApi.class);

            orderApi.reorder(request).enqueue(new Callback<BaseResponse<Boolean>>() {
                @Override
                public void onResponse(Call<BaseResponse<Boolean>> call, Response<BaseResponse<Boolean>> response) {
                    if (response.isSuccessful() && Boolean.TRUE.equals(response.body().getData())) {
                        Toast.makeText(OrderDetailActivity.this, "Đã thêm lại giỏ hàng!", Toast.LENGTH_SHORT).show();

                        // Delay để đảm bảo backend xử lý xong
                        binding.loadingOverlay.postDelayed(() -> {
                            binding.loadingOverlay.setVisibility(View.GONE);
                            Intent intent = new Intent(OrderDetailActivity.this, MainActivity.class);
                            intent.putExtra("open_cart", true);
                            startActivity(intent);
                            finish();
                        }, 400);
                    } else {
                        binding.loadingOverlay.setVisibility(View.GONE);
                        Toast.makeText(OrderDetailActivity.this, "Thêm lại giỏ hàng thất bại!", Toast.LENGTH_SHORT).show();
                        try {
                            if (response.errorBody() != null) {
                                Log.e("REORDER_ERROR", response.errorBody().string());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<Boolean>> call, Throwable t) {
                    binding.loadingOverlay.setVisibility(View.GONE);
                    Log.e("REORDER_FAILURE", "Error: " + t.getMessage(), t);
                    Toast.makeText(OrderDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    private void setupRecyclerView() {
        adapter = new OrderItemAdapter(cartItemList);
        binding.recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerCart.setAdapter(adapter);
    }

    private void loadOrderItems(int orderId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        orderService.getOrderItems(orderId).enqueue(new Callback<List<CartItemResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<CartItemResponse>> call, @NonNull Response<List<CartItemResponse>> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<CartItemResponse> itemList = response.body();
                    Log.d("ORDER_ITEM_JSON", new Gson().toJson(itemList));

                    // Cập nhật danh sách và thông báo cho adapter
                    cartItemList.clear();                   // Xóa dữ liệu cũ nếu có
                    cartItemList.addAll(itemList);         // Thêm dữ liệu mới
                    adapter.notifyDataSetChanged();        // Cập nhật RecyclerView

                    // Ẩn empty layout nếu có dữ liệu
                    if (!itemList.isEmpty()) {
                        binding.emptyLayout.setVisibility(View.GONE);
                    } else {
                        binding.emptyLayout.setVisibility(View.VISIBLE);
                    }

                } else {
                    Toast.makeText(OrderDetailActivity.this, "Failed to load items", Toast.LENGTH_SHORT).show();
                    Log.e("RESPONSE_ERROR", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CartItemResponse>> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Log.e("API_FAILURE", "Error: ", t);
                Toast.makeText(OrderDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOrderDetail(int orderId) {
        orderService.getOrderById(orderId).enqueue(new Callback<BaseResponse<OrderDetailResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<OrderDetailResponse>> call, Response<BaseResponse<OrderDetailResponse>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    OrderDetailResponse order = response.body().getData();

                    binding.tvOrderId.setText("Order ID: #" + order.getOrderId());
                    binding.tvOrderDate.setText("Order Date: " + formatDate(order.getOrderDate()));
                    binding.tvStatus.setText("Status: " + order.getOrderStatus());
                    binding.tvAddress.setText("Address: " + order.getAddress());
                    binding.tvTotalAmount.setText("Total Amount: $" + order.getTotal());

                    String status = order.getOrderStatus().toLowerCase();

                    switch (status) {
                        case "pending":
                            binding.btnCancel.setVisibility(View.VISIBLE);
                            binding.btnArrived.setVisibility(View.GONE);
                            binding.btnReorder.setVisibility(View.GONE);
                            break;

                        case "shipping":
                            binding.btnCancel.setVisibility(View.GONE);
                            binding.btnArrived.setVisibility(View.VISIBLE);
                            binding.btnReorder.setVisibility(View.GONE);
                            break;

                        case "arrived":
                        case "cancelled":
                            binding.btnCancel.setVisibility(View.GONE);
                            binding.btnArrived.setVisibility(View.GONE);
                            binding.btnReorder.setVisibility(View.VISIBLE);
                            break;

                        default:
                            // fallback
                            binding.btnCancel.setVisibility(View.GONE);
                            binding.btnArrived.setVisibility(View.GONE);
                            binding.btnReorder.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Failed to load order info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<OrderDetailResponse>> call, Throwable t) {
                Log.e("ORDER_DETAIL_API", "onFailure: ", t);
                Toast.makeText(OrderDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatDate(String rawDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            Date date = inputFormat.parse(rawDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return rawDate; // fallback nếu lỗi
        }
    }
}
