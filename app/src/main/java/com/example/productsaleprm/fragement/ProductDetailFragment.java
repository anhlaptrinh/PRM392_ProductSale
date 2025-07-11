package com.example.productsaleprm.fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class ProductDetailFragment extends Fragment {

    private ProductDetailBinding binding;
    private ProductAPI productAPI;
    private int quantity = 1;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ProductDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lấy token từ SharedPreferences
        String token = requireContext()
                .getSharedPreferences("auth", getContext().MODE_PRIVATE)
                .getString("jwt_token", "");

        // Tạo instance API
        productAPI = RetrofitClient.getClient(token).create(ProductAPI.class);

        // Nhận productId từ Bundle
        int productId = getArguments() != null ? getArguments().getInt("PRODUCT_ID", 0) : 0;

        loadProductDetail(productId);

        binding.btnPlus.setOnClickListener(v -> {
            quantity++;
            binding.textQuantity.setText(String.valueOf(quantity));
        });

        binding.btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                binding.textQuantity.setText(String.valueOf(quantity));
            }
        });
        binding.buttonAddToCart.setOnClickListener(v -> {
            int selectedQuantity = quantity;

            // CartAPI:
            CartAPI cartAPI = RetrofitClient.getClient(token).create(CartAPI.class);

            AddToCartRequest request = new AddToCartRequest(productId, selectedQuantity);

            cartAPI.addToCart(request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Thêm vào giỏ thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
    private void loadProductDetail(int productId) {
        Call<ProductResponse> call = productAPI.getProductById(productId);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponse> call,@NonNull Response<ProductResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body().getData();
                    String imageUrl = product.getImageURL();
                    // Load ảnh bằng Glide
                    Glide.with(requireContext())
                            .load(imageUrl)
                            .into(binding.imageProduct);
                    binding.textPrice.setText(String.valueOf(product.getPrice()));
                    binding.textTitle.setText(product.getProductName());
                    binding.textDescription.setText(product.getFullDesc());


                } else {
                    Toast.makeText(getContext(), "Không tải được chi tiết sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductResponse> call,@NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
