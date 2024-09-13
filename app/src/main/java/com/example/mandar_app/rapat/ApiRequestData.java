package com.example.mandar_app.rapat;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiRequestData {
    @GET("retrieve_meetings")
    Call<ModelResponse> ardRetrieveAllData();
}
