package com.example.cosmicwatchapp;

import android.app.Application;

import androidx.work.OneTimeWorkRequest;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import androidx.work.WorkManager;
import android.content.Context;

public class PictureOfDayApplication extends Application {
    private NasaApiService nasaApiService;

    @Override
    public void onCreate() {

        super.onCreate();
        nasaApiService = getNasaApiService();
    }

    public NasaApiService getNasaApiService() {
        // Configuração do Retrofit
        return new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NasaApiService.class);
    }

    public void fetchPictureOfDay(Callback<Picture> callback) {
        nasaApiService.getPictureOfDay("xgbesIq8RAGRLL7FQjxdzNBfXfQhowZHVH2Gtzsh").enqueue(callback);
    }
}
