package com.example.cosmicwatchapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NasaApiService {
    @GET("planetary/apod")
    Call<Picture> getPictureOfDay(@Query("api_key") String apiKey);
}
