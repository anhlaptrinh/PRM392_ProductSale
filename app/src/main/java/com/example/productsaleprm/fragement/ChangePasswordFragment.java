package com.example.productsaleprm.fragement;

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
import com.example.productsaleprm.activity.MainAuthActivity;
import com.example.productsaleprm.model.response.GenericResponse;
import com.example.productsaleprm.retrofit.AuthApi;
import com.example.productsaleprm.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {

    private EditText etEmail, etOldPassword, etNewPassword;
    private Button btnChangePassword;
    private TextView tvBackToLogin;

    private String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViews(view);
        setupListeners();

        if (getArguments() != null) {
            token = getArguments().getString("TOKEN", null);
            String email = getArguments().getString("EMAIL", null);
            if (email != null) {
                etEmail.setText(email);
                etEmail.setEnabled(false); // Khóa EditText nếu có email từ ForgotPassword
            }
        }
    }

    private void initViews(View view) {
        etEmail = view.findViewById(R.id.etEmail);
        etOldPassword = view.findViewById(R.id.etOldPassword);
        etNewPassword = view.findViewById(R.id.etNewPassword);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        tvBackToLogin = view.findViewById(R.id.tvBackToLogin);
    }

    private void setupListeners() {
        btnChangePassword.setOnClickListener(v -> handleChangePassword());

        tvBackToLogin.setOnClickListener(v ->
                ((MainAuthActivity) requireActivity()).loadFragment(new LoginFragment()));
    }

    private void handleChangePassword() {
        String email = etEmail.getText().toString().trim();
        String oldPass = etOldPassword.getText().toString().trim();
        String newPass = etNewPassword.getText().toString().trim();

        if (!isValidInput(email, oldPass, newPass)) return;

        AuthApi authApi = RetrofitClient.getClientWithoutAuth().create(AuthApi.class);

        String bearerToken = token != null ? "Bearer " + token : "";

        authApi.changePassword(bearerToken, email, oldPass, newPass).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    ((MainAuthActivity) requireActivity()).loadFragment(new LoginFragment());
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidInput(String email, String oldPass, String newPass) {
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Please enter email");
            return false;
        }
        if (TextUtils.isEmpty(oldPass)) {
            etOldPassword.setError("Please enter old password");
            return false;
        }
        if (TextUtils.isEmpty(newPass)) {
            etNewPassword.setError("Please enter new password");
            return false;
        }
        if (newPass.length() < 6) {
            etNewPassword.setError("New password must be at least 6 characters");
            return false;
        }
        return true;
    }

    private void handleErrorResponse(Response<?> response) {
        String message = "Failed to change password.";

        if (response.code() == 400) {
            message = "Invalid data!";
        } else if (response.code() == 404) {
            message = "Account not found!";
        } else if (response.code() == 401) {
            message = "Unauthorized. Please login again.";
        } else {
            try {
                if (response.errorBody() != null) {
                    message = "Error: " + response.code() + " - " + response.errorBody().string();
                }
            } catch (Exception ignored) {}
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
