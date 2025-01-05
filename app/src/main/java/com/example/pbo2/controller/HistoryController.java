package com.example.pbo2.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pbo2.R;
import com.example.pbo2.api.ApiService;
import com.example.pbo2.api.RetrofitClient;
import com.example.pbo2.model.Order;
import com.example.pbo2.view.HistoryDetailActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryController {
    private static final String TAG = "HistoryController";
    private Context context;
    private ApiService apiService;
private String role;
    public HistoryController(Context context) {
        this.context = context;
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    public interface HistoryCallback {
        void onSuccess(List<Order> orders);
        void onFailure(String errorMessage);
    }

    public void fetchOrderHistory(int userId, String roleuser, HistoryCallback callback) {
        Call<List<Order>> call = apiService.getOrdersByUserId(userId);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                    role = roleuser;
                } else {
                    callback.onFailure("Gagal memuat riwayat pesanan.");
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                callback.onFailure("Kesalahan jaringan: " + t.getMessage());
                Log.e(TAG, "Network error: " + t.getMessage());
            }
        });
    }

    public void populateHistoryList(LinearLayout container, List<Order> orders) {
        container.removeAllViews();

        if (orders.isEmpty()) {
            TextView emptyMessage = new TextView(context);
            emptyMessage.setText("Tidak ada riwayat pesanan.");
            container.addView(emptyMessage);
            return;
        }

        for (Order order : orders) {
            View historyCard = createHistoryCard(order);
            container.addView(historyCard);
        }
    }

    @SuppressLint("WrongViewCast")
    private View createHistoryCard(Order order) {
        View historyCard = LayoutInflater.from(context).inflate(R.layout.history_pengguna, null, false);
        TextView orderId = historyCard.findViewById(R.id.textview_history);

        orderId.setText("Order ID: " + order.getIdOrder());

        historyCard.setOnClickListener(v -> {
            Intent intent = new Intent(context, HistoryDetailActivity.class);
            intent.putExtra("ORDER_ID",order.getIdOrder());
            intent.putExtra("BENGKEL_ID",order.getBengkelId());
            intent.putExtra("PENGGUNA_ID",order.getPenggunaId());
            intent.putExtra("JADWAL_PERBAIKAN",order.getJadwalPerbaikan());
            intent.putExtra("LAYANAN",order.getLayanan());
            intent.putExtra("REVIEW",order.getReview());
            intent.putExtra("HARGA",order.getTotalHarga());
            intent.putExtra("ROLE",role);
            context.startActivity(intent);
        });
        // Set layout params untuk card
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 32); // Jarak antar card di bagian bawah
        historyCard.setLayoutParams(params);

        return historyCard;
    }
}
