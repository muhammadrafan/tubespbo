package com.example.pbo2.controller;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

public class VehicleController {
    private Context context;

    public VehicleController(Context context) {
        this.context = context;
    }

    // Callback untuk pengiriman data kendaraan
    public interface VehicleCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }

    public void addVehicle(String merek, String model, int tahun, boolean isDamaged, String imagePath, VehicleCallback callback) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // File dan request body untuk foto kendaraan
        File file = new File(imagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part fotoKendaraan = MultipartBody.Part.createFormData("fotoKendaraan", file.getName(), requestFile);

        // Request body untuk data lainnya
        RequestBody merekBody = RequestBody.create(MediaType.parse("text/plain"), merek);
        RequestBody modelBody = RequestBody.create(MediaType.parse("text/plain"), model);
        RequestBody tahunBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(tahun));
        RequestBody isDamagedBody = RequestBody.create(MediaType.parse("text/plain"), isDamaged ? "1" : "0");

        // Panggil API
        Call<ResponseBody> call = apiService.addVehicle(merekBody, modelBody, tahunBody, isDamagedBody, fotoKendaraan);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("Data kendaraan berhasil ditambahkan.");
                } else {
                    callback.onFailure("Gagal menambahkan data kendaraan. " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure("Kesalahan: " + t.getMessage());
            }
        });
    }
}

