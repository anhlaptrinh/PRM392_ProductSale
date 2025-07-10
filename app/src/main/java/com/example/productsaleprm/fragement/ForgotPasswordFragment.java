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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etForgotEmail = view.findViewById(R.id.etForgotEmail);
        btnSendReset = view.findViewById(R.id.btnSendReset);
        tvBackToLogin = view.findViewById(R.id.tvBackToLogin);

        btnSendReset.setOnClickListener(v -> handleReset());
        tvBackToLogin.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void handleReset() {
        String email = etForgotEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etForgotEmail.setError("Vui lòng nhập email");
            return;
        }

        AuthApi authApi = RetrofitClient.getAuthApi();
        authApi.forgotPassword(email).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    // 👉 Chuyển sang LoginFragment sau khi gửi thành công
                    ((MainAuthActivity) requireActivity()).loadFragment(new LoginFragment());
                } else {
                    Toast.makeText(getContext(), "Không thể gửi email. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
