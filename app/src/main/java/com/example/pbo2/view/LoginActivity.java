package com.example.pbo2.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pbo2.R;
import com.example.pbo2.controller.UserController;
import com.example.pbo2.model.MainActivity;
import com.example.pbo2.model.User;

public class LoginActivity extends AppCompatActivity implements UserController.LoginCallback {
    private TextView textRegister;
    private EditText etPhoneNumber, etPassword;
    private Button btnLogin;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi View
        textRegister = findViewById(R.id.login_register);
        etPhoneNumber = findViewById(R.id.login_phone);
        etPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.btn_login);

        // Inisialisasi Controller
        userController = new UserController(this, this);
        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        // Setup Login Button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = etPhoneNumber.getText().toString();
                String password = etPassword.getText().toString();

                // Panggil method login
                userController.login(phoneNumber, password);
            }
        });
    }

    // Implementasi callback login success
    @Override
    public void onLoginSuccess(User user) {
        // Pindah ke halaman selanjutnya
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("USER_ID", user.getId());
        intent.putExtra("USER_NAME", user.getName());
        startActivity(intent);
        finish(); // Tutup login activity
    }

    // Implementasi callback login failure
    @Override
    public void onLoginFailure(String errorMessage) {
        // Tidak perlu tambahan aksi, toast sudah ditangani di controller
    }
}