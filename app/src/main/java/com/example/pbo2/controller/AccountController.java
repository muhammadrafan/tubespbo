package com.example.pbo2.controller;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.pbo2.api.ApiService;
import com.example.pbo2.api.RetrofitClient;
import com.example.pbo2.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountController {
    private Context context;

    public AccountController(Context context) {
        this.context = context;
    }

    public interface UserDataCallback {
        void onSuccess(User user);
        void onFailure(String errorMessage);
    }

    public void fetchUserData(int userId, UserDataCallback callback) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<User> call = apiService.getUserById(userId);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to fetch user data.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                callback.onFailure("Error: " + t.getMessage());
            }
        });
    }

    public void loadProfileImage(String imageUser, android.widget.ImageView imageView) {
        if (imageUser != null && imageUser.startsWith("http")) {
            // Jika sumber gambar adalah URL
            Glide.with(context)
                    .load(imageUser)
                    .placeholder(com.example.pbo2.R.drawable.user_error) // Placeholder default
                    .into(imageView);
        } else if (imageUser != null) {
            // Jika sumber gambar adalah Base64
            byte[] decodedString = android.util.Base64.decode(imageUser, android.util.Base64.DEFAULT);
            android.graphics.Bitmap decodedByte = android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        } else {
            // Jika gambar tidak tersedia
            imageView.setImageResource(com.example.pbo2.R.drawable.user_error);
        }
    }
}
