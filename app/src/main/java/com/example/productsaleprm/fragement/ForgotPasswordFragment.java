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

public class ForgotPasswordFragment extends Fragment {

    private EditText etForgotEmail;
    private Button btnSendReset;
    private TextView tvBackToLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        etForgotEmail = view.findViewById(R.id.etForgotEmail);
        btnSendReset = view.findViewById(R.id.btnSendReset);
        tvBackToLogin = view.findViewById(R.id.tvBackToLogin);

        btnSendReset.setOnClickListener(v -> handleReset());
        tvBackToLogin.setOnClickListener(v -> ((MainAuthActivity) requireActivity()).loadFragment(new LoginFragment()));

        return view;
    }

    private void handleReset() {
        String email = etForgotEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etForgotEmail.setError("Vui lòng nhập email");
            return;
        }

        Toast.makeText(getContext(), "Đã gửi liên kết khôi phục về email", Toast.LENGTH_LONG).show();
        ((MainAuthActivity) requireActivity()).loadFragment(new LoginFragment());
    }
}
