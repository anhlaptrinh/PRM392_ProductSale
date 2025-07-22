package com.example.productsaleprm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.productsaleprm.adapter.OrderItemAdapter;
import com.example.productsaleprm.databinding.ActivityPaymentSuccessBinding;
import com.example.productsaleprm.model.response.CartItemResponse;
import com.example.productsaleprm.retrofit.OrderApi;
import com.example.productsaleprm.retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentSuccessActivity extends AppCompatActivity {

    private ActivityPaymentSuccessBinding binding;

    private OrderItemAdapter adapter;
    private final List<CartItemResponse> cartItemList = new ArrayList<>();
    private OrderApi orderService;

    @RequiresApi(api = Build.VERSION_CODES.O) // vì dùng java.time
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Khởi tạo Retrofit API
        orderService = RetrofitClient.getClient(this).create(OrderApi.class);

        // Khởi tạo adapter và setup RecyclerView
        adapter = new OrderItemAdapter(cartItemList);
        binding.rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvOrderItems.setAdapter(adapter);

        // Nhận data từ Intent
        Intent intent = getIntent();
        String paymentMethod = intent.getStringExtra("PAYMENT_METHOD");
        int orderId = intent.getIntExtra("ORDER_ID", -1);
        String totalAmount = intent.getStringExtra("TOTAL_AMOUNT");
        String paymentDateStr = intent.getStringExtra("PAYMENT_DATE");
        String paymentStatus = intent.getStringExtra("PAYMENT_STATUS");

        loadOrderItems(orderId);
            // Bind dữ liệu
            binding.tvItems.setText("Order #" + orderId);
            binding.tvTotalAmount.setText("$" + totalAmount);
            binding.tvPayedBy.setText(paymentMethod);
            binding.tvTitle.setText("ORDER SUCCESSFUL");
            binding.tvDescription.setText("Your payment has been successfully! Details of transaction are included below.");

            // Parse & format ngày
            LocalDateTime paymentDate = LocalDateTime.parse(paymentDateStr); // ISO string
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
            String formattedDate = paymentDate.format(formatter);

            binding.tvDate.setText(formattedDate);

            binding.btnScreenshot.setOnClickListener(v -> {
            takeScreenshot();
        });

        binding.btnHome.setOnClickListener(v -> {
            Intent intent2 = new Intent(PaymentSuccessActivity.this, MainActivity.class);
            startActivity(intent2); // Intent để về MainActivity
        });
    }

    private void loadOrderItems(int orderId) {
        orderService.getOrderItems(orderId).enqueue(new Callback<List<CartItemResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<CartItemResponse>> call, @NonNull Response<List<CartItemResponse>> response) {
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
                    Toast.makeText(PaymentSuccessActivity.this, "Failed to load items", Toast.LENGTH_SHORT).show();
                    Log.e("RESPONSE_ERROR", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CartItemResponse>> call, @NonNull Throwable t) {
                Log.e("API_FAILURE", "Error: ", t);
                Toast.makeText(PaymentSuccessActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void takeScreenshot() {
        try {
            // Lấy toàn bộ root view của activity
            View rootView = getWindow().getDecorView().getRootView();

            // Tạo bitmap và vẽ vào canvas
            Bitmap screenshot = Bitmap.createBitmap(rootView.getWidth(), rootView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(screenshot);
            rootView.draw(canvas);

            // Tạo thư mục lưu ảnh
            File path = new File(getExternalFilesDir(null), "Screenshots");
            if (!path.exists()) path.mkdirs();

            // Tạo file
            String fileName = "screenshot_" + System.currentTimeMillis() + ".png";
            File imageFile = new File(path, fileName);

            // Lưu ảnh
            FileOutputStream fos = new FileOutputStream(imageFile);
            screenshot.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            Toast.makeText(this, "Đã chụp màn hình: " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Chụp màn hình thất bại", Toast.LENGTH_SHORT).show();
        }
    }

}
