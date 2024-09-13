package com.example.mandar_app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiRequestData {
    @GET("banners")
    Call<ModelBannerResponse> ardRetrieveAllBanners();
}
