package com.example.mandar_app.loginregist;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroServer {
//    private static final String BASE_URL = "http://192.168.1.7/mandarapp/users/";
private static final String BASE_URL = "https://kudmandar.site/api/";

    //    private static final String BASE_URL = "http://192.168.152.121/mandarapp/users/";
//private static final String BASE_URL = "http://192.168.1.65/mandarapp/users/";
    private static Retrofit retrofit;

    public static Retrofit getRetroServer() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
