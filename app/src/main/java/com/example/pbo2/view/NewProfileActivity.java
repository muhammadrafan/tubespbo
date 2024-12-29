package com.example.pbo2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pbo2.R;
import com.example.pbo2.controller.UpdateController;
import com.example.pbo2.helper.FileUtils;

import java.io.File;

public class NewProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Kode permintaan untuk memilih gambar
    private EditText name, phone, age;
    private Button saveButton, uploadImageButton;
    private UpdateController updateController;
    private File selectedImageFile; // Variabel untuk menyimpan file gambar yang dipilih
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);

        // Inisialisasi view
        name = findViewById(R.id.new_nama);
        phone = findViewById(R.id.new_phone);
        age = findViewById(R.id.new_umur);
        saveButton = findViewById(R.id.btn_new);
        uploadImageButton = findViewById(R.id.new_upload_image_profile);

        // Inisialisasi controller
        updateController = new UpdateController();

        // Ambil ID pengguna dari sesi
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", -1);

        // Tombol untuk menyimpan data pengguna
        saveButton.setOnClickListener(v -> {
            String newName = name.getText().toString();
            String newPhone = phone.getText().toString();
            int newAge;

            // Validasi umur
            try {
                newAge = Integer.parseInt(age.getText().toString());
            } catch (NumberFormatException e) {
                age.setError("Umur harus berupa angka!");
                return;
            }

            // Kirim permintaan ke server
            updateController.updateUser(userId, newName, newPhone, newAge, selectedImageFile, this);
        });

        // Tombol untuk memilih gambar
        uploadImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Dapatkan path file menggunakan FileUtils
                String filePath = FileUtils.getPath(this, selectedImageUri);
                if (filePath != null) {
                    selectedImageFile = new File(filePath); // Simpan file gambar yang dipilih
                    Log.d("NewProfileActivity", "Selected Image Path: " + filePath);
                    Toast.makeText(this, "Gambar berhasil dipilih.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Gagal mendapatkan path gambar.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
