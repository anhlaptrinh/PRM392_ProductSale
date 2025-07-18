package com.example.productsaleprm.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.productsaleprm.databinding.ActivityPaymentSuccessBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentSuccessActivity extends AppCompatActivity {

    private ActivityPaymentSuccessBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O) // vì dùng java.time
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Nhận data từ Intent
        Intent intent = getIntent();
        String paymentMethod = intent.getStringExtra("PAYMENT_METHOD");

        if ("COD".equalsIgnoreCase(paymentMethod)) {
            int orderId = intent.getIntExtra("ORDER_ID", -1);
            String totalAmount = intent.getStringExtra("TOTAL_AMOUNT");
            String paymentDateStr = intent.getStringExtra("PAYMENT_DATE");
            String paymentStatus = intent.getStringExtra("PAYMENT_STATUS");

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
        }

        binding.btnHome.setOnClickListener(v -> {
            Intent intent2 = new Intent(PaymentSuccessActivity.this, MainActivity.class);
            startActivity(intent2); // Intent để về MainActivity
        });
    }
}
