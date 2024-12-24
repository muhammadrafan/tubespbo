package com.example.pbo2.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pbo2.view.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationController {
    private MainActivity mainActivity;
    private FusedLocationProviderClient fusedLocationClient;

    public LocationController(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivity);
    }

    public void checkAndRequestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mainActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getLocation();
        }
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(mainActivity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();

                                // Panggil fungsi untuk mendapatkan kota dari koordinat
                                getCityName(latitude, longitude);
                            } else {
                                mainActivity.showToast("Gagal mendapatkan lokasi");
                            }
                        }
                    });
        }
    }

    private void getCityName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(mainActivity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String city = addresses.get(0).getLocality();
                String country = addresses.get(0).getCountryName();
                mainActivity.showCityName(city, country);
            } else {
                mainActivity.showToast("Gagal mendapatkan nama kota");
            }
        } catch (IOException e) {
            e.printStackTrace();
            mainActivity.showToast("Error: " + e.getMessage());
        }
    }
}
