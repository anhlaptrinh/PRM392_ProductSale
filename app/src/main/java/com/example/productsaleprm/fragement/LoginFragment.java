package com.example.productsaleprm.fragement;

import android.content.Intent;
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
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Vui lòng nhập tên đăng nhập");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }

        if (username.equals("admin") && password.equals("123456")) {
            Toast.makeText(getActivity(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));
            requireActivity().finish(); // kết thúc MainAuthActivity
        } else {
            Toast.makeText(getActivity(), "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToRegister() {
        ((MainAuthActivity) requireActivity()).loadFragment(new RegisterFragment());
    }

    private void navigateToForgotPassword() {
        ((MainAuthActivity) requireActivity()).loadFragment(new ForgotPasswordFragment());
    }
}
