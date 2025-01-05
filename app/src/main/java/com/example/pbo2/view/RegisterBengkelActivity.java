package com.example.pbo2.view;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pbo2.R;
import com.example.pbo2.helper.FileUtils;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class RegisterBengkelActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST_BENGKEL = 200;

    private EditText etBengkelName, etBengkelAddress, etBengkelOpen;
    private Button btnPreviewMap, btnUploadBengkelImage, btnRegisterBengkel;
    private TextView regbengkelLogin;

    private String selectedBengkelImagePath = null;
    private double latitude = 0.0;
    private double longitude = 0.0;

    private String userName, userPhoneNumber, userAge, userRole, userPassword, userProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_bengkel);

        // Inisialisasi View
        etBengkelName = findViewById(R.id.register_bengkel_name);
        etBengkelAddress = findViewById(R.id.register_bengkel_address);
        etBengkelOpen = findViewById(R.id.register_bengkel_open);
        btnPreviewMap = findViewById(R.id.btn_preview_map);
        btnUploadBengkelImage = findViewById(R.id.register_bengkel_upload_image);
        btnRegisterBengkel = findViewById(R.id.btn_register_bengkel);
        regbengkelLogin = findViewById(R.id.regbengkel_textlogin);

        // Ambil data dari RegisterActivity
        Intent intent = getIntent();
        userName = intent.getStringExtra("USER_NAME");
        userPhoneNumber = intent.getStringExtra("USER_PHONE");
        userAge = intent.getStringExtra("USER_AGE");
        userRole = intent.getStringExtra("USER_ROLE");
        userPassword = intent.getStringExtra("USER_PASSWORD");
        userProfileImage = intent.getStringExtra("USER_IMAGE");

        // Tombol Login
        regbengkelLogin.setOnClickListener(v -> {
            Intent loginIntent = new Intent(RegisterBengkelActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        });

        // Pilih gambar bengkel
        btnUploadBengkelImage.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST_BENGKEL);
        });

        // Preview lokasi di Maps
        btnPreviewMap.setOnClickListener(v -> {
            String address = etBengkelAddress.getText().toString().trim();
            if (address.isEmpty()) {
                Toast.makeText(this, "Masukkan alamat bengkel terlebih dahulu!", Toast.LENGTH_SHORT).show();
                return;
            }
            getCoordinatesFromAddress(address);
        });

        // Tombol Daftar
        btnRegisterBengkel.setOnClickListener(v -> {
            String bengkelName = etBengkelName.getText().toString().trim();
            String bengkelAddress = etBengkelAddress.getText().toString().trim();
            String bengkelOpen = etBengkelOpen.getText().toString().trim();

            if (bengkelName.isEmpty() || bengkelAddress.isEmpty() || bengkelOpen.isEmpty() || selectedBengkelImagePath == null || latitude == 0 || longitude == 0) {
                Toast.makeText(RegisterBengkelActivity.this, "Mohon isi semua data dan pastikan koordinat telah diambil!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kirim data kembali ke RegisterActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("USER_NAME", userName);
            resultIntent.putExtra("USER_PHONE", userPhoneNumber);
            resultIntent.putExtra("USER_AGE", userAge);
            resultIntent.putExtra("USER_ROLE", userRole);
            resultIntent.putExtra("USER_PASSWORD", userPassword);
            resultIntent.putExtra("USER_IMAGE", userProfileImage);
            resultIntent.putExtra("BENGKEL_NAME", bengkelName);
            resultIntent.putExtra("BENGKEL_ADDRESS", bengkelAddress);
            resultIntent.putExtra("BENGKEL_OPEN", bengkelOpen);
            resultIntent.putExtra("LATITUDE", latitude);
            resultIntent.putExtra("LONGITUDE", longitude);
            resultIntent.putExtra("BENGKEL_IMAGE", selectedBengkelImagePath);

            setResult(RESULT_OK, resultIntent);
            finish();
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

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Koordinat berhasil didapatkan: Lat: " + latitude + ", Lng: " + longitude, Toast.LENGTH_SHORT).show();
                        openMap(new LatLng(latitude, longitude));
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Alamat tidak ditemukan!", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Terjadi kesalahan: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
        thread.start();
    }

    private void openMap(LatLng latLng) {
        String uri = String.format("geo:%f,%f?q=%f,%f(%s)", latLng.latitude, latLng.longitude, latLng.latitude, latLng.longitude, "Lokasi Bengkel");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_BENGKEL && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            selectedBengkelImagePath = FileUtils.getPath(this, uri);
            Toast.makeText(this, "Gambar Bengkel berhasil dipilih!", Toast.LENGTH_SHORT).show();
        }
    }
}
