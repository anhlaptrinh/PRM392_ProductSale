package com.example.productsaleprm.activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.example.productsaleprm.retrofit.CategoryAPI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Spinner;
import com.example.productsaleprm.model.Category;
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
import android.view.View;
import com.example.productsaleprm.model.response.CategoryListResponse;
import android.widget.Toast;
import com.example.productsaleprm.retrofit.WishListAPI;

public class ProductActivity extends AppCompatActivity {
    private Spinner spinnerSort, spinnerCategory;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    private ProductAPI productAPI;
    private CategoryAPI categoryAPI;
    private ArrayAdapter<String> categoryAdapter;
    private Integer selectedCategoryId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);

        productAdapter.setOnProductWishlistClick((product, position) -> {
                    if (!product.isFavorite()) {
                        // Chưa yêu thích thì thêm vào wishlist
                        WishListAPI api = RetrofitClient.getClient(ProductActivity.this).create(WishListAPI.class);
                        api.addToWishlist(product.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    product.setFavorite(true);
                                    productAdapter.notifyItemChanged(position);
                                    Toast.makeText(ProductActivity.this, "Added to wishlist", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ProductActivity.this, "Failed to add to wishlist", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(ProductActivity.this, "API error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Đã yêu thích rồi thì xóa khỏi wishlist
                        WishListAPI api = RetrofitClient.getClient(ProductActivity.this).create(WishListAPI.class);
                        api.deleteItemOrCreateCart(product.getWishlistId(), false).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    product.setFavorite(false); // sửa chỗ này
                                    productAdapter.notifyItemChanged(position);
                                    Toast.makeText(ProductActivity.this, "Removed from wishlist", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ProductActivity.this, "Failed to remove from wishlist", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(ProductActivity.this, "API error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
        });

        productAdapter.setOnProductItemClickListener(product -> {
            Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", product.getId());
            startActivity(intent);
        });


        spinnerSort = findViewById(R.id.spinnerSort);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Default", "Ascending", "Descending"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                fetchProductsFromAPI(getCurrentSortValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fetchProductsFromAPI(null);
            }
        });

        productAPI = RetrofitClient.getClient(getApplicationContext()).create(ProductAPI.class);
        categoryAPI = RetrofitClient.getClient(getApplicationContext()).create(CategoryAPI.class);

        // Load categories và sản phẩm mặc định
        loadCategoriesFromAPI();
        fetchProductsFromAPI(null);
    }

    private void loadCategoriesFromAPI() {
        categoryAPI.getAllCategories().enqueue(new Callback<CategoryListResponse>() {
            @Override
            public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList = response.body().getData();

                    List<String> categoryNames = new ArrayList<>();
                    categoryNames.add("All"); // ID null
                    for (Category category : categoryList) {
                        categoryNames.add(category.getCategoryName());
                    }

                    categoryAdapter = new ArrayAdapter<>(ProductActivity.this,
                            android.R.layout.simple_spinner_item, categoryNames);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(categoryAdapter);

                    spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                selectedCategoryId = null;
                            } else {
                                selectedCategoryId = categoryList.get(position - 1).getCategoryID();
                            }
                            fetchProductsFromAPI(getCurrentSortValue());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            selectedCategoryId = null;
                            fetchProductsFromAPI(getCurrentSortValue());
                        }
                    });
                } else {
                    Log.e("API", "Failed to load categories");
                }
            }

            @Override
            public void onFailure(Call<CategoryListResponse> call, Throwable t) {
                Log.e("API", "Category API failed: " + t.getMessage());
            }
        });
    }

    private String getCurrentSortValue() {
        int pos = spinnerSort.getSelectedItemPosition();
        switch (pos) {
            case 1:
                return "price_asc";
            case 2:
                return "price_desc";
            default:
                return null;
        }
    }


    private void fetchProductsFromAPI(String sort) {
        Call<ProductListResponse> call = productAPI.filterProducts(selectedCategoryId, null, null, sort);

        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body().getData();
                    productList.clear();
                    productList.addAll(products);
                    productAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API", "Product response unsuccessful or empty");
                }
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                Log.e("API", "Product API failed: " + t.getMessage());
            }
        });
    }
}