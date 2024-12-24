package com.example.pbo2.model;

public class Bengkel {
    private int userId;
    private String bengkelName;
    private String bengkelAddress;
    private String bengkelOpen;
    private String bengkelImage; // Base64 string

    // Getter dan Setter
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBengkelName() {
        return bengkelName;
    }

    public void setBengkelName(String bengkelName) {
        this.bengkelName = bengkelName;
    }

    public String getBengkelAddress() {
        return bengkelAddress;
    }

    public void setBengkelAddress(String bengkelAddress) {
        this.bengkelAddress = bengkelAddress;
    }

    public String getBengkelOpen() {
        return bengkelOpen;
    }

    public void setBengkelOpen(String bengkelOpen) {
        this.bengkelOpen = bengkelOpen;
    }

    public String getBengkelImage() {
        return bengkelImage;
    }

    public void setBengkelImage(String bengkelImage) {
        this.bengkelImage = bengkelImage;
    }
}
