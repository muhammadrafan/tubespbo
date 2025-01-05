package com.example.pbo2.controller;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pbo2.api.ApiService;
import com.example.pbo2.api.RetrofitClient;
import com.example.pbo2.model.Kendaraan;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KendaraanController {

    public interface SaveKendaraanCallback {
        void onSuccess(String message);
        void onError(String errorMessage);
    }
    public void fetchKendaraanList(int userId, Spinner spinner) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Kendaraan>> call = apiService.getKendaraanList(userId);

        call.enqueue(new Callback<List<Kendaraan>>() {
            @Override
            public void onResponse(Call<List<Kendaraan>> call, Response<List<Kendaraan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Kendaraan> kendaraanList = response.body();

                    // Override metode toString() pada adapter agar hanya menampilkan model kendaraan
                    ArrayAdapter<Kendaraan> adapter = new ArrayAdapter<Kendaraan>(
                            spinner.getContext(),
                            android.R.layout.simple_spinner_item,
                            kendaraanList
                    ) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            // Pastikan spinner menampilkan hanya "model" kendaraan
                            TextView textView = (TextView) super.getView(position, convertView, parent);
                            textView.setText(kendaraanList.get(position).getModel());
                            return textView;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            // Pastikan dropdown menampilkan hanya "model" kendaraan
                            TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                            textView.setText(kendaraanList.get(position).getModel());
                            return textView;
                        }
                    };

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                } else {
                    Toast.makeText(spinner.getContext(), "Gagal memuat kendaraan.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Kendaraan>> call, Throwable t) {
                Toast.makeText(spinner.getContext(), "Kesalahan jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void saveKendaraan(
            RequestBody userId,
            RequestBody merek,
            RequestBody model,
            RequestBody tahun,
            RequestBody isDamagged, // Nama kolom sesuai tabel
            MultipartBody.Part vehicleImage,
            SaveKendaraanCallback callback
    ) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.saveKendaraan(userId, merek, model, tahun, isDamagged, vehicleImage);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("Kendaraan berhasil disimpan.");
                } else {
                    callback.onError("Server error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}
