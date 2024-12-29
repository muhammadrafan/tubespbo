package com.example.pbo2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pbo2.R;
import com.example.pbo2.controller.AccountController;
import com.example.pbo2.controller.NavigationController;
import com.example.pbo2.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountActivity extends AppCompatActivity {
    private LinearLayout alamatBengkelSection, tambahKendaraanSection,updateAccout,changePassword;
    private NavigationController navigationController;
    private AccountController accountController;
    private ImageView imageProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        setContentView(R.layout.menu_usser);

        updateAccout = findViewById(R.id.informasi_akun);
        alamatBengkelSection = findViewById(R.id.alamat_bengkel);
        tambahKendaraanSection = findViewById(R.id.tambah_kendaraan);
        imageProfile = findViewById(R.id.image_profile);
        changePassword = findViewById(R.id.password);

        String role = sharedPreferences.getString("ROLE", "");
        if ("Pengguna".equalsIgnoreCase(role)) {
            alamatBengkelSection.setVisibility(View.GONE);
        } else if ("Bengkel".equalsIgnoreCase(role)) {
            tambahKendaraanSection.setVisibility(View.GONE);
        }

        navigationController = new NavigationController(this);
        accountController = new AccountController(this);

        int userId = sharedPreferences.getInt("USER_ID", -1);
        if (userId != -1) {
            accountController.fetchUserData(userId, new AccountController.UserDataCallback() {
                @Override
                public void onSuccess(User user) {
                    if (user.getImageUser() != null) {
                        accountController.loadProfileImage(user.getImageUser(), imageProfile);
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(AccountActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }

        updateAccout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewProfileActivity.class));
            }
        });
        changePassword.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
            }
        }));

        tambahKendaraanSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        LinearLayout logoutSection = findViewById(R.id.logout);
        logoutSection.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        BottomNavigationView navigationView = findViewById(R.id.navigation_bottom);
        navigationView.setSelectedItemId(R.id.account);
        navigationView.setOnItemSelectedListener(item -> {
            return navigationController.handleNavigationItemSelected(item.getItemId());
        });
    }
}
