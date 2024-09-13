package com.example.mandar_app.bbm;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiRequestData {
    @GET("fuels")
    Call<ModelResponse> ardRetrieveAllData(
            @Query("boat_name") String boatName,
            @Query("role") String role
    );

    @GET("boats/fisherman")
    Call<ModelBoatResponse> ardRetrieveBoatForFisherman(@Query("id_fisherman") int id_fisherman);

    @FormUrlEncoded
    @POST("fuels/create")
    Call<ModelResponse> ardCreateDataBBM(
            @Field("fuel_species") String fuel_species,
            @Field("date") String date,
            @Field("quantity_liters") String quantity_liters,
            @Field("price_of_liter") String price_of_liter,
            @Field("total_fuel") String total_fuel,
            @Field("id_boat") int id_boat
    );

    @FormUrlEncoded
    @POST("fuels/delete")
    Call<ModelResponse> ardDeleteDataBBM(
            @Field("id_fuel") int id_fuel
    );
}



