package com.example.mandar_app.production;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroServer {
//    private static final String baseURL = "http://192.168.1.7/mandarapp/produksi/";
private static final String baseURL = "https://kudmandar.site/api/";
//    private static final String baseURL = "http://192.168.1.65/mandarapp/produksi/";
    private static Retrofit retro;

    public static Retrofit konekRetrofit(){
        if (retro == null){
            // Membuat instance Gson dengan setLenient()
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retro = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create(gson))  // Menggunakan Gson dengan lenient mode
                    .build();
        }
        return retro;
    }
}
