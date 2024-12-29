package com.example.pbo2.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pbo2.R;
import com.example.pbo2.controller.UpdateController;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPassword, newPassword, confirmPassword;
    private Button changePasswordButton;
    private int userId;
    private UpdateController updateController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_change_password);

        oldPassword = findViewById(R.id.old_password);
        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirm_password);
        changePasswordButton = findViewById(R.id.btn_change_password);

        updateController = new UpdateController();

        // Ambil ID pengguna dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", -1);

        // Tombol untuk mengganti password
        changePasswordButton.setOnClickListener(v -> {
            String oldPass = oldPassword.getText().toString().trim();
            String newPass = newPassword.getText().toString().trim();
            String confirmPass = confirmPassword.getText().toString().trim();

            // Validasi input
            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                confirmPassword.setError("Password konfirmasi tidak cocok!");
                return;
            }

            // Kirim permintaan perubahan password melalui controller
            updateController.changePassword(userId, oldPass, newPass, this);
        });
    }
}
