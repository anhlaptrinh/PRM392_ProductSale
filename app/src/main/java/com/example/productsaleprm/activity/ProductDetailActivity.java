package com.example.productsaleprm.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.productsaleprm.databinding.ProductDetailBinding;
import com.example.productsaleprm.model.Product;
import com.example.productsaleprm.model.response.ProductResponse;
import com.example.productsaleprm.model.resquest.AddToCartRequest;
import com.example.productsaleprm.retrofit.CartAPI;
import com.example.productsaleprm.retrofit.ProductAPI;
import com.example.productsaleprm.retrofit.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ProductDetailBinding binding;
    private ProductAPI productAPI;
    private int quantity = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo ViewBinding
        binding = ProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Nút quay lại
        binding.btnBack.setOnClickListener(v -> finish());

        // Lấy JWT token
        String token = getSharedPreferences("auth", MODE_PRIVATE)
                .getString("jwt_token", "");

        // Tạo ProductAPI
        productAPI = RetrofitClient.getClient(this
        ).create(ProductAPI.class);

        // Nhận productId từ Intent
        int productId = getIntent().getIntExtra("PRODUCT_ID", 0);

        // Load chi tiết sản phẩm
        loadProductDetail(productId);

        // Tăng số lượng
        binding.btnPlus.setOnClickListener(v -> {
            quantity++;
            binding.textQuantity.setText(String.valueOf(quantity));
        });

        // Giảm số lượng
        binding.btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                binding.textQuantity.setText(String.valueOf(quantity));
            }
        });

        // Thêm vào giỏ hàng
        binding.buttonAddToCart.setOnClickListener(v -> {
            CartAPI cartAPI = RetrofitClient.getClient(this).create(CartAPI.class);
            AddToCartRequest request = new AddToCartRequest(productId, quantity);

            cartAPI.addToCart(request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ProductDetailActivity.this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(ProductDetailActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void loadProductDetail(int productId) {
        productAPI.getProductById(productId).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body().getData();

                    Glide.with(ProductDetailActivity.this)
                            .load(product.getImageURL())
                            .into(binding.imageProduct);

                    binding.textTitle.setText(product.getProductName());
                    binding.textPrice.setText(String.valueOf(product.getPrice()));
                    binding.textDescription.setText(product.getFullDesc());
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Không tải được chi tiết sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
