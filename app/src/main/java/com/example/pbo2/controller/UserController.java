package com.example.pbo2.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.pbo2.api.ApiService;
import com.example.pbo2.api.RetrofitClient;
import com.example.pbo2.model.User;
import com.google.gson.Gson;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserController {
    private Context context;
    private LoginCallback loginCallback;
    private RegisterCallback registerCallback;

    public interface LoginCallback {
        void onLoginSuccess(User user);
        void onLoginFailure(String errorMessage);
    }

    public interface RegisterCallback {
        void onRegisterSuccess(String message);
        void onRegisterFailure(String errorMessage);
    }

    public UserController(Context context, LoginCallback loginCallback) {
        this.context = context;
        this.loginCallback = loginCallback;
    }

    public UserController(Context context, RegisterCallback registerCallback) {
        this.context = context;
        this.registerCallback = registerCallback;
    }

    // Fitur Login
    public void login(String phoneNumber, String password) {
        Log.d("LoginRequest", "Phone Number: " + phoneNumber + ", Password: " + password);
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseBody> call = apiService.login(phoneNumber, password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Gson gson = new Gson();
                        User user = gson.fromJson(responseBody, User.class);
                        loginCallback.onLoginSuccess(user);
                        Toast.makeText(context, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        loginCallback.onLoginFailure("Parsing error: " + e.getMessage());
                    }

                } else {
                    loginCallback.onLoginFailure("Login gagal. Periksa kredensial Anda.");
                    Toast.makeText(context, "Login gagal. Periksa kredensial Anda.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loginCallback.onLoginFailure("Gagal menghubungi server.");
                Toast.makeText(context, "Gagal menghubungi server.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fitur Registrasi
    public void register(String name, String phoneNumber, String age, String role, String password,
                         String bengkelName, String bengkelAddress, String bengkelOpen, String profileImagePath,
                         String bengkelImagePath, double latitude, double longitude) {

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody phoneBody = RequestBody.create(MediaType.parse("text/plain"), phoneNumber);
        RequestBody ageBody = RequestBody.create(MediaType.parse("text/plain"), age);
        RequestBody roleBody = RequestBody.create(MediaType.parse("text/plain"), role);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody bengkelNameBody = bengkelName != null ? RequestBody.create(MediaType.parse("text/plain"), bengkelName) : null;
        RequestBody bengkelAddressBody = bengkelAddress != null ? RequestBody.create(MediaType.parse("text/plain"), bengkelAddress) : null;
        RequestBody bengkelOpenBody = bengkelOpen != null ? RequestBody.create(MediaType.parse("text/plain"), bengkelOpen) : null;
        RequestBody latitudeBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude));
        RequestBody longitudeBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude));

        MultipartBody.Part profileImagePart = createMultipartBody("profileImage", profileImagePath);
        MultipartBody.Part bengkelImagePart = createMultipartBody("bengkelImage", bengkelImagePath);

        Call<ResponseBody> call = apiService.register(
                nameBody, phoneBody, ageBody, roleBody, passwordBody,
                profileImagePart, bengkelImagePart, bengkelNameBody,
                bengkelAddressBody, bengkelOpenBody, latitudeBody, longitudeBody
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    registerCallback.onRegisterSuccess("Registrasi berhasil!");
                } else {
                    registerCallback.onRegisterFailure("Registrasi gagal. Coba lagi.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                registerCallback.onRegisterFailure("Gagal menghubungi server: " + t.getMessage());
            }
        });
    }

    private MultipartBody.Part createMultipartBody(String partName, String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists()) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
                return MultipartBody.Part.createFormData(partName, file.getName(), fileBody);
            }
        }
        return null;
    }
}
