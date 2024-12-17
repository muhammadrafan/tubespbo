package com.example.pbo2.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.pbo2.model.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class UserController {
    private Context context;
    private LoginCallback loginCallback;
    private RegisterCallback registerCallback;

    // Interface untuk callback login
    public interface LoginCallback {
        void onLoginSuccess(User user);
        void onLoginFailure(String errorMessage);
    }
    public interface RegisterCallback {
        void onRegisterSuccess(int userId, String name, String phoneNumber, int age, String role);
        void onRegisterFailure(String errorMessage);
    }

    //Login Constructor

    public UserController(Context context, LoginCallback loginCallback) {
        this.context = context;
        this.loginCallback = loginCallback;
    }
    public UserController(Context context, RegisterCallback registerCallback) {
        this.context = context;
        this.registerCallback = registerCallback;
    }

    // Method login dengan koneksi backend
    public void login(String phoneNumber, String password) {
        new LoginTask().execute(phoneNumber, password);
    }

    // AsyncTask untuk login
    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String phoneNumber = params[0];
            String password = params[1];

            try {
                URL url = new URL("http://10.0.2.2:8080/MYBengkelBackend/login");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Konfigurasi koneksi
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);

                // Parameter login
                String postData = "phoneNumber=" + phoneNumber + "&password=" + password;

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = postData.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Baca response
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    return response.toString();
                } else {
                    return "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("error")) {
                try {
                    // Parse JSON response
                    Gson gson = new Gson();
                    LoginResponse loginResponse = gson.fromJson(result, LoginResponse.class);

                    if ("success".equals(loginResponse.status)) {
                        // Buat objek User dari response
                        User user = new User(
                                loginResponse.user_id,
                                loginResponse.name,
                                loginResponse.phoneNumber,
                                loginResponse.age,
                                loginResponse.role,
                                "" // password tidak perlu disimpan di client
                        );

                        // Panggil callback success
                        loginCallback.onLoginSuccess(user);

                        // Tampilkan toast
                        Toast.makeText(context, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Panggil callback failure
                        loginCallback.onLoginFailure(loginResponse.message);

                        // Tampilkan toast error
                        Toast.makeText(context, loginResponse.message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    loginCallback.onLoginFailure("Parsing Error");
                    Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                }
            } else {
                loginCallback.onLoginFailure("Koneksi Gagal");
                Toast.makeText(context, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Inner class untuk parsing JSON response
    private static class LoginResponse {
        String status;
        String message;
        int user_id;
        String name;
        String phoneNumber;
        int age;
        String role;
    }
    public void register(String name, String phoneNumber, int age, String role, String password) {
        new RegisterTask().execute(name, phoneNumber, String.valueOf(age), role, password);
    }

    private class RegisterTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String phoneNumber = params[1];
            String age = params[2];
            String role = params[3];
            String password = params[4];

            try {
                URL url = new URL("http://10.0.2.2:8080/MYBengkelBackend/register");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Konfigurasi koneksi
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);

                // Parameter register
                String postData = "name=" + name + "&phoneNumber=" + phoneNumber + "&age=" + age + "&role=" + role + "&password=" + password;

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = postData.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Baca response
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    return response.toString();
                } else {
                    return "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("error")) {
                try {
                    // Parse JSON response
                    Gson gson = new Gson();
                    RegisterResponse registerResponse = gson.fromJson(result, RegisterResponse.class);

                    if ("success".equals(registerResponse.status)) {
                        // Panggil callback success
                        registerCallback.onRegisterSuccess(registerResponse.user_id, registerResponse.name, registerResponse.phoneNumber, registerResponse.age, registerResponse.role);

                        // Tampilkan toast
                        Toast.makeText(context, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Panggil callback failure
                        registerCallback.onRegisterFailure(registerResponse.message);

                        // Tampilkan toast error
                        Toast.makeText(context, registerResponse.message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    registerCallback.onRegisterFailure("Parsing Error");
                    Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                }
            } else {
                registerCallback.onRegisterFailure("Koneksi Gagal");
                Toast.makeText(context, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static class RegisterResponse {
        String status;
        String message;
        int user_id;
        String name;
        String phoneNumber;
        int age;
        String role;
    }
}

