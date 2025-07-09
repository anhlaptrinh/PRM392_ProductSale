package com.example.productsaleprm.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.productsaleprm.R;
import com.example.productsaleprm.fragement.LoginFragment;

public class MainAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_auth); // tạo file này trong res/layout

        // Load LoginFragment mặc định khi mở app
        if (savedInstanceState == null) {
            loadFragment(new LoginFragment());
        }
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
