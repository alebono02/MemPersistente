package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("users/")
    Call<SignUpResponse> register();


    @GET("objects/{id}/")
    Call<VirtualObjectDetails> virtualObjectDetails(@Path("id")int id, @Query("sid") String sid);

}
