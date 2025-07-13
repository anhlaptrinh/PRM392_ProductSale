package com.example.productsaleprm.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.productsaleprm.databinding.ActivityPaymentSuccessBinding;

public class PaymentSuccessActivity extends AppCompatActivity {

    private ActivityPaymentSuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Hiển thị nội dung thành công, ví dụ:


        binding.btnHome.setOnClickListener(v -> {
            finish(); // hoặc Intent để về MainActivity
        });
    }
}
