    package com.example.productsaleprm.activity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.widget.ImageView;
    import android.widget.Toast;

    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.productsaleprm.R;
    import com.example.productsaleprm.activity.OrderDetailActivity;
    import com.example.productsaleprm.adapter.OrderHistoryAdapter;
    import com.example.productsaleprm.model.Order;
    import com.example.productsaleprm.model.User;
    import com.example.productsaleprm.model.response.BaseResponse;
    import com.example.productsaleprm.retrofit.OrderApi;
    import com.example.productsaleprm.retrofit.RetrofitClient;
    import com.example.productsaleprm.retrofit.UserAPI;

    import java.util.ArrayList;
    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class OrderHistoryActivity extends AppCompatActivity {

        private RecyclerView rvOrders;
        private OrderHistoryAdapter adapter;
        private List<Order> orderList;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_order_history);

            // Back button
            ImageView btnBack = findViewById(R.id.btnBack);
            btnBack.setOnClickListener(v -> finish());

            rvOrders = findViewById(R.id.rvOrders);
            rvOrders.setLayoutManager(new LinearLayoutManager(this));

            orderList = new ArrayList<>();
            adapter = new OrderHistoryAdapter(this, orderList, order -> {
                Intent intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra("ORDER_ID", order.getId());
                startActivity(intent);
            });

            rvOrders.setAdapter(adapter);

            // Gọi API lấy user trước → khi có user mới gọi API lấy đơn hàng
            getCurrentUserAndLoadOrders();
        }

        private void getCurrentUserAndLoadOrders() {
            UserAPI userAPI = RetrofitClient.getClient(this).create(UserAPI.class);
            userAPI.getCurrentUser().enqueue(new Callback<BaseResponse<User>>() {
                @Override
                public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                        User user = response.body().getData();
                        fetchOrdersFromApi(user.getId());  // ✅ Gọi ở đây để đảm bảo user đã sẵn sàng
                    } else {
                        Toast.makeText(OrderHistoryActivity.this, "Không tìm thấy User!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                    Toast.makeText(OrderHistoryActivity.this, "Lỗi khi tải User!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void fetchOrdersFromApi(int userId) {
            OrderApi api = RetrofitClient.getClient(this).create(OrderApi.class);
            Call<List<Order>> call = api.getOrders(userId);
            call.enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        orderList.clear();
                        orderList.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(OrderHistoryActivity.this, "Không thể tìm thấy Orders", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    Toast.makeText(OrderHistoryActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
