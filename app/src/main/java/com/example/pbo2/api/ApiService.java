package com.example.pbo2.api;

import com.example.pbo2.controller.OrderController;
import com.example.pbo2.model.Bengkel;
import com.example.pbo2.model.Kendaraan;
import com.example.pbo2.model.Order;
import com.example.pbo2.model.Pengguna;
import com.example.pbo2.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
            @Part MultipartBody.Part profileImage, // Gambar profil pengguna
            @Part MultipartBody.Part bengkelImage, // Gambar bengkel (opsional)
            @Part("bengkelName") RequestBody bengkelName, // Opsional
            @Part("bengkelAddress") RequestBody bengkelAddress, // Opsional
            @Part("bengkelOpen") RequestBody bengkelOpen, // Opsional
            @Part("latitude") RequestBody latitude, // Opsional
            @Part("longitude") RequestBody longitude // Opsional
    );

    @POST("updateLocation")
    Call<ResponseBody> updateLocation(
            @Query("userId") int userId,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude
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
    @Multipart
    @POST("updateBengkel")
    Call<ResponseBody> updateBengkel(
            @Part("user_id") RequestBody userId,
            @Part("bengkel_name") RequestBody bengkelName,
            @Part("bengkel_address") RequestBody bengkelAddress,
            @Part("bengkel_open") RequestBody bengkelOpen,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part MultipartBody.Part bengkelImage // Gambar bengkel (opsional)
    );
    @Multipart
    @POST("registerVehicle")
    Call<Void> saveKendaraan(
            @Part("user_id") RequestBody userId,
            @Part("merek") RequestBody merek,
            @Part("model") RequestBody model,
            @Part("tahun") RequestBody tahun,
            @Part("isDamagged") RequestBody isDamagged, // Nama kolom sesuai tabel
            @Part MultipartBody.Part vehicleImage
    );

    @GET("bengkel/getDetail")
    Call<Bengkel> getBengkelDetail(@Query("bengkelId") int bengkelId);

    @GET("kendaraan/getList")
    Call<List<Kendaraan>> getKendaraanList(@Query("userId") int userId);
    @POST("order/create")
    Call<OrderController.OrderResponse> createOrder(@Body Order order);
    // Ambil semua order berdasarkan bengkel_id
    @GET("fetchOrdersByBengkel")
    Call<List<Order>> getOrdersByBengkel(@Query("bengkel_id") int bengkelId);
    // Get user by ID
    @GET("user/{id}")
    Call<User> getUserById(@Path("id") int userId);
    @GET("user_pengguna/{id}")
    Call<Pengguna> getPenggunaById(@Path("id") int userId);
    @FormUrlEncoded
    @POST("updateOrder")
    Call<Void> updateOrder(
            @Field("id_order") int idOrder,
            @Field("status_pengerjaan") boolean statusPengerjaan,
            @Field("status_pembayaran") boolean statusPembayaran,
            @Field("jadwal_perbaikan") String jadwalPerbaikan,
            @Field("layanan") String layanan,
            @Field("total_harga") double totalHarga
    );
    @GET("getOrdersByUserId")
    Call<List<Order>> getOrdersByUserId(@Query("userId") int userId);

    // Mengirimkan review untuk order
    @FormUrlEncoded
    @POST("submitReview")
    Call<Void> submitOrderReview(
            @Field("id_order") int idOrder,
            @Field("review") int review
    );
}

