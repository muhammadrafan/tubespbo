package com.example.pbo2.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pbo2.R;
import com.example.pbo2.api.ApiService;
import com.example.pbo2.api.RetrofitClient;
import com.example.pbo2.controller.LocationController;
import com.example.pbo2.model.Pengguna;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BengkelPeggunaActivity extends AppCompatActivity {
    private int orderId, pelangganId;
    private String pelangganNameIntent;
    private TextView pelangganName;
    private EditText jadwalPerbaikanEditText, layananEditText, hargaEditText;
    private CheckBox statusPengerjaanCheckbox, statusPembayaranCheckbox;
    private Button btnOpenMaps,btnSubmitOrder;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_order);

        // Bind elemen UI
        pelangganName = findViewById(R.id.card_order_pelanggan);
        jadwalPerbaikanEditText = findViewById(R.id.jadwal_order);
        layananEditText = findViewById(R.id.layanan_order);
        hargaEditText = findViewById(R.id.harga_order);
        statusPengerjaanCheckbox = findViewById(R.id.statuspengerjaan_order);
        statusPembayaranCheckbox = findViewById(R.id.statuspembayaran_order);
        btnOpenMaps = findViewById(R.id.btn_open_maps);
        btnSubmitOrder = findViewById(R.id.btn_submit_order);

        // Ambil data dari Intent
        pelangganNameIntent = getIntent().getStringExtra("PENGGUNA_NAME");
        orderId = getIntent().getIntExtra("ORDER_ID", -1);
        pelangganId = getIntent().getIntExtra("PENGGUNA_ID", -1);

        // Tampilkan nama pelanggan
        if (pelangganNameIntent != null) {
            pelangganName.setText(pelangganNameIntent);
        } else {
            pelangganName.setText("Nama pelanggan tidak ditemukan");
        }

        // Tambahkan listener ke tombol "Pergi ke Lokasi Pelanggan"
        btnOpenMaps.setOnClickListener(v -> {
            // Gunakan LocationController untuk menangani logika
            LocationController locationController = new LocationController(this);
            fetchPelangganLocation(locationController, pelangganId);
        });
        btnSubmitOrder.setOnClickListener(v -> {
            // Ambil nilai dari elemen UI
            String jadwalPerbaikan = jadwalPerbaikanEditText.getText().toString().trim();
            String layanan = layananEditText.getText().toString().trim();
            String hargaStr = hargaEditText.getText().toString().trim();
            boolean statusPengerjaan = statusPengerjaanCheckbox.isChecked();
            boolean statusPembayaran = statusPembayaranCheckbox.isChecked();

            // Validasi input
            if (jadwalPerbaikan.isEmpty() || layanan.isEmpty() || hargaStr.isEmpty()) {
                Toast.makeText(BengkelPeggunaActivity.this, "Harap lengkapi semua field.", Toast.LENGTH_SHORT).show();
                return;
            }

            double harga;
            try {
                harga = Double.parseDouble(hargaStr);
            } catch (NumberFormatException e) {
                Toast.makeText(BengkelPeggunaActivity.this, "Harga harus berupa angka.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kirim data ke server untuk update order
            updateOrder(orderId, jadwalPerbaikan, layanan, harga, statusPengerjaan, statusPembayaran);
        });
    }


    private void fetchPelangganLocation(LocationController locationController, int pelangganId) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Call<Pengguna> call = apiService.getPenggunaById(pelangganId);
        call.enqueue(new Callback<Pengguna>() {
            @Override
            public void onResponse(Call<Pengguna> call, Response<Pengguna> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Pengguna pengguna = response.body();
                    double latitude = pengguna.getLatitude();
                    double longitude = pengguna.getLongitude();

                    // Periksa apakah koordinat valid
                    if (latitude != 0 && longitude != 0) {
                        // Panggil method dari LocationController untuk membuka Google Maps
                        locationController.fetchLocationAndOpenMaps(latitude, longitude);
                    } else {
                        Toast.makeText(BengkelPeggunaActivity.this, "Koordinat pelanggan tidak valid.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BengkelPeggunaActivity.this, "Gagal mengambil lokasi pelanggan.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pengguna> call, Throwable t) {
                Toast.makeText(BengkelPeggunaActivity.this, "Kesalahan jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateOrder(int orderId, String jadwalPerbaikan, String layanan, double harga, boolean statusPengerjaan, boolean statusPembayaran) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Call<Void> call = apiService.updateOrder(orderId, statusPengerjaan, statusPembayaran, jadwalPerbaikan, layanan, harga);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BengkelPeggunaActivity.this, "Order berhasil diperbarui.", Toast.LENGTH_SHORT).show();
                    finish(); // Tutup activity
                } else {
                    Toast.makeText(BengkelPeggunaActivity.this, "Gagal memperbarui order.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(BengkelPeggunaActivity.this, "Kesalahan jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
