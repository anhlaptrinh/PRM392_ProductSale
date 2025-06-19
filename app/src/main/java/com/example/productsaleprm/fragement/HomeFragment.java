package com.example.productsaleprm.fragement;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.productsaleprm.activity.CarouselActivity;
import com.example.productsaleprm.adapter.CarouselAdapter;
import com.example.productsaleprm.adapter.CategoriesAdapter;
import com.example.productsaleprm.databinding.FragmentHomeBinding;
import com.example.productsaleprm.interfaceui.OnItemCarouselClick;
import com.example.productsaleprm.model.Categories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupFlipper();
        setupCarousel();


        return root;
    }

    private void setupCarousel() {
        RecyclerView recyclerView = binding.carouselRecycle;
        ArrayList<String> imageUrls = new ArrayList<>(Arrays.asList(
                "https://plus.unsplash.com/premium_photo-1673758905770-a62f4309c43c?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8bW9kZWx8ZW58MHx8MHx8fDA%3D",
                "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8bW9kZWx8ZW58MHx8MHx8fDA%3D",
                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8bW9kZWx8ZW58MHx8MHx8fDA%3D",
                "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8bW9kZWx8ZW58MHx8MHx8fDA%3D",
                "https://images.unsplash.com/photo-1529139574466-a303027c1d8b?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8bW9kZWx8ZW58MHx8MHx8fDA%3D"
        ));
        CarouselAdapter adapter = new CarouselAdapter(requireContext(),imageUrls);
        adapter.setOnItemCarouselClick((imageView, url) -> {
            Intent intent = new Intent(requireContext(), CarouselActivity.class);
            intent.putExtra("image", url);

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    requireActivity(), imageView, "image");

            startActivity(intent, options.toBundle());
        });
        recyclerView.setAdapter(adapter);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupFlipper() {
        // Ví dụ adapter ViewPager2 cho flipper
        List<Categories> categoriesList = new ArrayList<>();
        categoriesList.add(new Categories("Áo thun", "https://purepng.com/public/uploads/large/purepng.com-mens-jeansgarmentlower-bodydenimjeans-1421526362615sqofu.png"));
        categoriesList.add(new Categories("Quần jean", "https://purepng.com/public/uploads/large/purepng.com-mens-jeansgarmentlower-bodydenimjeans-1421526362615sqofu.png"));
        categoriesList.add(new Categories("Giày thể thao", "https://png.pngtree.com/png-vector/20220716/ourmid/pngtree-clock-icon-vector-png-png-image_5966528.png"));
        categoriesList.add(new Categories("Khăn thể thao", "https://png.pngtree.com/png-vector/20220716/ourmid/pngtree-clock-icon-vector-png-png-image_5966528.png"));
        categoriesList.add(new Categories("Lắc thể thao", "https://png.pngtree.com/png-vector/20220716/ourmid/pngtree-clock-icon-vector-png-png-image_5966528.png"));
        categoriesList.add(new Categories("Lắc thể thao", "https://png.pngtree.com/png-vector/20220716/ourmid/pngtree-clock-icon-vector-png-png-image_5966528.png"));

        // Adapter
        CategoriesAdapter adapter = new CategoriesAdapter(categoriesList);

        // Gán adapter cho ViewPager2 trong flipper
        binding.viewFlipperContainer.viewpagerProducts.setAdapter(adapter);
        binding.viewFlipperContainer.viewpagerProducts.setOffscreenPageLimit(4);
        CompositePageTransformer transformer = new CompositePageTransformer();

        transformer.addTransformer(new MarginPageTransformer(8));

        binding.viewFlipperContainer.viewpagerProducts.setPageTransformer(transformer);
        binding.viewFlipperContainer.viewpagerProducts.setClipToPadding(false);
        binding.viewFlipperContainer.viewpagerProducts.setClipChildren(false);
        final int lastIndex = adapter.getItemCount() - 2;
        final float[] downX = new float[1];
        binding.viewFlipperContainer.viewpagerProducts.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        RecyclerView internalRv = (RecyclerView) binding.viewFlipperContainer.viewpagerProducts.getChildAt(0);
        internalRv.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX[0] = event.getX();
                    return false; // vẫn cho ViewPager2 xử lý tiếp
                case MotionEvent.ACTION_MOVE:
                    float diffX = event.getX() - downX[0];
                    boolean isSwipeLeft = diffX < 0;          // vuốt sang trái = tiến
                    boolean atLastPage = binding.viewFlipperContainer.viewpagerProducts.getCurrentItem() == lastIndex;

                    if (atLastPage && isSwipeLeft) {
                        // Đang ở cuối và cố gắng vuốt tiến → chặn
                        return true;
                    }
                    return false; // các trường hợp còn lại vẫn xử lý bình thường
            }
            return false;
        });

        // 7. Khi lùi ra khỏi trang cuối, reset lại enable swipe (phòng trường hợp setUserInputEnabled)
        binding.viewFlipperContainer.viewpagerProducts.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position < lastIndex) {
                    binding.viewFlipperContainer.viewpagerProducts.setUserInputEnabled(true);
                }
            }
        });
    }
}
