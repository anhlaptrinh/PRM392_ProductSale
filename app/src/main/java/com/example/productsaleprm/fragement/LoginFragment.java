package com.example.productsaleprm.fragement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.productsaleprm.R;
import com.example.productsaleprm.activity.MainActivity;
import com.example.productsaleprm.activity.MainAuthActivity;
import com.example.productsaleprm.retrofit.AuthApi;
import com.example.productsaleprm.model.response.LoginResponse;
import com.example.productsaleprm.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private TextView tvForgotPassword;

    public LoginFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnRegister = view.findViewById(R.id.btnRegister);
        tvForgotPassword = view.findViewById(R.id.tvForgotPassword);

        btnLogin.setOnClickListener(v -> handleLogin());
        btnRegister.setOnClickListener(v -> navigateToRegister());
        tvForgotPassword.setOnClickListener(v -> navigateToForgotPassword());
    }

    private void handleLogin() {
        String email = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etUsername.setError("Vui lòng nhập email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }

        AuthApi authApi = RetrofitClient.getClient("ok").create(AuthApi.class);
        Call<LoginResponse> call = authApi.login(email, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getData();
                    Toast.makeText(getActivity(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    // ✅ Lưu token vào SharedPreferences
                    SharedPreferences prefs = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);
                    prefs.edit().putString("jwt_token", token).apply();

                    // ✅ Chuyển sang MainActivity
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("token", token);
                    startActivity(intent);
                    requireActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "Sai email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToRegister() {
        ((MainAuthActivity) requireActivity()).loadFragment(new RegisterFragment());
    }

    private void navigateToForgotPassword() {
        ((MainAuthActivity) requireActivity()).loadFragment(new ForgotPasswordFragment());
    }
}
