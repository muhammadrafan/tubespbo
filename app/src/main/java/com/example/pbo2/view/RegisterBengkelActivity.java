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
import com.example.pbo2.controller.UserController;
import com.example.pbo2.helper.FileUtils;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class RegisterBengkelActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText regBengkelName, regBengkelAddress, regBengkelOpen;
    private Button btnUploadImage, btnRegisterBengkel, btnPreviewMap;
    private TextView regbengkelLogin;

    private Uri imageUri;
    private String imagePath;
    private double latitude;
    private double longitude;

    private String name, phoneNumber, age, role, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_bengkel);

        regBengkelName = findViewById(R.id.register_bengkel_name);
        regBengkelAddress = findViewById(R.id.register_bengkel_address);
        regBengkelOpen = findViewById(R.id.register_bengkel_open);
        btnUploadImage = findViewById(R.id.register_bengkel_upload_image);
        btnRegisterBengkel = findViewById(R.id.btn_register_bengkel);
        regbengkelLogin = findViewById(R.id.regbengkel_textlogin);
        btnPreviewMap = findViewById(R.id.btn_preview_map);

        Intent intent = getIntent();
        name = intent.getStringExtra("USER_NAME");
        phoneNumber = intent.getStringExtra("USER_PHONE");
        age = intent.getStringExtra("USER_AGE");
        role = intent.getStringExtra("USER_ROLE");
        password = intent.getStringExtra("USER_PASSWORD");

        btnUploadImage.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
        });

        btnPreviewMap.setOnClickListener(v -> {
            String address = regBengkelAddress.getText().toString().trim();
            if (address.isEmpty()) {
                Toast.makeText(this, "Masukkan alamat bengkel terlebih dahulu!", Toast.LENGTH_SHORT).show();
                return;
            }
            getCoordinatesFromAddress(address);
        });

        btnRegisterBengkel.setOnClickListener(v -> {
            String bengkelName = regBengkelName.getText().toString().trim();
            String bengkelAddress = regBengkelAddress.getText().toString().trim();
            String bengkelOpen = regBengkelOpen.getText().toString().trim();

            if (bengkelName.isEmpty() || bengkelAddress.isEmpty() || bengkelOpen.isEmpty() || imagePath == null || latitude == 0 || longitude == 0) {
                Toast.makeText(RegisterBengkelActivity.this, "Mohon isi semua data dan pastikan koordinat telah diambil!", Toast.LENGTH_SHORT).show();
                return;
            }

            UserController userController = new UserController(this, new UserController.RegisterCallback() {
                @Override
                public void onRegisterSuccess(int userId, String name, String phoneNumber, String age, String role) {
                    Toast.makeText(RegisterBengkelActivity.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onRegisterFailure(String errorMessage) {
                    Toast.makeText(RegisterBengkelActivity.this, "Registrasi gagal: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
            userController.register(name, phoneNumber, age, role, password, bengkelName, bengkelAddress, bengkelOpen, imagePath, latitude, longitude);
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imagePath = FileUtils.getPath(this, imageUri);

            if (imagePath != null) {
                Toast.makeText(this, "Gambar berhasil dipilih!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Gagal mendapatkan path gambar!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
