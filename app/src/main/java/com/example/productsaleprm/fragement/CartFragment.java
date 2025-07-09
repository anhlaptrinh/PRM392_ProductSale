package com.example.productsaleprm.fragement;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productsaleprm.R;
import com.example.productsaleprm.adapter.CartAdapter;
import com.example.productsaleprm.databinding.FragmentCartBinding;
import com.example.productsaleprm.model.CartItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private List<CartItem> cartList;

    private CartAdapter cartAdapter;
    private FragmentCartBinding binding;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCartBinding.inflate(inflater, container, false);
        getCartData();
        cartAdapter.setOnCartChangedListener(this::checkEmptyCart);
        checkEmptyCart();

        // Example: set text
        binding.tvOrderTitle.setText("Your Order");


        return binding.getRoot();
    }

    //lấy API cartItem
    private void getCartData(){
        cartList = new ArrayList<>();
        CartItem sampleItem = new CartItem("Apple iPhone 14","@drawable/clock.png",3,BigDecimal.valueOf(900));
        CartItem sampleItem2 = new CartItem("Apple iPhone 15","@drawable/clock.png",3,BigDecimal.valueOf(900));
        cartList.add(sampleItem);
        cartList.add(sampleItem2);

        // Adapter setup
        cartAdapter = new CartAdapter(requireContext(), cartList);
        binding.recyclerCart.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerCart.setAdapter(cartAdapter);
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




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // tránh memory leak
    }
}
