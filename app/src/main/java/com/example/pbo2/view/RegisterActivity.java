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
import com.example.pbo2.model.MainActivity;
import com.example.pbo2.model.User;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity implements UserController.RegisterCallback{
    private TextView textmasuk;
    private EditText regName,regPhone,regAge, regPassword;
    private Button btnRegister;
    private TextInputLayout  regRoles;
    private MaterialAutoCompleteTextView regInputRoles;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        // Inisialisasi View
        regName = findViewById(R.id.register_nama);
        regPhone = findViewById(R.id.register_phone);
        regAge = findViewById(R.id.register_umur);
        regRoles = findViewById(R.id.register_roles);
        regInputRoles = findViewById(R.id.inputrole);
        regPassword = findViewById(R.id.register_password);
        btnRegister = findViewById(R.id.btn_register);
        textmasuk = findViewById(R.id.reg_textlogin);

        // Inisialisasi Controller
        userController = new UserController(this,this);
        textmasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        // Setup Login Button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = regName.getText().toString();
                String phoneNumber = regPhone.getText().toString();
                String age = regAge.getText().toString();
                String password = regPassword.getText().toString();
                String role = regInputRoles.getText().toString();
                if (role.isEmpty()) {
                    regRoles.setError("Silakan Pilih");
                } else {
                    userController.register(name, phoneNumber, Integer.parseInt(age), role, password);
                }

                // Panggil method login
                userController.login(phoneNumber, password);
            }
        });
    }
    @Override
    public void onRegisterSuccess(int userId, String name, String phoneNumber, int age, String role) {
        // Pindah ke halaman selanjutnya
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("USER_NAME", name);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegisterFailure(String errorMessage) {
        // Tidak perlu tambahan aksi, toast sudah ditangani di controller
    }
}
