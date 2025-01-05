package com.example.pbo2.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pbo2.R;
import com.example.pbo2.controller.BengkelController;
import com.example.pbo2.helper.FileUtils;

import java.io.IOException;
import java.util.List;

public class ChangeBengkelActivity extends AppCompatActivity {
    private EditText newName, newAddress, newOpen;
    private Button newPreview, newImage, newSubmit;
    private String selectedBengkelImagePath = null;
    private double latitude = 0.0, longitude = 0.0;
    private int userId;

    private BengkelController bengkelController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bengkel_change);

        // Inisialisasi View
        newName = findViewById(R.id.new_bengkel_nama);
        newAddress = findViewById(R.id.new_bengkel_address);
        newOpen = findViewById(R.id.new_bengkel_open);
        newPreview = findViewById(R.id.new_bengkel_preview);
        newImage = findViewById(R.id.new_bengkel_image);
        newSubmit = findViewById(R.id.new_bengkel_submit);

        // Inisialisasi Controller
        bengkelController = new BengkelController(this);

        // Ambil User ID dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", -1);

        // Tombol Pilih Gambar
        newImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        });

        // Tombol Preview Map
        newPreview.setOnClickListener(v -> {
            String address = newAddress.getText().toString().trim();
            if (!address.isEmpty()) {
                getCoordinatesFromAddress(address);
            } else {
                Toast.makeText(this, "Masukkan alamat terlebih dahulu!", Toast.LENGTH_SHORT).show();
            }
        });

        // Tombol Submit
        newSubmit.setOnClickListener(v -> {
            String bengkelName = newName.getText().toString().trim();
            String bengkelAddress = newAddress.getText().toString().trim();
            String bengkelOpen = newOpen.getText().toString().trim();

            if (bengkelName.isEmpty() || bengkelAddress.isEmpty() || bengkelOpen.isEmpty()) {
                Toast.makeText(this, "Isi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            bengkelController.updateBengkelData(userId, bengkelName, bengkelAddress, bengkelOpen, latitude, longitude, selectedBengkelImagePath, new BengkelController.BengkelUpdateCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(ChangeBengkelActivity.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(ChangeBengkelActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void getCoordinatesFromAddress(String address) {
        Thread thread = new Thread(() -> {
            try {
                Geocoder geocoder = new Geocoder(this);
                List<Address> addresses = geocoder.getFromLocationName(address, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address location = addresses.get(0);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    runOnUiThread(() -> Toast.makeText(this, "Koordinat berhasil diambil: Lat: " + latitude + ", Lng: " + longitude, Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Gagal mendapatkan koordinat: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
        thread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            selectedBengkelImagePath = FileUtils.getPath(this, uri);
            Toast.makeText(this, "Gambar berhasil dipilih!", Toast.LENGTH_SHORT).show();
        }
    }
}