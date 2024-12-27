package com.example.pbo2.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pbo2.R;
import com.example.pbo2.controller.VehicleController;
import com.example.pbo2.helper.FileUtils;

public class AddVehicleActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageVehicle;
    private EditText etMerek, etModel, etTahun;
    private CheckBox cbIsDamaged;
    private Button btnSelectImage, btnSubmitVehicle;

    private String imagePath = null;
    private VehicleController vehicleController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_usser_register_kendaraan);

        // Inisialisasi View
        imageVehicle = findViewById(R.id.image_vehicle);
        etMerek = findViewById(R.id.et_merek);
        etModel = findViewById(R.id.et_model);
        etTahun = findViewById(R.id.et_tahun);
        cbIsDamaged = findViewById(R.id.cb_is_damaged);
        btnSelectImage = findViewById(R.id.btn_select_image);
        btnSubmitVehicle = findViewById(R.id.btn_submit_vehicle);

        // Inisialisasi Controller
        vehicleController = new VehicleController(this);

        // Listener untuk memilih gambar
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        // Listener untuk menyimpan kendaraan
        btnSubmitVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String merek = etMerek.getText().toString().trim();
                String model = etModel.getText().toString().trim();
                String tahunStr = etTahun.getText().toString().trim();

                if (merek.isEmpty() || model.isEmpty() || tahunStr.isEmpty() || imagePath == null) {
                    Toast.makeText(AddVehicleActivity.this, "Harap lengkapi semua data.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int tahun;
                try {
                    tahun = Integer.parseInt(tahunStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(AddVehicleActivity.this, "Tahun harus berupa angka.", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isDamaged = cbIsDamaged.isChecked();

                // Kirim data ke server melalui VehicleController
                vehicleController.addVehicle(merek, model, tahun, isDamaged, imagePath, new VehicleController.VehicleCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(AddVehicleActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish(); // Tutup activity setelah berhasil
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(AddVehicleActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imagePath = FileUtils.getPath(this, imageUri);
            if (imagePath != null) {
                imageVehicle.setImageURI(imageUri); // Tampilkan gambar yang dipilih
            } else {
                Toast.makeText(this, "Gagal mendapatkan path gambar.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
