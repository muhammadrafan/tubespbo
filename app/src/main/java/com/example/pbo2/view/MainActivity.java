package com.example.pbo2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pbo2.R;
import com.example.pbo2.controller.BengkelController;
import com.example.pbo2.controller.LocationController;
import com.example.pbo2.controller.NavigationController;
import com.example.pbo2.controller.OrderController;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private TextView kota,greeting,tulisanbengkel;
    private LocationController locationController;
    private NavigationController navigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        int userId = sharedPreferences.getInt("USER_ID", -1);
        String name = sharedPreferences.getString("USER_NAME","");
        String role = sharedPreferences.getString("ROLE", "");
        greeting = findViewById(R.id.tv_greeting);
        kota = findViewById(R.id.kota);
        tulisanbengkel = findViewById(R.id.tulisanbengkel);
        locationController = new LocationController(this);
        locationController.checkAndRequestLocationPermission(new LocationController.LocationCallback() {
            @Override
            public void onLocationRetrieved(double latitude, double longitude) {
                // Panggil showCityName dengan data yang diperoleh
                showCityNameFromCoordinates(latitude, longitude);
            }

            @Override
            public void onError(String errorMessage) {
                // Tampilkan error jika lokasi gagal diperoleh
                showToast(errorMessage);
            }
        });
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);
        if (!isLoggedIn) {
            // Redirect ke LoginActivity jika belum login
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        greeting.setText("Hi, " + name);

        LinearLayout bengkelContainer = findViewById(R.id.bengkel_container);
        if ("Pengguna".equalsIgnoreCase(role)) {
            BengkelController bengkelController = new BengkelController(this);
            bengkelController.fetchBengkelData(bengkelContainer);
        } else if ("Bengkel".equalsIgnoreCase(role)) {
            tulisanbengkel.setText("Orderan");
            OrderController orderController = new OrderController(this);
            orderController.fetchOrdersByBengkel(userId, bengkelContainer);
        }


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
    private void showCityNameFromCoordinates(double latitude, double longitude) {
        android.location.Geocoder geocoder = new android.location.Geocoder(this, java.util.Locale.getDefault());
        try {
            android.location.Address address = geocoder.getFromLocation(latitude, longitude, 1).get(0);
            String city = address.getLocality();
            String country = address.getCountryName();

            // Gunakan metode showCityName untuk menampilkan kota dan negara
            showCityName(city, country);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            showToast("Gagal mendapatkan nama kota: " + e.getMessage());
        }
    }
    public void showCityName(String city, String country) {
        kota.setText(city);
        showToast("Kota: " + city + ", Negara: " + country);
    }
}
