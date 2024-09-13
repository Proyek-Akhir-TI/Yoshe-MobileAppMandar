package com.example.mandar_app.kelompok;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiRequestData {
    @GET("kelompok")
    Call<ModelResponse> ardRetrieveAllData();
}
