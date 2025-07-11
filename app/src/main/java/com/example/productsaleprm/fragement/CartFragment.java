package com.example.productsaleprm.fragement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.productsaleprm.adapter.CartAdapter;
import com.example.productsaleprm.databinding.FragmentCartBinding;
import com.example.productsaleprm.model.CartItem;
import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.model.response.CartResponseData;
import com.example.productsaleprm.model.response.CartUpdateResponse;
import com.example.productsaleprm.retrofit.CartAPI;
import com.example.productsaleprm.retrofit.RetrofitClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private List<CartItem> cartList;

    private CartAdapter cartAdapter;
    private FragmentCartBinding binding;

    private int currentPage = 0;
    private final int pageSize = 5;
    private boolean isLastPage = false;
    private boolean isLoading = false;

    private final String token = getTokenFromPrefs();



    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCartBinding.inflate(inflater, container, false);
        fetchCartData();

        //cartAdapter.setOnCartChangedListener(this::checkEmptyCart);
        binding.btnClearCart.setOnClickListener(v -> {
            CartAPI api = RetrofitClient.getClient(getTokenFromPrefs()).create(CartAPI.class);
            api.clearCart().enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        if (cartList == null) {
                            cartList = new ArrayList<>();
                        } else {
                            cartList.clear();
                        }

                        cartAdapter.notifyDataSetChanged();
                        updateTotalAmount();
                        checkEmptyCart();
                        Toast.makeText(getContext(), "Cart cleared", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Error clearing cart", Toast.LENGTH_SHORT).show();
                }
            });
        });
        checkEmptyCart();

        // Example: set text
        binding.tvOrderTitle.setText("Your Order");

        return binding.getRoot();
    }

    //lấy API cartItem
    private void fetchCartData(){
        CartAPI api = RetrofitClient.getClient(getTokenFromPrefs()).create(CartAPI.class);
        binding.progressBar.setVisibility(View.VISIBLE);

        api.getCartItems().enqueue(new Callback<BaseResponse<CartResponseData>>() {

            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(Call<BaseResponse<CartResponseData>> call,
                                   Response<BaseResponse<CartResponseData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    binding.progressBar.setVisibility(View.GONE);
                    CartResponseData cartData = response.body().getData();

                    if (cartData == null) {
                        Toast.makeText(getContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<CartItem> items = cartData.getCartItems();

                    // Nếu list null thì dùng list rỗng để tránh lỗi
                    if (items == null) {
                        items = new ArrayList<>();
                    }

                    // Gán vào cartList
                    cartList = new ArrayList<>(items);

                    // Khởi tạo adapter
                    cartAdapter = new CartAdapter(requireContext(), cartList);
                    cartAdapter.setOnCartChangedListener(CartFragment.this::checkEmptyCart);

                    //delete item
                    cartAdapter.setOnCartActionListener(new CartAdapter.OnCartActionListener() {
                        @Override
                        public void deleteCartItem(CartItem item, int position) {
                            deleteCartItemFromServer(item.getId(), position);
                        }

                        @Override
                        public void updateCartItem(CartItem item, int position, boolean isIncrease) {
                            int currentQty = item.getQuantity();
                            int newQty = isIncrease ? currentQty + 1 : currentQty - 1;

                            if (newQty <= 0) {
                                Toast.makeText(getContext(), "Số lượng không được nhỏ hơn 1", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            item.setQuantity(newQty);
                            item.setPrice(item.getPrice().multiply(BigDecimal.valueOf(newQty))); // bạn cần thêm unitPrice nếu chưa có
                            cartAdapter.notifyItemChanged(position);
                            updateTotalAmount();
                            updateQuantityFromServer(item.getId(), position, newQty);
                        }
                    });

                    binding.recyclerCart.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerCart.setAdapter(cartAdapter);

                    // Tổng tiền
                    BigDecimal total = cartData.getTotalAmount();
                    if (total != null) {
                        binding.tvTotalAmount.setText("$" + total);
                    } else {
                        binding.tvTotalAmount.setText("$0");
                    }

                    checkEmptyCart();
                } else {
                    Toast.makeText(getContext(), "Không thể lấy giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<CartResponseData>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "fetchCartData failed", t);
            }
        });
    }

    //Delete
    private void deleteCartItemFromServer(int id, int position) {
        CartAPI api = RetrofitClient.getClient(getTokenFromPrefs()).create(CartAPI.class);

        api.deleteCartItem(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    cartList.remove(position);
                    cartAdapter.notifyItemRemoved(position);
                    cartAdapter.notifyItemRangeChanged(position, cartList.size());

                    checkEmptyCart();
                    updateTotalAmount();
                    Toast.makeText(getContext(), "Xoá thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Xoá thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi xoá: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Update
    private void updateQuantityFromServer(int itemId, int position, int newQuantity) {
        CartAPI api = RetrofitClient.getClient(getTokenFromPrefs()).create(CartAPI.class);

        api.updateQuantity(itemId, newQuantity)  // 👈 quantity truyền bằng @Query
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<BaseResponse<CartUpdateResponse>> call,
                                           Response<BaseResponse<CartUpdateResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            CartUpdateResponse data = response.body().getData();

                            if (data != null) {
                                CartItem item = cartList.get(position);

                                // Cập nhật dữ liệu mới từ server
                                item.setQuantity(data.getQuantity());
                                item.setPrice(data.getPrice());

                                cartAdapter.notifyItemChanged(position);

                                // Cập nhật tổng tiền
                                binding.tvTotalAmount.setText("$" + data.getCartTotal());
                            }
                        } else {
                            Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<CartUpdateResponse>> call, Throwable t) {
                        Toast.makeText(getContext(), "Lỗi cập nhật: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkEmptyCart() {
        if (cartAdapter != null && cartAdapter.getItemCount() == 0) {
            binding.recyclerCart.setVisibility(View.GONE);
            binding.emptyLayout.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerCart.setVisibility(View.VISIBLE);
            binding.emptyLayout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cartList) {
            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        binding.tvTotalAmount.setText("$" + total);
    }

    // ✅ Lấy token từ SharedPreferences
    private String getTokenFromPrefs() {
        SharedPreferences prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        return prefs.getString("jwt_token", null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // tránh memory leak
    }
}
