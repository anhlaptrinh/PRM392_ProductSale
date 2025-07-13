package com.example.productsaleprm.fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.productsaleprm.databinding.FragmentEditUserBinding;
import com.example.productsaleprm.databinding.FragmentUserInfoBinding;
import com.example.productsaleprm.model.User;
import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.retrofit.RetrofitClient;
import com.example.productsaleprm.retrofit.UserAPI;

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
        userAPI.updateUser(currentUser).enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    reloadUserInfoView();
                } else {
                    Toast.makeText(getContext(), "Failed to update\n" + (response.body() != null ? response.body().getMessage() : ""), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
}