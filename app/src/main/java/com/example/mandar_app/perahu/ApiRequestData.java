package com.example.mandar_app.perahu;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiRequestData {
    @GET("retrieveperahu")
    Call<ModelResponse> ardRetrieveAllData();
}
