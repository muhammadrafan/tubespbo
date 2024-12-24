package com.example.pbo2.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pbo2.R;
import com.example.pbo2.api.ApiService;
import com.example.pbo2.api.RetrofitClient;
import com.example.pbo2.controller.LocationController;
import com.example.pbo2.model.Bengkel;

import android.content.Intent;
import android.content.SharedPreferences;
import com.bumptech.glide.Glide;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView kota;
    private LocationController locationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        kota = findViewById(R.id.kota);
        locationController = new LocationController(this);
        locationController.checkAndRequestLocationPermission();
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);
        if (!isLoggedIn) {
            // Redirect ke LoginActivity jika belum login
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

    }
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showCityName(String city, String country) {
        kota.setText(city);
        showToast("Kota: " + city + ", Negara: " + country);
    }
}
