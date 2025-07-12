package com.example.productsaleprm.fragement;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.productsaleprm.R;
import com.example.productsaleprm.activity.MainAuthActivity;
import com.example.productsaleprm.model.resquest.RegisterRequest;
import com.example.productsaleprm.model.response.RegisterResponse;
import com.example.productsaleprm.retrofit.AuthApi;
import com.example.productsaleprm.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterFragment extends Fragment {

    private EditText etEmail, etVerificationCode, etUsername, etPhone, etAddress, etPassword, etConfirmPassword;
    private Button btnRegister, btnSendOtp;
    private TextView tvBackToLogin;
    private CountDownTimer countDownTimer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        etEmail = view.findViewById(R.id.etEmail);
        etVerificationCode = view.findViewById(R.id.etVerificationCode);
        etUsername = view.findViewById(R.id.etUsername);
        etPhone = view.findViewById(R.id.etPhone);
        etAddress = view.findViewById(R.id.etAddress);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        btnRegister = view.findViewById(R.id.btnRegister);
        btnSendOtp = view.findViewById(R.id.btnSendOtp);
        tvBackToLogin = view.findViewById(R.id.tvBackToLogin);

        btnSendOtp.setOnClickListener(v -> sendVerificationCode());
        btnRegister.setOnClickListener(v -> handleRegister());
        tvBackToLogin.setOnClickListener(v -> ((MainAuthActivity) requireActivity()).loadFragment(new LoginFragment()));

        return view;
    }

    private void sendVerificationCode() {
        String email = etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Vui lòng nhập email");
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.getClient(requireContext()
                ).baseUrl()) // ⬅ lấy từ RetrofitClient
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AuthApi authApi = retrofit.create(AuthApi.class);
        authApi.sendVerificationCode(email).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đã gửi OTP đến email", Toast.LENGTH_SHORT).show();
                    startCountdown();
                } else {
                    Toast.makeText(getContext(), "Không thể gửi mã xác minh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi gửi OTP: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startCountdown() {
        btnSendOtp.setEnabled(false);

        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                btnSendOtp.setText("Gửi lại (" + millisUntilFinished / 1000 + "s)");
            }

            public void onFinish() {
                btnSendOtp.setEnabled(true);
                btnSendOtp.setText("Gửi lại mã");
            }
        }.start();
    }

    private void handleRegister() {
        String email = etEmail.getText().toString().trim();
        String otp = etVerificationCode.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Vui lòng nhập email");
            return;
        }
        if (TextUtils.isEmpty(otp)) {
            etVerificationCode.setError("Vui lòng nhập mã xác minh");
            return;
        }
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Vui lòng nhập tên đăng nhập");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Vui lòng nhập số điện thoại");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Vui lòng nhập địa chỉ");
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

        RegisterRequest request = new RegisterRequest(email, password, username, phone, address);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.getClient(requireContext()
                ).baseUrl()) // ⬅ lấy từ RetrofitClient
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AuthApi authApi = retrofit.create(AuthApi.class);
        authApi.register(otp, request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    ((MainAuthActivity) requireActivity()).loadFragment(new LoginFragment());
                } else {
                    String errorMsg = "Đăng ký thất bại";
                    try {
                        if (response.errorBody() != null) {
                            errorMsg = response.errorBody().string();
                        }
                    } catch (Exception ignored) {}
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi đăng ký: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
