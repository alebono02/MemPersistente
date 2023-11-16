package com.example.myapplication;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitProvider {
    public static ApiInterface apiInterface = null;

    public static ApiInterface getApiInterface() {
        if (apiInterface == null) {
            String BASE_URL = "https://develop.ewlab.di.unimi.it/mc/mostri/";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
                    apiInterface = retrofit.create(ApiInterface.class);
    }
        Log.d("MiaApp","RetrofitProvider: getApiInterface");
        return apiInterface;
    }
}
