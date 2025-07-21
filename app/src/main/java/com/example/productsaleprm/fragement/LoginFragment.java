package com.example.productsaleprm.fragement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.example.productsaleprm.model.response.LoginResponse;
import com.example.productsaleprm.retrofit.AuthApi;
import com.example.productsaleprm.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private TextView tvForgotPassword;
    private ProgressBar progressBar;

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
        initViews(view);
        setupListeners();
    }

    private void initViews(View view) {
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnRegister = view.findViewById(R.id.btnRegister);
        tvForgotPassword = view.findViewById(R.id.tvForgotPassword);
        progressBar = view.findViewById(R.id.progressBarLogin);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());
        btnRegister.setOnClickListener(v -> navigateToRegister());
        tvForgotPassword.setOnClickListener(v -> navigateToForgotPassword());
    }

    private void handleLogin() {
        String email = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!isValidInput(email, password)) return;

        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        AuthApi authApi = RetrofitClient.getClientWithoutAuth().create(AuthApi.class);

        authApi.login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getData();
                    Log.d("JWT_TOKEN", "Token received: " + token);
                    saveToken(token);
                    Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                    goToMainActivity();
                } else {
                    if (response.code() == 401) {
                        Toast.makeText(getContext(), "Incorrect email or password!", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 400) {
                        Toast.makeText(getContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                    } else {
                        showErrorFromResponse(response);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);
                Toast.makeText(getContext(), "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Bắt lỗi đồng thời cho tất cả các trường
    private boolean isValidInput(String email, String password) {
        boolean isValid = true;

        if (TextUtils.isEmpty(email)) {
            etUsername.setError("Please enter email");
            isValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please enter password");
            isValid = false;
        }

        return isValid;
    }

    private void saveToken(String token) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);
        prefs.edit().putString("jwt_token", token).apply();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void showErrorFromResponse(Response<LoginResponse> response) {
        String message = "Unknown error!";
        try {
            if (response.errorBody() != null) {
                message = "Error: " + response.code() + " - " + response.errorBody().string();
            } else {
                message = "Error: " + response.code();
            }
        } catch (Exception ignored) {}

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void navigateToRegister() {
        ((MainAuthActivity) requireActivity()).loadFragment(new RegisterFragment());
    }

    private void navigateToForgotPassword() {
        ((MainAuthActivity) requireActivity()).loadFragment(new ForgotPasswordFragment());
    }
}
