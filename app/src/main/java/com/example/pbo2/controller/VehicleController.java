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

    public void addVehicle(String merek, String model, int tahun, boolean isDamaged, Uri imageUri) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Konversi data menjadi Multipart
        RequestBody merekBody = RequestBody.create(MediaType.parse("text/plain"), merek);
        RequestBody modelBody = RequestBody.create(MediaType.parse("text/plain"), model);
        RequestBody tahunBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(tahun));
        RequestBody isDamagedBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(isDamaged));
        File imageFile = new File(imageUri.getPath());
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("foto_kendaraan", imageFile.getName(), imageBody);

        // Panggil API
        Call<ResponseBody> call = apiService.addVehicle(merekBody, modelBody, tahunBody, isDamagedBody, imagePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Kendaraan berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Gagal menambahkan kendaraan.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
