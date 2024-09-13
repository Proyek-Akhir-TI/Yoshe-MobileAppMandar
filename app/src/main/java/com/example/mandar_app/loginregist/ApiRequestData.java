package com.example.mandar_app.loginregist;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiRequestData {
    @FormUrlEncoded
    @POST("register")
    Call<ApiResponse> register(
            @Field("name") String name,
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password,
            @Field("kusuka") String kusuka // Tambahkan kusuka
    );

    @FormUrlEncoded
    @POST("login")
    Call<ApiResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );
}
