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

import com.example.productsaleprm.R;
import com.example.productsaleprm.adapter.BannerAdapter;
import com.example.productsaleprm.adapter.BrandAdapter;
import com.example.productsaleprm.adapter.CategoryAdapter;
import com.example.productsaleprm.adapter.LookAdapter;
import com.example.productsaleprm.adapter.ProductAdapter;
import com.example.productsaleprm.databinding.FragmentHomeBinding;
import com.example.productsaleprm.model.Brand;
import com.example.productsaleprm.model.Category;
import com.example.productsaleprm.model.Look;
import com.example.productsaleprm.model.Product;
import com.example.productsaleprm.model.response.CategoryListResponse;
import com.example.productsaleprm.model.response.ProductListResponse;
import com.example.productsaleprm.retrofit.CategoryAPI;
import com.example.productsaleprm.retrofit.ProductAPI;
import com.example.productsaleprm.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private List<Category> categoryList = new ArrayList<>();
    private List<Integer> bannerList = new ArrayList<>();
    private List<Brand> brandList = new ArrayList<>();
    private List<Look> lookList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();

    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupCategory();
        setupBanner();
        setupBrands();
        setupLooks();
        setupProducts();
    }

    private void setupCategory() {
        categoryList = new ArrayList<>();

        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategories.setAdapter(categoryAdapter);

        CategoryAPI categoryAPI = RetrofitClient.getClient(getContext()).create(CategoryAPI.class);

        categoryAPI.getAllCategories().enqueue(new Callback<CategoryListResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryListResponse> call, @NonNull Response<CategoryListResponse> response) {
                if (!isAdded() || binding == null) return;

                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    categoryList.clear();
                    categoryList.addAll(response.body().getData());
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Unable to get directory", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryListResponse> call, @NonNull Throwable t) {
                if (!isAdded() || binding == null) return;
                Log.e("CATEGORY_API", "Error calling API Category:" + t.getMessage(), t);
                Toast.makeText(getContext(), "Network error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBanner() {
        bannerList.add(R.drawable.banner1);
        bannerList.add(R.drawable.banner2);
        bannerList.add(R.drawable.banner3);
        bannerList.add(R.drawable.banner4);
        bannerList.add(R.drawable.banner5);


        BannerAdapter bannerAdapter = new BannerAdapter(bannerList);
        binding.bannerViewPager.setAdapter(bannerAdapter);
    }

    private void setupBrands() {
        brandList.add(new Brand("Nike", R.drawable.nike));
        brandList.add(new Brand("Adidas", R.drawable.adidas));
        brandList.add(new Brand("Puma", R.drawable.puma));
        brandList.add(new Brand("Zara", R.drawable.zara));
        brandList.add(new Brand("Dior", R.drawable.dior));
        brandList.add(new Brand("Uniqlo", R.drawable.uniqlo));
        brandList.add(new Brand("Fog", R.drawable.fog));
        brandList.add(new Brand("Gap", R.drawable.gap));


        BrandAdapter brandAdapter = new BrandAdapter(getContext(), brandList);
        binding.rvBrands.setLayoutManager(new GridLayoutManager(getContext(), 4));
        binding.rvBrands.setAdapter(brandAdapter);
    }

    private void setupLooks() {
        lookList.add(new Look("Minimalist", "Tokyo", R.drawable.look1));
        lookList.add(new Look("Streetwear", "NYC", R.drawable.look2));
        lookList.add(new Look("Facion", "Japan", R.drawable.look3));
        lookList.add(new Look("Streetwear", "China", R.drawable.look4));
        lookList.add(new Look("Streetwear", "Korea", R.drawable.look5));


        LookAdapter lookAdapter = new LookAdapter(getContext(), lookList);
        binding.rvLooks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvLooks.setAdapter(lookAdapter);
    }

    private void setupProducts() {
        productAdapter = new ProductAdapter(getContext(), productList);
        binding.rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvProducts.setAdapter(productAdapter);

        ProductAPI productAPI = RetrofitClient.getClient(getContext()).create(ProductAPI.class);

        productAPI.getAllProducts().enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductListResponse> call, @NonNull Response<ProductListResponse> response) {
                if (!isAdded() || binding == null) return;

                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body().getData());
                    productAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Cannot get product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductListResponse> call, @NonNull Throwable t) {
                if (!isAdded() || binding == null) return;
                Toast.makeText(getContext(), "Network error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
