package com.example.productsaleprm.fragement;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.productsaleprm.adapter.BannerAdapter;
import com.example.productsaleprm.adapter.BrandAdapter;
import com.example.productsaleprm.adapter.CategoryAdapter;
import com.example.productsaleprm.adapter.LookAdapter;
import com.example.productsaleprm.adapter.ProductAdapter;
import com.example.productsaleprm.databinding.FragmentHomeBinding;
import com.example.productsaleprm.model.Brand;
import com.example.productsaleprm.model.Look;
import com.example.productsaleprm.model.Product;
import com.example.productsaleprm.model.response.CategoryListResponse;
import com.example.productsaleprm.model.response.ProductListResponse;
import com.example.productsaleprm.retrofit.ProductAPI;
import com.example.productsaleprm.retrofit.RetrofitClient;
import com.example.productsaleprm.retrofit.WishListAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private List<String> bannerList = new ArrayList<>();
    private List<Brand> brandList = new ArrayList<>();
    private List<Look> lookList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();

    private ProductAdapter productAdapter;

    private int pendingApiCalls = 0;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showLoading();
        setupBanner();
        setupBrands();
        setupLooks();
        setupProducts();
    }



    private void setupBanner() {
        bannerList.add("https://res.cloudinary.com/di9gy73rg/image/upload/v1752916959/banner1_cit5tt.jpg");
        bannerList.add("https://res.cloudinary.com/di9gy73rg/image/upload/v1752916962/banner2_newsss.jpg");
        bannerList.add("https://res.cloudinary.com/di9gy73rg/image/upload/v1752916959/banner3_b3xndl.jpg");
        bannerList.add("https://res.cloudinary.com/di9gy73rg/image/upload/v1752916959/banner4_aiw603.jpg");
        bannerList.add("https://res.cloudinary.com/di9gy73rg/image/upload/v1752916960/banner5_xm3seh.jpg");

        BannerAdapter bannerAdapter = new BannerAdapter(bannerList);
        binding.bannerViewPager.setAdapter(bannerAdapter);
    }

    private void setupBrands() {
        brandList.add(new Brand("Nike", "https://res.cloudinary.com/di9gy73rg/image/upload/v1752916968/nike_wgbbq6.png"));
        brandList.add(new Brand("Adidas", "https://res.cloudinary.com/di9gy73rg/image/upload/v1752916957/adidas_t7w6lg.png"));
        brandList.add(new Brand("Puma", "https://res.cloudinary.com/di9gy73rg/image/upload/v1752916969/puma_uqgmfy.png"));
        brandList.add(new Brand("Zara", "https://res.cloudinary.com/di9gy73rg/image/upload/v1752916974/zara_vuvfl8.png"));
        brandList.add(new Brand("Fog", "https://res.cloudinary.com/di9gy73rg/image/upload/v1752916959/fog_zwkyz0.png"));
        brandList.add(new Brand("Uniqlo", "https://res.cloudinary.com/di9gy73rg/image/upload/v1752916970/uniqlo_rn3l3x.png"));
        brandList.add(new Brand("Dior", "https://res.cloudinary.com/di9gy73rg/image/upload/v1752916958/dior_jstkwl.png"));
        brandList.add(new Brand("Gap", "https://res.cloudinary.com/di9gy73rg/image/upload/v1752916959/gap_ov0693.png"));

        BrandAdapter brandAdapter = new BrandAdapter(getContext(), brandList);
        binding.rvBrands.setLayoutManager(new GridLayoutManager(getContext(), 4));
        binding.rvBrands.setAdapter(brandAdapter);
    }

    private void setupLooks() {
        lookList.add(new Look("Minimalist", "Tokyo", "https://res.cloudinary.com/di9gy73rg/image/upload/v1752916961/look1_blz0yp.jpg"));
        lookList.add(new Look("Streetwear", "NYC", "https://res.cloudinary.com/di9gy73rg/image/upload/v1752916962/look2_dxbork.jpg"));
        lookList.add(new Look("Facion", "Japan", "https://res.cloudinary.com/di9gy73rg/image/upload/v1752916967/look3_ioohzt.jpg"));
        lookList.add(new Look("Streetwear", "China", "https://res.cloudinary.com/di9gy73rg/image/upload/v1752916969/look4_nj8edm.jpg"));
        lookList.add(new Look("Streetwear", "China", "https://res.cloudinary.com/di9gy73rg/image/upload/v1752916969/look5_lcbuuw.jpg"));

        LookAdapter lookAdapter = new LookAdapter(getContext(), lookList);
        binding.rvLooks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvLooks.setAdapter(lookAdapter);
    }

    private void setupProducts() {
        productAdapter = new ProductAdapter(getContext(), productList);
        binding.rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvProducts.setAdapter(productAdapter);

        // 👉 Xử lý sự kiện click trái tim như trong ProductActivity
        productAdapter.setOnProductWishlistClick((product, position) -> {
            WishListAPI api = RetrofitClient.getClient(getContext()).create(WishListAPI.class);

            if (!product.isFavorite()) {
                // Chưa yêu thích thì thêm vào wishlist
                api.addToWishlist(product.getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            product.setFavorite(true);
                            productAdapter.notifyItemChanged(position);
                            Toast.makeText(getContext(), "Added to wishlist", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to add to wishlist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "API error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // 👉 Gọi API để lấy danh sách sản phẩm
        ProductAPI productAPI = RetrofitClient.getClient(getContext()).create(ProductAPI.class);

        pendingApiCalls++;
        productAPI.getAllProducts().enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductListResponse> call, @NonNull Response<ProductListResponse> response) {
                if (!isAdded() || binding == null) return;

                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body().getData());
                    productAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Cannot get products", Toast.LENGTH_SHORT).show();
                }
                checkAllApiCompleted();
            }

            @Override
            public void onFailure(@NonNull Call<ProductListResponse> call, @NonNull Throwable t) {
                if (!isAdded() || binding == null) return;
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                checkAllApiCompleted();
            }
        });
    }
    private void showLoading() {
        if (binding != null && binding.progressBar != null) {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoading() {
        if (binding != null && binding.progressBar != null) {
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    private void checkAllApiCompleted() {
        pendingApiCalls--;
        if (pendingApiCalls <= 0) {
            hideLoading();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
