package com.example.pbo2.model;

public class Pengguna extends User {
    private double latitude;  // Latitude lokasi pengguna
    private double longitude; // Longitude lokasi pengguna

    // Konstruktor
    public Pengguna(int id, String name, String phoneNumber, String age, String role, String password, String imageUser, double latitude, double longitude) {
        super(id, name, phoneNumber, age, role, password, imageUser); // Panggil konstruktor User
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getter dan Setter untuk latitude
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    // Getter dan Setter untuk longitude
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
