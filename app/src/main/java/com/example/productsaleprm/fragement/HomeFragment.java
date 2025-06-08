package com.example.productsaleprm.fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.productsaleprm.R;
import com.example.productsaleprm.adapter.ProductAdapter;
import com.example.productsaleprm.databinding.FragmentHomeBinding;
import com.example.productsaleprm.model.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupFlipper();


        return root;
    }

    private void setupFlipper() {
        // Ví dụ adapter ViewPager2 cho flipper
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Áo thun", "https://png.pngtree.com/png-clipart/20201223/ourmid/pngtree-clothing-sweater-clothes-clothing-mens-clothing-spring-clothes-foreign-trade-tailor-png-image_2611494.jpg"));
        productList.add(new Product("Quần jean", "https://purepng.com/public/uploads/large/purepng.com-mens-jeansgarmentlower-bodydenimjeans-1421526362615sqofu.png"));
        productList.add(new Product("Giày thể thao", "https://png.pngtree.com/png-vector/20220716/ourmid/pngtree-clock-icon-vector-png-png-image_5966528.png"));
        productList.add(new Product("Giày thể thao", "https://png.pngtree.com/png-vector/20220716/ourmid/pngtree-clock-icon-vector-png-png-image_5966528.png"));

        // Adapter
        ProductAdapter adapter = new ProductAdapter(productList);

        // Gán adapter cho ViewPager2 trong flipper
        binding.viewFlipperContainer.viewpagerProducts.setAdapter(adapter);
        binding.viewFlipperContainer.viewpagerProducts.setOffscreenPageLimit(3);
        CompositePageTransformer transformer = new CompositePageTransformer();
        binding.viewFlipperContainer.viewpagerProducts.setClipToPadding(false);
        binding.viewFlipperContainer.viewpagerProducts.setClipChildren(false);

        binding.viewFlipperContainer.viewpagerProducts.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        transformer.addTransformer(new MarginPageTransformer(20));
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.9f + r * 0.1f);
        });
        binding.viewFlipperContainer.viewpagerProducts.setPageTransformer(transformer);
    }
}
