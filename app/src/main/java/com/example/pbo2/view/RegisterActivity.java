package com.example.pbo2.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pbo2.R;
import com.example.pbo2.controller.UserController;

public class RegisterActivity extends AppCompatActivity implements UserController.RegisterCallback {
    private EditText regName, regPhone, regAge, regPassword, regInputRoles;
    private TextView reg_login;
    private Button btnRegister;
    private UserController userController;

    private static final int REQUEST_BENGKEL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        // Inisialisasi View
        reg_login = findViewById(R.id.reg_textlogin);
        regName = findViewById(R.id.register_nama);
        regPhone = findViewById(R.id.register_phone);
        regAge = findViewById(R.id.register_umur);
        regPassword = findViewById(R.id.register_password);
        regInputRoles = findViewById(R.id.inputrole);
        btnRegister = findViewById(R.id.btn_register);

        // Inisialisasi Controller
        userController = new UserController(this, this);
        reg_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = regName.getText().toString().trim();
                String phoneNumber = regPhone.getText().toString().trim();
                String age = regAge.getText().toString().trim();
                String password = regPassword.getText().toString().trim();
                String role = regInputRoles.getText().toString().trim();

                // Validasi Input
                if (name.isEmpty() || phoneNumber.isEmpty() || age.isEmpty() || password.isEmpty() || role.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Mohon isi semua data!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Log untuk Debugging
                System.out.println("Role yang dipilih: " + role);

                // Pengecekan Role
                if ("Bengkel".equals(role)) {
                    // Role Bengkel, arahkan ke RegisterBengkelActivity
                    Intent intent = new Intent(RegisterActivity.this, RegisterBengkelActivity.class);
                    intent.putExtra("USER_NAME", name);
                    intent.putExtra("USER_PHONE", phoneNumber);
                    intent.putExtra("USER_AGE", age);
                    intent.putExtra("USER_ROLE", role);
                    intent.putExtra("USER_PASSWORD", password);
                    startActivityForResult(intent, REQUEST_BENGKEL);
                } else {
                    // Role lain, langsung registrasi
                    userController.register(name, phoneNumber, age, role, password, null, null, null, null,0,0);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BENGKEL && resultCode == RESULT_OK && data != null) {
            // Ambil data tambahan dari RegisterBengkelActivity
            String name = data.getStringExtra("USER_NAME");
            String phoneNumber = data.getStringExtra("USER_PHONE");
            String age = data.getStringExtra("USER_AGE");
            String role = data.getStringExtra("USER_ROLE");
            String password = data.getStringExtra("USER_PASSWORD");
            String bengkelName = data.getStringExtra("BENGKEL_NAME");
            String bengkelAddress = data.getStringExtra("BENGKEL_ADDRESS");
            String bengkelOpen = data.getStringExtra("BENGKEL_OPEN");
            String bengkelImage = data.getStringExtra("BENGKEL_IMAGE");
            double latitude = data.getDoubleExtra("LATITUDE", 0.0);
            double longitude = data.getDoubleExtra("LONGITUDE", 0.0);

            // Lakukan registrasi dengan data tambahan untuk Bengkel
            userController.register(name, phoneNumber, age, role, password, bengkelName, bengkelAddress, bengkelOpen, bengkelImage, latitude, longitude);
        }
    }

    @Override
    public void onRegisterSuccess(int userId, String name, String phoneNumber, String age, String role) {
        Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
        // Arahkan ke LoginActivity setelah registrasi berhasil
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegisterFailure(String errorMessage) {
        // Tampilkan pesan kesalahan
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
