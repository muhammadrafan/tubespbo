package com.example.pbo2.api;

import com.example.pbo2.model.Bengkel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {


    @POST("login")
    Call<ResponseBody> login(
            @Query("phoneNumber") String phoneNumber,
            @Query("password") String password
    );

    @Multipart
    @POST("register")
    Call<ResponseBody> registerBengkel(
            @Part("name") RequestBody name,
            @Part("phoneNumber") RequestBody phoneNumber,
            @Part("age") RequestBody age,
            @Part("role") RequestBody role,
            @Part("password") RequestBody password,
            @Part("bengkelName") RequestBody bengkelName,
            @Part("bengkelAddress") RequestBody bengkelAddress,
            @Part("bengkelOpen") RequestBody bengkelOpen,
            @Part MultipartBody.Part bengkelImage,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude
    );
}

