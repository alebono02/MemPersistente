package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("users/")
    Call<SignUpResponse> register();

}
