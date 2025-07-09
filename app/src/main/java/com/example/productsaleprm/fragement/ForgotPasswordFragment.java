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

public class ForgotPasswordFragment extends Fragment {

    private EditText etForgotEmail;
    private Button btnSendReset;
    private TextView tvBackToLogin;

    public ForgotPasswordFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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

        // ✅ Hiển thị thông báo
        Toast.makeText(getContext(), "Đã gửi mã xác nhận đến email: " + email, Toast.LENGTH_SHORT).show();

        // ✅ Chuyển sang Fragment_Verification + truyền email
        Bundle bundle = new Bundle();
        bundle.putString("email", email);

        Fragment_Verification verificationFragment = new Fragment_Verification();
        verificationFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.auth_fragment_container, verificationFragment)
                .addToBackStack(null)
                .commit();
    }
}
