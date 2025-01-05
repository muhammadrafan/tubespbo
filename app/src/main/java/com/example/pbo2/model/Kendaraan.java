package com.example.pbo2.model;

public class Kendaraan {
    private int kendaraanId; // Kolom kendaraan_id
    private int userId;      // Kolom user_id
    private String fotoKendaraan; // Kolom foto_kendaraan
    private String merek;    // Kolom merek
    private String model;    // Kolom model
    private int tahun;       // Kolom tahun
    private boolean isDamaged; // Kolom isDamaged

    // Constructor kosong
    public Kendaraan() {}

    // Constructor dengan semua parameter
    public Kendaraan(int kendaraanId, int userId, String fotoKendaraan, String merek, String model, int tahun, boolean isDamaged) {
        this.kendaraanId = kendaraanId;
        this.userId = userId;
        this.fotoKendaraan = fotoKendaraan;
        this.merek = merek;
        this.model = model;
        this.tahun = tahun;
        this.isDamaged = isDamaged;
    }

    // Getters and Setters
    public int getKendaraanId() { return kendaraanId; }
    public void setKendaraanId(int kendaraanId) { this.kendaraanId = kendaraanId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFotoKendaraan() { return fotoKendaraan; }
    public void setFotoKendaraan(String fotoKendaraan) { this.fotoKendaraan = fotoKendaraan; }

    public String getMerek() { return merek; }
    public void setMerek(String merek) { this.merek = merek; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getTahun() { return tahun; }
    public void setTahun(int tahun) { this.tahun = tahun; }

    public boolean isDamaged() { return isDamaged; }
    public void setDamaged(boolean isDamaged) { this.isDamaged = isDamaged; }

    @Override
    public String toString() {
        return "Kendaraan{" +
                "kendaraanId=" + kendaraanId +
                ", userId=" + userId +
                ", fotoKendaraan='" + fotoKendaraan + '\'' +
                ", merek='" + merek + '\'' +
                ", model='" + model + '\'' +
                ", tahun=" + tahun +
                ", isDamaged=" + isDamaged +
                '}';
    }
}
