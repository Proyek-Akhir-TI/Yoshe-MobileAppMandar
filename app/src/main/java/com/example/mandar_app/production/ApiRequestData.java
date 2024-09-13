package com.example.mandar_app.production;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiRequestData {
    @POST("create_production")
    Call<ModelResponse> ardCreateProduction(
            @Body ModelProductionRequest productionRequest
    );

    @FormUrlEncoded
    @POST("delete_production")
    Call<ModelResponse> ardDeleteProduction(
            @Field("id_production") int id_production
    );

    @GET("retrieve_boats/{fishermanName}")
    Call<ModelBoatResponse> ardRetrieveAllBoats(@Path("fishermanName") String fishermanName);

    @GET("retrieve_fish_species")
    Call<ModelFishSpeciesResponse> ardRetrieveAllFishSpecies();

    @GET("retrieve_production")
    Call<ModelResponse> ardRetrieveAllProduction();

    @GET("retrieve_production_by_fisherman/{fishermanName}")
    Call<ModelResponse> ardRetrieveProductionByFisherman(@Path("fishermanName") String fishermanName);
}