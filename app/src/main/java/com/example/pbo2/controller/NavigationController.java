package com.example.pbo2.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.pbo2.R;
import com.example.pbo2.view.AccountActivity;
import com.example.pbo2.view.MainActivity;

public class NavigationController {
    private Context context;

    public NavigationController(Context context) {
        this.context = context;
    }

    public boolean handleNavigationItemSelected(int itemId) {
        // Dapatkan aktivitas saat ini
        Activity currentActivity = (Activity) context;

        if (itemId == R.id.home) {
            // Cek apakah pengguna sudah berada di MainActivity
            if (!(currentActivity instanceof MainActivity)) {
                Intent homeIntent = new Intent(context, MainActivity.class);
                context.startActivity(homeIntent);
                currentActivity.overridePendingTransition(0, 0); // Animasi tanpa transisi
                currentActivity.finish(); // Tutup aktivitas sebelumnya
            }
            return true;
        } else if (itemId == R.id.history) {
            // Tambahkan logika jika halaman history ditambahkan nanti
            return true;
        } else if (itemId == R.id.order) {
            // Tambahkan logika jika halaman order ditambahkan nanti
            return true;
        } else if (itemId == R.id.account) {
            // Cek apakah pengguna sudah berada di AccountActivity
            if (!(currentActivity instanceof AccountActivity)) {
                Intent accountIntent = new Intent(context, AccountActivity.class);
                context.startActivity(accountIntent);
                currentActivity.overridePendingTransition(0, 0); // Animasi tanpa transisi
                currentActivity.finish(); // Tutup aktivitas sebelumnya
            }
            return true;
        } else {
            return false;
        }
    }
}
