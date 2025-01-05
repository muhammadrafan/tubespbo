package com.example.pbo2.view;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pbo2.R;
import com.example.pbo2.controller.BengkelController;
import com.example.pbo2.controller.KendaraanController;
import com.example.pbo2.controller.LocationController;
import com.example.pbo2.controller.OrderController;
import com.example.pbo2.controller.UpdateController;
import com.example.pbo2.model.Kendaraan;
import com.example.pbo2.model.Order;

import java.util.List;

public class BengkelOrderActivity extends AppCompatActivity {
    private TextView bengkelName, bengkelAddress;
    private ImageView bengkelImage;
    private Spinner spinnerKendaraan;
    private Button buttonPesan;

    private BengkelController bengkelController;
    private KendaraanController kendaraanController;
    private LocationController locationController;
    private int bengkelId;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bengkel_information);

        // Bind UI components
        bengkelName = findViewById(R.id.bengkel_name);
        bengkelAddress = findViewById(R.id.bengkel_address);
        bengkelImage = findViewById(R.id.bengkel_image);
        spinnerKendaraan = findViewById(R.id.spinner_kendaraan);
        buttonPesan = findViewById(R.id.button_pesan_bengkel);

        // Get data from intent
        bengkelId = getIntent().getIntExtra("BENGKEL_ID",-1) ;
        // Get user ID from session
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", -1);

        // Initialize controllers
        bengkelController = new BengkelController(this);
        kendaraanController = new KendaraanController();


        // Fetch bengkel detail
        bengkelController.fetchBengkelDetail(bengkelId, new BengkelController.BengkelDetailCallback() {
            @Override
            public void onSuccess(String name, String address, String imageBase64) {
                // Set nama bengkel
                bengkelName.setText(name);

                // Set alamat bengkel
                bengkelAddress.setText(address);

                // Decode dan tampilkan gambar
                if (imageBase64 != null) {
                    byte[] decodedBytes = Base64.decode(imageBase64, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    bengkelImage.setImageBitmap(bitmap);
                } else {
                    bengkelImage.setImageResource(R.drawable.error_image); // Placeholder
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(BengkelOrderActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });


        // Fetch kendaraan list
        kendaraanController.fetchKendaraanList(userId, spinnerKendaraan);

        locationController = new LocationController(this);
        locationController.requestLocation(new LocationController.LocationCallback() {
            @Override
            public void onLocationRetrieved(double lat, double lng) {
                latitude = lat;
                longitude = lng;
                Toast.makeText(BengkelOrderActivity.this, "Lokasi diperbarui.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(BengkelOrderActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        // Set button click listener
        buttonPesan.setOnClickListener(v -> {
            Kendaraan selectedKendaraan = (Kendaraan) spinnerKendaraan.getSelectedItem();
            if (selectedKendaraan != null) {
                // Create order object
                Order order = new Order();
                order.setAlamat(bengkelAddress.getText().toString());
                order.setBengkelId(bengkelId);
                order.setPenggunaId(userId);
                order.setKendaraanId(selectedKendaraan.getKendaraanId());
                order.setStatusPengerjaan(false); // 0 status pengerjaan
                order.setStatusPembayaran(false); // 0 status pembayaran

                OrderController orderController = new OrderController(this);
                // Send order to API
                orderController.createOrder(order, new OrderController.OrderCreateCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(BengkelOrderActivity.this, message, Toast.LENGTH_SHORT).show();
                        UpdateController updateController = new UpdateController();
                        updateController.updateLocation(userId, latitude, longitude, BengkelOrderActivity.this);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(BengkelOrderActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Pilih kendaraan terlebih dahulu.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
