package com.example.pbo2.controller;

import static android.content.ContentValues.TAG;

import com.example.pbo2.api.ApiService;
import com.example.pbo2.api.RetrofitClient;
import com.example.pbo2.controller.OrderController.OrderResponse;
import com.example.pbo2.model.Order;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pbo2.R;
import com.example.pbo2.api.ApiService;
import com.example.pbo2.api.RetrofitClient;
import com.example.pbo2.model.Bengkel;
import com.example.pbo2.model.User;
import com.example.pbo2.view.BengkelOrderActivity;
import com.example.pbo2.view.BengkelPeggunaActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderController {
    private Context context;
    public OrderController(Context context) {
        this.context = context;
    }
    // Callback interface untuk menangani respons dari server
    public interface OrderCreateCallback {
        void onSuccess(String message);
        void onError(String errorMessage);
    }

    // Method untuk membuat order
    public void createOrder(Order order, OrderCreateCallback callback) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<OrderResponse> call = apiService.createOrder(order);  // Mengirim order ke server

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OrderResponse orderResponse = response.body();
                    if ("success".equals(orderResponse.status)) {
                        callback.onSuccess(orderResponse.message); // Jika berhasil
                    } else {
                        callback.onError(orderResponse.message); // Jika gagal
                    }
                } else {
                    callback.onError(response.body().message);
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                callback.onError("Kesalahan jaringan: " + t.getMessage());
            }
        });
    }


    // Method untuk mengambil order berdasarkan bengkel_id
    public void fetchOrdersByBengkel(int bengkelId, LinearLayout container) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Call<List<Order>> call = apiService.getOrdersByBengkel(bengkelId);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orders = response.body();

                    // Bersihkan container sebelum menambahkan card baru
                    container.removeAllViews();

                    // Iterasi data order
                    for (Order order : orders) {
                        if (!order.getStatusPengerjaan()) { // Jika status pengerjaan belum selesai
                            fetchUserAndCreateOrderCard(order, container);
                        }
                    }
                } else {
                    Toast.makeText(context, "Gagal memuat data order.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(context, "Kesalahan Manggil order " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method untuk mencari nama pelanggan berdasarkan penggunaId, lalu membuat card order
    private void fetchUserAndCreateOrderCard(Order order, LinearLayout container) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Call<User> call = apiService.getUserById(order.getPenggunaId());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();

                    // Buat card order
                    View orderCard = createOrderCard(order, user);
                    container.addView(orderCard);
                } else {
                    Toast.makeText(context, "Gagal memuat data pengguna.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "Kesalahan jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method untuk membuat tampilan card order
    private View createOrderCard(Order order, User pelanggan) {
        View orderCard = LayoutInflater.from(context).inflate(R.layout.order_bengkel, null, false);

        // Inisialisasi elemen-elemen di dalam card_order.xml
        int orderId = order.getIdOrder();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView namaPelangganView = orderCard.findViewById(R.id.textview_pelanggan_order_bengkel);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView image = orderCard.findViewById(R.id.image_pelanggan_order_bengkel);


        // Set data ke elemen
        namaPelangganView.setText(pelanggan.getName());
        if (pelanggan.getImageUser() != null && !pelanggan.getImageUser().isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(pelanggan.getImageUser(), Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                image.setImageBitmap(decodedImage);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Error decoding image: " + e.getMessage());
                image.setImageResource(R.drawable.error_image); // Placeholder jika decoding gagal
            }
        } else {
            image.setImageResource(R.drawable.error_image); // Placeholder jika tidak ada gambar
        }

        orderCard.setOnClickListener(v -> {
            Intent intent = new Intent(context, BengkelPeggunaActivity.class);
            intent.putExtra("ORDER_ID", orderId); // Kirim ID Order
            intent.putExtra("PENGGUNA_ID", order.getPenggunaId());
            intent.putExtra("PENGGUNA_NAME", pelanggan.getName());
            context.startActivity(intent);
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(0, 0, 0, 32); // Jarak antar card di bagian bawah
        orderCard.setLayoutParams(params);

        return orderCard;
    }



    // Class untuk menangani respons server setelah membuat order
    public static class OrderResponse {
        public String status;
        public String message;
        public int order_id;
    }
}
