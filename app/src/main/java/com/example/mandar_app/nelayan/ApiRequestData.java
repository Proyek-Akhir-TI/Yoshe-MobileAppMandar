package com.example.mandar_app.nelayan;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRequestData {
    @GET("retrieveGroups")
    Call<ModelResponseGroups> retrieveGroups();

    @GET("retrieveAllData")
    Call<ModelResponse> ardRetrieveAllData(@Query("group_name") String groupName);
}

