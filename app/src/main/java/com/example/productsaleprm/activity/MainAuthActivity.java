package com.example.productsaleprm.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.productsaleprm.R;
import com.example.productsaleprm.fragement.LoginFragment;

public class MainAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ Kiểm tra token đã lưu chưa
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        String token = prefs.getString("jwt_token", null);

        if (token != null && !token.isEmpty()) {
            // ✅ Đã đăng nhập → chuyển sang MainActivity
            Intent intent = new Intent(MainAuthActivity.this, MainActivity.class);
            intent.putExtra("token", token);
            startActivity(intent);
            finish(); // không cho quay lại trang login
        } else {
            // ❌ Chưa đăng nhập → load LoginFragment
            setContentView(R.layout.activity_main_auth);
            if (savedInstanceState == null) {
                loadFragment(new LoginFragment());
            }
        }
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
