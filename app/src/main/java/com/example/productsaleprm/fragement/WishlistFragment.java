package com.example.productsaleprm.fragement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.productsaleprm.R;
import com.example.productsaleprm.adapter.WishListAdapter;
import com.example.productsaleprm.databinding.FragmentCartBinding;
import com.example.productsaleprm.databinding.FragmentWishlistBinding;
import com.example.productsaleprm.model.WishList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class WishlistFragment extends Fragment {
    private FragmentWishlistBinding binding;
    private RecyclerView rcvWishlist;
    private WishListAdapter adapter;
    private List<WishList> mockList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWishlistBinding.inflate(inflater, container, false);
        rcvWishlist = binding.recyclerWishlist;
        rcvWishlist.setLayoutManager(new GridLayoutManager(getContext(), 2));
        fetchWishlistData();
        return binding.getRoot();
    }
    private void fetchWishlistData() {
        mockList = new ArrayList<>();
        mockList.add(new WishList(1, 1, "Áo nỉ", "de chiu thoang mat","image1", BigDecimal.valueOf(55.55)));
        mockList.add(new WishList(2, 2, "Quần", "de chiu thoang mat","image1", BigDecimal.valueOf(55.55)));


        adapter = new WishListAdapter(getContext(), mockList);
        rcvWishlist.setAdapter(adapter);
    }

}