package com.example.pbo2.view;

import android.content.SharedPreferences;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pbo2.helper.FileUtils;

import java.io.File;

import com.example.pbo2.controller.KendaraanController;
import com.example.pbo2.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserRegisterKendaraanActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageVehicle;
    private EditText etMerek, etModel, etTahun;
    private CheckBox cbIsDamagged;
    private Button btnSubmitVehicle, btnSelectImage;

    private Uri selectedImageUri;
    private int userId; // ID user dari SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_usser_register_kendaraan);

        // Ambil user_id dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "User ID tidak ditemukan. Harap login ulang.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inisialisasi View
        imageVehicle = findViewById(R.id.image_vehicle);
        etMerek = findViewById(R.id.et_merek);
        etModel = findViewById(R.id.et_model);
        etTahun = findViewById(R.id.et_tahun);
        cbIsDamagged = findViewById(R.id.cb_is_damaged);
        btnSubmitVehicle = findViewById(R.id.btn_submit_vehicle);
        btnSelectImage = findViewById(R.id.btn_select_image);

        btnSelectImage.setOnClickListener(v -> openImageChooser());
        btnSubmitVehicle.setOnClickListener(v -> saveKendaraan());
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageVehicle.setImageURI(selectedImageUri);
        }
    }

    private void saveKendaraan() {
        String merek = etMerek.getText().toString();
        String model = etModel.getText().toString();
        String tahun = etTahun.getText().toString();
        boolean isDamagged = cbIsDamagged.isChecked();

        if (selectedImageUri == null) {
            Toast.makeText(this, "Pilih gambar kendaraan.", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(FileUtils.getPath(this, selectedImageUri));
        RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/*"));
        MultipartBody.Part vehicleImage = MultipartBody.Part.createFormData("vehicleImage", file.getName(), requestFile);

        RequestBody userIdBody = RequestBody.create(String.valueOf(userId), MediaType.parse("text/plain"));
        RequestBody merekBody = RequestBody.create(merek, MediaType.parse("text/plain"));
        RequestBody modelBody = RequestBody.create(model, MediaType.parse("text/plain"));
        RequestBody tahunBody = RequestBody.create(tahun, MediaType.parse("text/plain"));
        RequestBody isDamaggedBody = RequestBody.create(String.valueOf(isDamagged ? 1 : 0), MediaType.parse("text/plain"));

        new KendaraanController().saveKendaraan(userIdBody, merekBody, modelBody, tahunBody, isDamaggedBody, vehicleImage, new KendaraanController.SaveKendaraanCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(UserRegisterKendaraanActivity.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(UserRegisterKendaraanActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
