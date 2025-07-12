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
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar();
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new CartFragment())
                    .commit();
        }



    }

    private void ActionBar() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,binding.drawerLayout,binding.toolbarMain.toolbarContainer, R.string.nav_open,R.string.nav_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_account) {
                // Show a Toast message for the Account item
                Toast.makeText(MainActivity.this,
                        "Account Details", Toast.LENGTH_SHORT).show();
            }

            if (item.getItemId() == R.id.nav_settings) {
                // Show a Toast message for the Settings item
                Toast.makeText(MainActivity.this,
                        "Settings Opened", Toast.LENGTH_SHORT).show();
            }

            if (item.getItemId() == R.id.nav_logout) {
                // Show a Toast message for the Logout item
                Toast.makeText(MainActivity.this,
                        "You are Logged Out", Toast.LENGTH_SHORT).show();
            }

            // Close the drawer after selection
            binding.drawerLayout.closeDrawers();
            return false;
        });
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            // Called when the back button is pressed.
            @Override
            public void handleOnBackPressed() {
                // Check if the drawer is open
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
// Close the drawer if it's open
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // Finish the activity if the drawer is closed
                    finish();
                }
            }
        });
    }

}
