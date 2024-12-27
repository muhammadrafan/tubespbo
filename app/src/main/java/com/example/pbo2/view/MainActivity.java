package com.example.pbo2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pbo2.R;
import com.example.pbo2.controller.BengkelController;
import com.example.pbo2.controller.LocationController;
import com.example.pbo2.controller.NavigationController;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private TextView kota;
    private LocationController locationController;
    private NavigationController navigationController;

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

        LinearLayout bengkelContainer = findViewById(R.id.bengkel_container);
        BengkelController bengkelController = new BengkelController(this);
        bengkelController.fetchBengkelData(bengkelContainer);

        // Inisialisasi NavigationController
        navigationController = new NavigationController(this);

        // Inisialisasi BottomNavigationView
        BottomNavigationView navigationView = findViewById(R.id.navigation_bottom);
        navigationView.setOnItemSelectedListener(item -> {
            return navigationController.handleNavigationItemSelected(item.getItemId());
        });
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showCityName(String city, String country) {
        kota.setText(city);
        showToast("Kota: " + city + ", Negara: " + country);
    }
}
