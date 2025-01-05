package com.example.pbo2.model;

import java.io.Serializable;

public class Order implements Serializable {
    private int idOrder;
    private String alamat;
    private String jadwalPerbaikan;
    private String layanan;
    private boolean statusPengerjaan;
    private boolean statusPembayaran;
    private double totalHarga;
    private int review;
    private int penggunaId;
    private int bengkelId;
    private int kendaraanId;

    public Order(int idOrder,String alamat, String jadwalPerbaikan, String layanan, boolean statusPengerjaan, boolean statusPembayaran, double totalHarga, int review, int penggunaId, int bengkelId, int kendaraanId){
        this.idOrder = idOrder;
        this.alamat = alamat;
        this.jadwalPerbaikan = jadwalPerbaikan;
        this.layanan = layanan;
        this.statusPengerjaan = statusPengerjaan;
        this.statusPembayaran = statusPembayaran;
        this.totalHarga = totalHarga;
        this.review = review;
        this.penggunaId = penggunaId;
        this.bengkelId = bengkelId;
        this.kendaraanId = kendaraanId;
    }
    public Order(){
        
    }

    // Getters and Setters
    public int getIdOrder() { return idOrder; }
    public void setIdOrder(int idOrder) { this.idOrder = idOrder; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getJadwalPerbaikan() { return jadwalPerbaikan; }
    public void setJadwalPerbaikan(String jadwalPerbaikan) { this.jadwalPerbaikan = jadwalPerbaikan; }

    public String getLayanan() { return layanan; }
    public void setLayanan(String layanan) { this.layanan = layanan; }

    public boolean getStatusPengerjaan() { return statusPengerjaan; }
    public void setStatusPengerjaan(boolean statusPengerjaan) { this.statusPengerjaan = statusPengerjaan; }

    public boolean getStatusPembayaran() { return statusPembayaran; }
    public void setStatusPembayaran(boolean statusPembayaran) { this.statusPembayaran = statusPembayaran; }

    public double getTotalHarga() { return totalHarga; }
    public void setTotalHarga(int totalHarga) { this.totalHarga = totalHarga; }

    public int getReview() { return review; }
    public void setReview(int review) { this.review = review; }

    public int getPenggunaId() { return penggunaId; }
    public void setPenggunaId(int penggunaId) { this.penggunaId = penggunaId; }

    public int getBengkelId() { return bengkelId; }
    public void setBengkelId(int bengkelId) { this.bengkelId = bengkelId; }

    public int getKendaraanId() { return kendaraanId; }
    public void setKendaraanId(int kendaraanId) { this.kendaraanId = kendaraanId; }
}
