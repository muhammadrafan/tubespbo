package com.example.pbo2.controller;

import android.content.Context;
import android.widget.Toast;

import com.example.pbo2.api.ApiService;
import com.example.pbo2.api.RetrofitClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateController {

    private ApiService apiService;

    public UpdateController() {
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    // Update data pengguna
    public void updateUser(int id, String name, String phone, int age, File imageFile, Context context) {
        MultipartBody.Part imagePart = null;

        // Cek apakah file gambar ada
        if (imageFile != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
        }

        // Kirim permintaan API
        apiService.updateAccount(id, name, phone, age, imagePart).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Gagal memperbarui profil.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void changePassword(int userId, String oldPassword, String newPassword, Context context) {
        apiService.changePassword(userId, oldPassword, newPassword).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Password berhasil diubah!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Gagal mengubah password. Periksa password lama.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
