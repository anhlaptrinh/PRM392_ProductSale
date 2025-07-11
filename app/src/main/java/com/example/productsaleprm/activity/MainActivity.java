package com.example.productsaleprm.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.productsaleprm.R;
import com.example.productsaleprm.databinding.ActivityMainBinding;
import com.example.productsaleprm.fragement.CartFragment;
import com.example.productsaleprm.fragement.HomeFragment;
import com.example.productsaleprm.fragement.WishlistFragment;
import com.example.productsaleprm.fragement.ProfileFragment;
import com.example.productsaleprm.fragement.CategoryFragment;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupDrawer(); // Drawer logic

        // ✅ Load HomeFragment mặc định sau khi đăng nhập
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            binding.bottomNav.setSelectedItemId(R.id.nav_home); // đánh dấu tab Home
        }

        // ✅ Xử lý điều hướng bottom nav (dùng if-else thay switch-case)
        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (id == R.id.nav_category) {
                loadFragment(new CategoryFragment());
                return true;
            } else if (id == R.id.nav_cart) {
                loadFragment(new CartFragment());
                return true;
            } else if (id == R.id.nav_wishlist) {
                loadFragment(new WishlistFragment());
                return true;
            } else if (id == R.id.nav_profile) {
                loadFragment(new ProfileFragment());
                return true;
            }

            return false;
        });
    }

    private void loadFragment(androidx.fragment.app.Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.toolbarMain.toolbarContainer,
                R.string.nav_open,
                R.string.nav_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_account) {
                Toast.makeText(this, "Account Details", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.nav_settings) {
                Toast.makeText(this, "Settings Opened", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.nav_logout) {
                Toast.makeText(this, "You are Logged Out", Toast.LENGTH_SHORT).show();
            }

            binding.drawerLayout.closeDrawers();
            return true;
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            }
        });
    }
}
