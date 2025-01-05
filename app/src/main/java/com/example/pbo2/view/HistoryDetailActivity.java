package com.example.pbo2.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pbo2.R;
import com.example.pbo2.controller.UpdateController;

public class HistoryDetailActivity extends AppCompatActivity {
    private int orderId,bengkelId,pelangganId,review;
    private String jadwal,layanan,role;
    private double harga;
    private UpdateController updateController;
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.card_history);
        TextView BengkelId = findViewById(R.id.card_history_pelanggan);
        TextView PelangganId = findViewById(R.id.card_history_bengkel);
        TextView Jadwal = findViewById(R.id.isi_Jadwal_history);
        TextView Layanan = findViewById(R.id.isi_layanan_history);
        TextView Review = findViewById(R.id.isi_riview_history);
        TextView Harga = findViewById(R.id.isi_total_history);
        LinearLayout sectionReview = findViewById(R.id.history_review);
        EditText reviewInput = findViewById(R.id.beririvew_history);
        Button submitReviewButton = findViewById(R.id.btn_open_maps);
        updateController = new UpdateController();

        orderId = getIntent().getIntExtra("ORDER_ID",-1);
        bengkelId = getIntent().getIntExtra("BENGKEL_ID",-1);
        pelangganId = getIntent().getIntExtra("PENGGUNA_ID",-1);
        review = getIntent().getIntExtra("REVIEW",-1);
        jadwal = getIntent().getStringExtra("JADWAL_PERBAIKAN");
        layanan = getIntent().getStringExtra("LAYANAN");
        role = getIntent().getStringExtra("ROLE");
        harga = getIntent().getDoubleExtra("HARGA",-1);
        if("Bengkel".equals(role)){
            sectionReview.setVisibility(View.GONE);
        }

        BengkelId.setText(String.valueOf(bengkelId));
       PelangganId.setText(String.valueOf(pelangganId));
        Jadwal.setText(jadwal);
        Layanan.setText(layanan);
        Harga.setText(String.valueOf(harga));
        Review.setText(String.valueOf(review));
        submitReviewButton.setOnClickListener(v -> {
            String newReviewText = reviewInput.getText().toString().trim();
            if (!newReviewText.isEmpty()) {
                try {
                    int newReview = Integer.parseInt(newReviewText);
                    // Update review via UpdateController
                    updateController.updateReview(orderId, newReview, HistoryDetailActivity.this);
                } catch (NumberFormatException e) {
                    Toast.makeText(HistoryDetailActivity.this, "Masukkan nilai review yang valid", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(HistoryDetailActivity.this, "Review tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
