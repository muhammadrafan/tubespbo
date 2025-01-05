package com.example.pbo2.view;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity implements UserController.RegisterCallback {

    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int REQUEST_BENGKEL = 101;

    private EditText regName, regPhone, regAge, regPassword, regInputRoles;
    private TextView regLogin;
    private Button btnRegister, btnUploadImage;
    private UserController userController;

    private String selectedProfileImagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        regLogin = findViewById(R.id.reg_textlogin);
        regName = findViewById(R.id.register_nama);
        regPhone = findViewById(R.id.register_phone);
        regAge = findViewById(R.id.register_umur);
        regPassword = findViewById(R.id.register_password);
        regInputRoles = findViewById(R.id.inputrole);
        btnUploadImage = findViewById(R.id.register_upload_image_profile);
        btnRegister = findViewById(R.id.btn_register);

        userController = new UserController(this, this);

        regLogin.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));

        btnUploadImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnRegister.setOnClickListener(v -> {
            String name = regName.getText().toString().trim();
            String phoneNumber = regPhone.getText().toString().trim();
            String age = regAge.getText().toString().trim();
            String password = regPassword.getText().toString().trim();
            String role = regInputRoles.getText().toString().trim();

            if (name.isEmpty() || phoneNumber.isEmpty() || age.isEmpty() || password.isEmpty() || role.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Mohon isi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedProfileImagePath == null) {
                Toast.makeText(RegisterActivity.this, "Pilih gambar profil!", Toast.LENGTH_SHORT).show();
                return;
            }

            if ("Bengkel".equalsIgnoreCase(role)) {
                Intent intent = new Intent(RegisterActivity.this, RegisterBengkelActivity.class);
                intent.putExtra("USER_NAME", name);
                intent.putExtra("USER_PHONE", phoneNumber);
                intent.putExtra("USER_AGE", age);
                intent.putExtra("USER_ROLE", role);
                intent.putExtra("USER_PASSWORD", password);
                intent.putExtra("USER_IMAGE", selectedProfileImagePath);
                startActivityForResult(intent, REQUEST_BENGKEL);
            } else {
                userController.register(name, phoneNumber, age, role, password, null, null, null, selectedProfileImagePath, null, 0, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Untuk gambar profil
            Uri uri = data.getData();
            selectedProfileImagePath = FileUtils.getPath(this, uri);

            if (selectedProfileImagePath != null) {
                Toast.makeText(this, "Gambar profil berhasil dipilih!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Gagal mendapatkan path gambar profil!", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_BENGKEL && resultCode == RESULT_OK && data != null) {
            // Data tambahan dari RegisterBengkelActivity
            String name = data.getStringExtra("USER_NAME");
            String phoneNumber = data.getStringExtra("USER_PHONE");
            String age = data.getStringExtra("USER_AGE");
            String role = data.getStringExtra("USER_ROLE");
            String password = data.getStringExtra("USER_PASSWORD");
            String bengkelName = data.getStringExtra("BENGKEL_NAME");
            String bengkelAddress = data.getStringExtra("BENGKEL_ADDRESS");
            String bengkelOpen = data.getStringExtra("BENGKEL_OPEN");
            String bengkelImagePath = data.getStringExtra("BENGKEL_IMAGE");
            double latitude = data.getDoubleExtra("LATITUDE", 0.0);
            double longitude = data.getDoubleExtra("LONGITUDE", 0.0);

            if (bengkelImagePath != null) {
                Toast.makeText(this, "Gambar bengkel berhasil diterima!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Gagal mendapatkan path gambar bengkel!", Toast.LENGTH_SHORT).show();
            }

            // Registrasi user dengan role Bengkel
            userController.register(name, phoneNumber, age, role, password, bengkelName, bengkelAddress, bengkelOpen, selectedProfileImagePath, bengkelImagePath, latitude, longitude);
        }
    }

    @Override
    public void onRegisterSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onRegisterFailure(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
