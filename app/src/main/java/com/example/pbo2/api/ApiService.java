package com.example.pbo2.api;

import com.example.pbo2.model.Bengkel;
import com.example.pbo2.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("getBengkel")
    Call<List<Bengkel>> getBengkelList();

    @POST("login")
    Call<ResponseBody> login(
            @Query("phoneNumber") String phoneNumber,
            @Query("password") String password
    );

    @Multipart
    @POST("register")
    Call<ResponseBody> register(
            @Part("name") RequestBody name,
            @Part("phoneNumber") RequestBody phoneNumber,
            @Part("age") RequestBody age,
            @Part("role") RequestBody role,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part profileImage,
            @Part("bengkelName") RequestBody bengkelName, // Opsional
            @Part("bengkelAddress") RequestBody bengkelAddress, // Opsional
            @Part("bengkelOpen") RequestBody bengkelOpen, // Opsional
            @Part("latitude") RequestBody latitude, // Opsional
            @Part("longitude") RequestBody longitude // Opsional
    );


    @Multipart
    @POST("user/update")
    Call<ResponseBody> updateAccount(
            @Query("id") int id,
            @Query("name") String name,
            @Query("phone") String phone,
            @Query("age") int age,
            @Part MultipartBody.Part image // Gambar (opsional)
    );
    @POST("user/change-password")
    Call<ResponseBody> changePassword(
            @Query("id") int id,
            @Query("oldPassword") String oldPassword,
            @Query("newPassword") String newPassword
    );



    // Get user by ID
    @GET("user/{id}")
    Call<User> getUserById(@Path("id") int userId);
}

