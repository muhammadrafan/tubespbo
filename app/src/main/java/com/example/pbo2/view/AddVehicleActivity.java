package com.example.pbo2.view;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.pbo2.R;
import com.example.pbo2.controller.VehicleController;

import java.io.IOException;

public class AddVehicleActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 1;

    private ImageView imageVehicle;
    private EditText etMerek, etModel, etTahun;
    private CheckBox cbIsDamaged;
    private Button btnSubmitVehicle;
    private Uri selectedImageUri;

    private VehicleController vehicleController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_usser_register_kendaraan);

        // Inisialisasi view
        imageVehicle = findViewById(R.id.image_vehicle);
        etMerek = findViewById(R.id.et_merek);
        etModel = findViewById(R.id.et_model);
        etTahun = findViewById(R.id.et_tahun);
        cbIsDamaged = findViewById(R.id.cb_is_damaged);
        btnSubmitVehicle = findViewById(R.id.btn_submit_vehicle);
        Button btnSelectImage = findViewById(R.id.btn_select_image);

        vehicleController = new VehicleController(this);

        // Pilih gambar
        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });

        // Submit kendaraan
        btnSubmitVehicle.setOnClickListener(v -> {
            String merek = etMerek.getText().toString().trim();
            String model = etModel.getText().toString().trim();
            String tahunStr = etTahun.getText().toString().trim();
            boolean isDamaged = cbIsDamaged.isChecked();

            if (merek.isEmpty() || model.isEmpty() || tahunStr.isEmpty() || selectedImageUri == null) {
                Toast.makeText(this, "Lengkapi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            int tahun = Integer.parseInt(tahunStr);
            vehicleController.addVehicle(merek, model, tahun, isDamaged, selectedImageUri);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                imageVehicle.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal memuat gambar.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
