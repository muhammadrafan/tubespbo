package com.example.pbo2.model;

public class Bengkel extends User {
    private int user_id; // Kolom user_id untuk menyimpan ID pengguna yang terkait
    private String bengkel_name;
    private String bengkel_address;
    private String bengkel_open;
    private String latitude;
    private String longitude;
    private String bengkel_image;

    // Konstruktor
    public Bengkel(int id, String name, String phoneNumber, String age, String role, String password, String imageUser,
                   int user_id, String bengkel_name, String bengkel_address, String bengkel_open,
                   String latitude, String longitude, String bengkel_image) {
        super(id, name, phoneNumber, age, role, password,imageUser); // Panggil konstruktor User
        this.user_id = user_id;
        this.bengkel_name = bengkel_name;
        this.bengkel_address = bengkel_address;
        this.bengkel_open = bengkel_open;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bengkel_image = bengkel_image;
    }

    // Getter dan Setter untuk user_id
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    // Getter dan Setter untuk atribut Bengkel
    public String getBengkelName() {
        return bengkel_name;
    }

    public void setBengkelName(String bengkel_name) {
        this.bengkel_name = bengkel_name;
    }

    public String getBengkelAddress() {
        return bengkel_address;
    }

    public void setBengkelAddress(String bengkel_address) {
        this.bengkel_address = bengkel_address;
    }

    public String getBengkelOpen() {
        return bengkel_open;
    }

    public void setBengkelOpen(String bengkel_open) {
        this.bengkel_open = bengkel_open;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getBengkelImage() {
        return bengkel_image;
    }

    public void setBengkelImage(String bengkel_image) {
        this.bengkel_image = bengkel_image;
    }
}
