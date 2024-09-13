package com.example.mandar_app.nelayan;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroServer {
//    private static final String baseURL = "http://192.168.1.7/mandarapp/nelayan/";
//private static final String baseURL = "http://192.168.1.65/mandarapp/nelayan/";
private static final String baseURL = "https://kudmandar.site/api/";
    private static Retrofit retro;

    public static Retrofit konekRetrofit(){
        if (retro == null){
            retro = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retro;
    }
}
