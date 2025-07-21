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

public class RegisterFragment extends Fragment {

    private EditText etEmail, etVerificationCode, etUsername, etPhone, etAddress, etPassword, etConfirmPassword;
    private Button btnRegister, btnSendOtp;
    private TextView tvBackToLogin;
    private CountDownTimer countDownTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initViews(view);
        setupListeners();
        return view;
    }

    private void initViews(View view) {
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
    }

    private void setupListeners() {
        btnSendOtp.setOnClickListener(v -> sendVerificationCode());
        btnRegister.setOnClickListener(v -> handleRegister());
        tvBackToLogin.setOnClickListener(v ->
                ((MainAuthActivity) requireActivity()).loadFragment(new LoginFragment()));
    }

    private void sendVerificationCode() {
        String email = etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Please enter email");
            return;
        }

        AuthApi authApi = RetrofitClient.getClientWithoutAuth().create(AuthApi.class);
        authApi.sendVerificationCode(email).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "OTP sent to email", Toast.LENGTH_SHORT).show();
                    startCountdown();
                } else {
                    Toast.makeText(getContext(), "Unable to send verification code", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error sending OTP: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startCountdown() {
        btnSendOtp.setEnabled(false);
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                btnSendOtp.setText("Resend (" + millisUntilFinished / 1000 + "s)");
            }

            public void onFinish() {
                btnSendOtp.setEnabled(true);
                btnSendOtp.setText("Resend code");
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

        if (!isValidInput(email, otp, username, phone, address, password, confirmPassword)) return;

        RegisterRequest request = new RegisterRequest(email, password, username, phone, address);

        AuthApi authApi = RetrofitClient.getClientWithoutAuth().create(AuthApi.class);
        authApi.register(otp, request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    ((MainAuthActivity) requireActivity()).loadFragment(new LoginFragment());
                } else {
                    showErrorFromResponse(response);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Registration error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Validate tất cả các trường và hiển thị lỗi đồng thời
    private boolean isValidInput(String email, String otp, String username, String phone, String address, String password, String confirmPassword) {
        boolean isValid = true;

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Please enter email");
            isValid = false;
        }

        if (TextUtils.isEmpty(otp)) {
            etVerificationCode.setError("Please enter verification code");
            isValid = false;
        }

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Please enter username");
            isValid = false;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Please enter phone number");
            isValid = false;
        }

        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Please enter address");
            isValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please enter password");
            isValid = false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Please confirm password");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            isValid = false;
        }

        return isValid;
    }

    private void showErrorFromResponse(Response<?> response) {
        String errorMsg = "Registration failed!";
        try {
            if (response.errorBody() != null) {
                errorMsg = response.errorBody().string();
            }
        } catch (Exception ignored) {}
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
