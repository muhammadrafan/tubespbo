package com.example.pbo2.view;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pbo2.R;
import com.example.pbo2.controller.HistoryController;
import com.example.pbo2.controller.NavigationController;
import com.example.pbo2.model.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private int userId;
    private String role;
    private LinearLayout historyContainer;
    private HistoryController historyController;
    private NavigationController navigationController;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", -1);
        role = sharedPreferences.getString("ROLE", "");
        setContentView(R.layout.history_user_activity);

        historyContainer = findViewById(R.id.history_container);
        historyController = new HistoryController(this);

        // Fetch user order history
        fetchOrderHistory();

        // Handle bottom navigation
        navigationController = new NavigationController(this);
        BottomNavigationView navigationView = findViewById(R.id.navigation_bottom);
        navigationView.setSelectedItemId(R.id.history);
        navigationView.setOnItemSelectedListener(item -> {
            // Your navigation handling logic
            return navigationController.handleNavigationItemSelected(item.getItemId());
        });
    }

    private void fetchOrderHistory() {
        historyController.fetchOrderHistory(userId, role,  new HistoryController.HistoryCallback() {
            @Override
            public void onSuccess(List<Order> orders) {
                historyController.populateHistoryList(historyContainer, orders);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(HistoryActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("HistoryActivity", "Error fetching history: " + errorMessage);
            }
        });
    }
}
