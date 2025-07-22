package com.example.productsaleprm.fragement;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.productsaleprm.activity.MainAuthActivity;
import com.example.productsaleprm.activity.MapActivity;
import com.example.productsaleprm.databinding.FragmentEditUserBinding;
import com.example.productsaleprm.databinding.FragmentUserInfoBinding;
import com.example.productsaleprm.model.User;
import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.retrofit.RetrofitClient;
import com.example.productsaleprm.retrofit.UserAPI;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private FragmentUserInfoBinding userInfoBinding;
    private FragmentEditUserBinding editUserBinding;
    private UserAPI userAPI;
    private User currentUser;

    private boolean isEditing = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        userAPI = RetrofitClient.getClient(requireContext()).create(UserAPI.class);
        userInfoBinding = FragmentUserInfoBinding.inflate(inflater, container, false);
        View rootView = userInfoBinding.getRoot();

        fetchCurrentUser();

        userInfoBinding.btnEdit.setOnClickListener(v -> {
            if (currentUser != null) {
                switchToEditMode(container);
            }
        });

        userInfoBinding.btnGetStoreLocation.setOnClickListener(v -> {
            Intent intent  = new Intent(requireContext(), MapActivity.class);
            startActivity(intent);
        });

        userInfoBinding.btnLogout.setOnClickListener(v -> {
            logout();
        });

        return rootView;
    }

    private void switchToEditMode(ViewGroup container) {
        isEditing = true;
        editUserBinding = FragmentEditUserBinding.inflate(getLayoutInflater(), container, false);
        editUserBinding.setUser(currentUser);

        editUserBinding.btnUpdate.setOnClickListener(v -> updateUser());

        editUserBinding.btnBack.setOnClickListener(v -> reloadUserInfoView());

        ViewGroup parent = (ViewGroup) userInfoBinding.getRoot().getParent();
        if (parent != null) {
            parent.removeAllViews();
            parent.addView(editUserBinding.getRoot());
        }
    }

    private void fetchCurrentUser() {
        userAPI.getCurrentUser().enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    currentUser = response.body().getData();
                    userInfoBinding.setUser(currentUser);
                } else {
                    Toast.makeText(getContext(), "Failed to load user info\n" + (response.body() != null ? response.body().getMessage() : ""), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUser() {
        String updatedUsername = editUserBinding.usernameLayout.getEditText().getText().toString().trim();
        String updatedPhone = editUserBinding.phoneLayout.getEditText().getText().toString().trim();
        String updatedAddress = editUserBinding.addressLayout.getEditText().getText().toString().trim();

        if (updatedUsername.isEmpty()) {
            Toast.makeText(getContext(), "Username is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPhone(updatedPhone)) {
            Toast.makeText(getContext(), "Phone is incorrect!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidAddress(updatedAddress)) {
            Toast.makeText(getContext(), "Address is incorrect!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật lại dữ liệu user bằng các giá trị đã nhập
        currentUser.setUsername(updatedUsername);
        currentUser.setPhoneNumber(updatedPhone);
        currentUser.setAddress(updatedAddress);

        userAPI.updateUser(currentUser).enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Updated successful", Toast.LENGTH_SHORT).show();
                    reloadUserInfoView();
                } else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    Log.e("ProfileFragment", "onResponse: " + response);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                Log.e("ProfileFragment", "updateUser error: "+t.getMessage());
                Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reloadUserInfoView() {
        ViewGroup parent = (ViewGroup) editUserBinding.getRoot().getParent();
        if (parent != null) {
            parent.removeAllViews();
            userInfoBinding.setUser(currentUser);
            parent.addView(userInfoBinding.getRoot());
        }
        isEditing = false;
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^(\\+84|0)[0-9]{9,10}$");
    }

    private boolean isValidAddress(String address) {
        if (address == null || address.isEmpty()) return false;

        try {
            Geocoder geocoder = new Geocoder(getContext());
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            return addresses != null && !addresses.isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("API", "isValidAddress: " + e.getMessage());
            return false;
        }
    }

    private void logout() {
        // Xóa JWT token khỏi SharedPreferences
        requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
                .edit()
                .remove("jwt_token")
                .apply();

        Intent intent = new Intent(requireContext(), MainAuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}