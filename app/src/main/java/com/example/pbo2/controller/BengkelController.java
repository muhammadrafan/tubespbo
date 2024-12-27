package com.example.pbo2.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pbo2.R;
import com.example.pbo2.api.ApiService;
import com.example.pbo2.api.RetrofitClient;
import com.example.pbo2.model.Bengkel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BengkelController {
    private static final String TAG = "BengkelController";
    private Context context;

    public BengkelController(Context context) {
        this.context = context;
    }

    public void fetchBengkelData(LinearLayout container) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Bengkel>> call = apiService.getBengkelList();

        call.enqueue(new Callback<List<Bengkel>>() {
            @Override
            public void onResponse(Call<List<Bengkel>> call, Response<List<Bengkel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Data berhasil diterima: " + response.body().size() + " bengkel");
                    populateBengkelList(container, response.body());
                } else {
                    Log.e(TAG, "Gagal memuat data: " + response.code());
                    showToast("Gagal memuat data bengkel.");
                }
            }

            @Override
            public void onFailure(Call<List<Bengkel>> call, Throwable t) {
                Log.e(TAG, "Kesalahan jaringan: " + t.getMessage());
                showToast("Kesalahan jaringan: " + t.getMessage());
            }
        });
    }

    private void populateBengkelList(LinearLayout container, List<Bengkel> bengkelList) {
        container.removeAllViews();

        if (bengkelList.isEmpty()) {
            // Tampilkan pesan jika tidak ada data bengkel
            TextView emptyMessage = new TextView(context);
            emptyMessage.setText("Tidak ada bengkel yang tersedia.");
            emptyMessage.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            emptyMessage.setPadding(16, 16, 16, 16);
            container.addView(emptyMessage);
            return;
        }

        for (Bengkel bengkel : bengkelList) {
            View bengkelCard = createBengkelCard(bengkel);
            container.addView(bengkelCard);
        }
    }

    private View createBengkelCard(Bengkel bengkel) {
        View bengkelCard = LayoutInflater.from(context).inflate(R.layout.card_bengkel, null, false);

        TextView name = bengkelCard.findViewById(R.id.bengkel_name);
        TextView address = bengkelCard.findViewById(R.id.bengkel_address);
        ImageView image = bengkelCard.findViewById(R.id.bengkel_image);

        // Set data pada card
        name.setText(bengkel.getBengkelName());
        address.setText(bengkel.getBengkelAddress());

        // Set gambar
        if (bengkel.getBengkelImage() != null && !bengkel.getBengkelImage().isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(bengkel.getBengkelImage(), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                image.setImageBitmap(decodedImage);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Error decoding image: " + e.getMessage());
                image.setImageResource(R.drawable.error_image); // Placeholder jika decoding gagal
            }
        } else {
            image.setImageResource(R.drawable.error_image); // Placeholder jika tidak ada gambar
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 32); // Jarak antar card di bagian bawah
        bengkelCard.setLayoutParams(params);
        return bengkelCard;
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
