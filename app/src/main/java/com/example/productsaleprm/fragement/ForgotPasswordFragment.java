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

public class ForgotPasswordFragment extends Fragment {

    private EditText etForgotEmail;
    private Button btnSendReset;
    private TextView tvBackToLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViews(view);
        setupListeners();
    }

    private void initViews(View view) {
        etForgotEmail = view.findViewById(R.id.etForgotEmail);
        btnSendReset = view.findViewById(R.id.btnSendReset);
        tvBackToLogin = view.findViewById(R.id.tvBackToLogin);
    }

    private void setupListeners() {
        btnSendReset.setOnClickListener(v -> handleReset());
        tvBackToLogin.setOnClickListener(v ->
                ((MainAuthActivity) requireActivity()).loadFragment(new LoginFragment()));
    }

    private void handleReset() {
        String email = etForgotEmail.getText().toString().trim();

        if (!isValidEmail(email)) return;

        AuthApi authApi = RetrofitClient.getClientWithoutAuth().create(AuthApi.class);

        authApi.forgotPassword(email).enqueue(new Callback<GenericResponse>() {
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

    private boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            etForgotEmail.setError("Please enter email");
            return false;
        }
        return true;
    }

    private void handleErrorResponse(Response<?> response) {
        String message = "Failed to send reset email.";

        if (response.code() == 404) {
            message = "Email not found!";
        } else if (response.code() == 400) {
            message = "Wrong email !";
        } else {
            try {
                if (response.errorBody() != null) {
                    message = "Error: " + response.code() + " - " + response.errorBody().string();
                } else {
                    message = "Error: " + response.code();
                }
            } catch (Exception ignored) {}
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
