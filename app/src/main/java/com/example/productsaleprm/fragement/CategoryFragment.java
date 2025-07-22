package com.example.productsaleprm.fragement;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.productsaleprm.R;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ProgressBar;
import com.example.productsaleprm.model.Category;
import com.example.productsaleprm.model.response.CategoryListResponse;
import com.example.productsaleprm.retrofit.CategoryAPI;
import com.example.productsaleprm.retrofit.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import com.example.productsaleprm.adapter.CategoryAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment {

    private RecyclerView rvCategories;
    private ProgressBar progressBar;

    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    public CategoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        rvCategories = view.findViewById(R.id.rvCategories);
        progressBar = view.findViewById(R.id.progressBar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupCategoryRecyclerView();
        fetchCategoriesFromApi();
    }

    private void setupCategoryRecyclerView() {
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        rvCategories.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 💡 2 cột
        rvCategories.setAdapter(categoryAdapter);
    }

    private void fetchCategoriesFromApi() {
        showLoading();
        CategoryAPI categoryAPI = RetrofitClient.getClient(getContext()).create(CategoryAPI.class);
        categoryAPI.getAllCategories().enqueue(new Callback<CategoryListResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryListResponse> call, @NonNull Response<CategoryListResponse> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    categoryList.clear();
                    categoryList.addAll(response.body().getData());
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryListResponse> call, @NonNull Throwable t) {
                hideLoading();
                Toast.makeText(getContext(), "API Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }
}