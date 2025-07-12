package com.example.productsaleprm.fragement;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.productsaleprm.R;
import com.example.productsaleprm.adapter.WishListAdapter;
import com.example.productsaleprm.databinding.FragmentCartBinding;
import com.example.productsaleprm.databinding.FragmentWishlistBinding;
import com.example.productsaleprm.interfaceui.OnWishlistClick;
import com.example.productsaleprm.model.WishList;
import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.model.response.WishListResponse;
import com.example.productsaleprm.retrofit.RetrofitClient;
import com.example.productsaleprm.retrofit.WishListAPI;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WishlistFragment extends Fragment {
    private List<WishList> wishListList;
    private WishListAdapter wishListAdapter;
    private FragmentWishlistBinding binding;
    private int currentPage = 0;
    private int pageSize = 5;
    private boolean isLastPage = false;
    private boolean isLoading = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWishlistBinding.inflate(inflater, container, false);

        fetchWishlistData(0);
        binding.recyclerWishlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if(gridLayoutManager == null) return;
                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        fetchWishlistData(currentPage + 1);
                    }
                }
            }
        });
        checkEmptyWishlist();
        return binding.getRoot();
    }
    private void fetchWishlistData(int page) {
        isLoading = true;
        WishListAPI api = RetrofitClient.getClient(requireContext()
        ).create(WishListAPI.class);
        binding.progressBar.setVisibility(View.VISIBLE);
        api.getWishList(page,pageSize).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<BaseResponse<WishListResponse>> call, Response<BaseResponse<WishListResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    binding.progressBar.setVisibility(View.GONE);
                    isLoading = false;
                    WishListResponse wishListResponse = response.body().getData();
                    if (wishListResponse.getWishListItem() == null) {
                        if (page == 0) {
                            Toast.makeText(getContext(), "you don't have wishlist", Toast.LENGTH_SHORT).show();
                        }

                        return;
                    }
                    if (page == 0) {
                        wishListList = new ArrayList<>(wishListResponse.getWishListItem());
                        wishListAdapter = new WishListAdapter(requireContext(), wishListList);
                        wishListAdapter.setOnWishListChange((id, position, isCreate) -> {
                            WishListAPI api1 = RetrofitClient.getClient(requireContext()).create(WishListAPI.class);
                            api1.deleteItemOrCreateCart(id,isCreate).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(@NonNull Call<Void> call1, Response<Void> response1) {
                                    if(response.isSuccessful()){
                                        wishListList.remove(position);
                                        wishListAdapter.notifyItemRemoved(position);
                                        wishListAdapter.notifyItemRangeChanged(position,wishListList.size());
                                        checkEmptyWishlist();
                                        if (isCreate)
                                         Toast.makeText(getContext(), "Update to cart success", Toast.LENGTH_SHORT).show();
                                        else Toast.makeText(getContext(), "Delete Success", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(@NonNull Call<Void> call1, Throwable t) {

                                }
                            });

                        });
                        binding.recyclerWishlist.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        binding.recyclerWishlist.setAdapter(wishListAdapter);


                    } else {
                        int start = wishListList.size();
                        wishListList.addAll(wishListResponse.getWishListItem());
                        wishListAdapter.notifyItemRangeInserted(start, wishListResponse.getWishListItem().size());
                    }
                    currentPage = page;
                    isLastPage = wishListResponse.isLast();
                    checkEmptyWishlist();
                } else {
                    binding.progressBar.setVisibility(View.GONE);

                    Toast.makeText(getContext(), "Don't have data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<WishListResponse>> call, Throwable t) {

            }
        });

    }


    private void checkEmptyWishlist() {
        if (wishListAdapter != null && wishListAdapter.getItemCount() == 0) {
            binding.recyclerWishlist.setVisibility(View.GONE);
            binding.emptyLayout.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerWishlist.setVisibility(View.VISIBLE);
            binding.emptyLayout.setVisibility(View.GONE);
        }
    }

}