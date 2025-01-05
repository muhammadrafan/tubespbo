package com.example.pbo2.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationController {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Activity activity;
    private FusedLocationProviderClient fusedLocationClient;

    public interface LocationCallback {
        void onLocationRetrieved(double latitude, double longitude);
        void onError(String errorMessage);
    }

    public LocationController(Activity activity) {
        this.activity = activity;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
    }
    public void checkAndRequestLocationPermission(LocationCallback callback) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocation(callback); // Kirim callback jika ada
        }
    }

    public void requestLocation(LocationCallback callback) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocation(callback);
        }
    }

    public void getLocation(LocationCallback callback) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            // Panggil callback jika tidak null
                            if (callback != null) {
                                callback.onLocationRetrieved(latitude, longitude);
                            } else {
                                Toast.makeText(activity, "Lokasi berhasil diperoleh: " + latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (callback != null) {
                                callback.onError("Gagal mendapatkan lokasi.");
                            } else {
                                Toast.makeText(activity, "Gagal mendapatkan lokasi.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            if (callback != null) {
                callback.onError("Izin lokasi belum diberikan.");
            } else {
                Toast.makeText(activity, "Izin lokasi belum diberikan.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void fetchLocationAndOpenMaps(double targetLatitude, double targetLongitude) {
        requestLocation(new LocationCallback() {
            @Override
            public void onLocationRetrieved(double currentLatitude, double currentLongitude) {
                // Format URI untuk Google Maps
                String uri = "http://maps.google.com/maps?saddr=" + currentLatitude + "," + currentLongitude
                        + "&daddr=" + targetLatitude + "," + targetLongitude;

                // Intent untuk membuka Google Maps
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");

                // Periksa apakah Google Maps tersedia
                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "Google Maps tidak tersedia.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
