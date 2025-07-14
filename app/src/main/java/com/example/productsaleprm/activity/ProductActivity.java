package com.example.productsaleprm.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Spinner;

import com.example.productsaleprm.R;
import com.example.productsaleprm.adapter.ProductAdapter;
import com.example.productsaleprm.model.Product;
import com.example.productsaleprm.model.response.ProductListResponse;
import com.example.productsaleprm.retrofit.ProductAPI;
import com.example.productsaleprm.retrofit.RetrofitClient;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    private Spinner spinnerSort;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private ProductAPI productAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product); // file bạn đã tạo với ConstraintLayout

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);
        spinnerSort = findViewById(R.id.spinnerSort);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Default", "Ascending", "Descending"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String sort = null;
                switch (position) {
                    case 1:
                        sort = "price_asc";
                        break;
                    case 2:
                        sort = "price_desc";
                        break;
                    default:
                        sort = null;
                }
                fetchProductsFromAPI(sort);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fetchProductsFromAPI(null);
            }
        });

        productAPI = RetrofitClient.getClient(getApplicationContext()).create(ProductAPI.class);

        // Load sản phẩm mặc định khi mở activity
        fetchProductsFromAPI(null);
    }

    private void fetchProductsFromAPI(String sort) {
        Call<ProductListResponse> call;
        if (sort == null || sort.isEmpty()) {
            call = productAPI.getAllProducts();
        } else {
            call = productAPI.filterProducts(null, null, null, sort);
        }

        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body().getData();
                    productList.clear();
                    productList.addAll(products);
                    productAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API", "Response unsuccessful or empty body");
                }
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                Log.e("API", "Call failed: " + t.getMessage());
            }
        });
    }
}