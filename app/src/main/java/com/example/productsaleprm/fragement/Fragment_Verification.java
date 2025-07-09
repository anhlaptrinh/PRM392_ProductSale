package com.example.productsaleprm.fragement;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.productsaleprm.R;

public class Fragment_Verification extends Fragment {

    private EditText otp1, otp2, otp3, otp4, otp5, otp6;
    private Button btnSubmitOtp;
    private TextView tvResendOtp;
    private String email;

    public Fragment_Verification() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            email = getArguments().getString("email", "");
        }

        otp1 = view.findViewById(R.id.otp1);
        otp2 = view.findViewById(R.id.otp2);
        otp3 = view.findViewById(R.id.otp3);
        otp4 = view.findViewById(R.id.otp4);
        otp5 = view.findViewById(R.id.otp5);
        otp6 = view.findViewById(R.id.otp6);
        btnSubmitOtp = view.findViewById(R.id.btnSubmitOtp);
        tvResendOtp = view.findViewById(R.id.tvResendOtp);

        setupOtpInputs();

        btnSubmitOtp.setOnClickListener(v -> handleOtpSubmit());

        tvResendOtp.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đã gửi lại mã OTP đến: " + email, Toast.LENGTH_SHORT).show();
            // TODO: Gửi OTP thật ở đây nếu có backend
        });
    }

    private void handleOtpSubmit() {
        String otp = otp1.getText().toString().trim()
                + otp2.getText().toString().trim()
                + otp3.getText().toString().trim()
                + otp4.getText().toString().trim()
                + otp5.getText().toString().trim()
                + otp6.getText().toString().trim();

        if (otp.length() != 6 || TextUtils.isEmpty(otp)) {
            Toast.makeText(getContext(), "Vui lòng nhập đủ 6 số OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        if (otp.equals("123456")) {
            showDialog(true); // đúng OTP
        } else {
            showDialog(false); // sai OTP
        }
    }

    private void showDialog(boolean isSuccess) {
        int layout = isSuccess ? R.layout.layout_success_dialog : R.layout.layout_failed_dialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(layout, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        new Handler().postDelayed(() -> {
            dialog.dismiss();

            if (isSuccess) {
                // Chuyển về LoginFragment sau khi thành công
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.auth_fragment_container, new LoginFragment())
                        .commit();
            } else {
                // Xóa ô nhập để nhập lại OTP
                clearOtpInputs();
            }
        }, 2000);
    }

    private void clearOtpInputs() {
        otp1.setText("");
        otp2.setText("");
        otp3.setText("");
        otp4.setText("");
        otp5.setText("");
        otp6.setText("");
        otp1.requestFocus();
    }

    private void setupOtpInputs() {
        otp1.addTextChangedListener(new GenericTextWatcher(otp1, otp2));
        otp2.addTextChangedListener(new GenericTextWatcher(otp2, otp3));
        otp3.addTextChangedListener(new GenericTextWatcher(otp3, otp4));
        otp4.addTextChangedListener(new GenericTextWatcher(otp4, otp5));
        otp5.addTextChangedListener(new GenericTextWatcher(otp5, otp6));
        otp6.addTextChangedListener(new GenericTextWatcher(otp6, null));
    }

    private class GenericTextWatcher implements TextWatcher {
        private final EditText currentView;
        private final EditText nextView;

        public GenericTextWatcher(EditText currentView, EditText nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 1 && nextView != null) {
                nextView.requestFocus();
            } else if (s.length() == 0) {
                currentView.focusSearch(View.FOCUS_LEFT).requestFocus();
            }
        }
    }
}
