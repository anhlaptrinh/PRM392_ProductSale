package com.example.productsaleprm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import com.example.productsaleprm.R;
import com.example.productsaleprm.databinding.ActivityMainBinding;
import com.example.productsaleprm.fragement.CartFragment;
import com.example.productsaleprm.fragement.CategoryFragment;
import com.example.productsaleprm.fragement.HomeFragment;
import com.example.productsaleprm.fragement.ProfileFragment;
import com.example.productsaleprm.fragement.WishlistFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupActionBar();
        setupBottomNav();

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            binding.bottomNav.setSelectedItemId(R.id.nav_home);
        }
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            binding.bottomNav.setSelectedItemId(R.id.nav_home);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "cart_channel";
            CharSequence name = "Cart Notifications";
            String description = "Hiển thị thông báo số lượng sản phẩm trong giỏ";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setupActionBar() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.toolbarMain.toolbarContainer,
                R.string.nav_open,
                R.string.nav_close
        );
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.toolbarMain.toolbarChat.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
        });

        binding.navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_account) {
                Toast.makeText(this, "Account Details", Toast.LENGTH_SHORT).show();
            }else if (id == R.id.nav_order_history) {
                startActivity(new Intent(this, OrderHistoryActivity.class));
            } else if (id == R.id.nav_settings) {
                Toast.makeText(this, "Settings Opened", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_logout) {
                Toast.makeText(this, "You are Logged Out", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_product) {
                startActivity(new Intent(this, ProductActivity.class));
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

    private void setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment selectedFragment = null;

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_category) {
                selectedFragment = new CategoryFragment();
            } else if (id == R.id.nav_cart) {
                selectedFragment = new CartFragment();
            } else if (id == R.id.nav_wishlist) {
                selectedFragment = new WishlistFragment();
            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            return; // Tránh reload nếu đang ở đúng fragment
        }

        currentFragment = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
