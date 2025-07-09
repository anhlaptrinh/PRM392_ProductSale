package com.example.productsaleprm.fragement;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;
import com.example.productsaleprm.R;
import com.example.productsaleprm.activity.MainAuthActivity;


public class RegisterFragment extends Fragment {

    private EditText etUsername, etPhone, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvBackToLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        etUsername = view.findViewById(R.id.etUsername);
        etPhone = view.findViewById(R.id.etPhone);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        btnRegister = view.findViewById(R.id.btnRegister);
        tvBackToLogin = view.findViewById(R.id.tvBackToLogin);

        btnRegister.setOnClickListener(v -> handleRegister());
        tvBackToLogin.setOnClickListener(v -> ((MainAuthActivity) requireActivity()).loadFragment(new LoginFragment()));

        return view;
    }

    private void handleRegister() {
        String username = etUsername.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Vui lòng nhập tên đăng nhập");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Vui lòng nhập số điện thoại");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu không khớp");
            return;
        }

        Toast.makeText(getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
        ((MainAuthActivity) requireActivity()).loadFragment(new LoginFragment());
    }
}