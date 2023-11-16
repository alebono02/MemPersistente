package com.example.myapplication;

import static com.example.myapplication.RetroFitProvider.getApiInterface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public Button bt;
    public TextView tv;
    public static final String TAG = "MiaApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        /*SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("PREF_KEY", 369);
        editor.apply();

        int highScore = sharedPref.getInt("PREF_KEY", 0);

        Log.d("MiaApp", "onCreate: "+ highScore); */

        tv = findViewById(R.id.textView);
        bt = findViewById(R.id.button);

        if (sharedPref.getString("sid","").isEmpty() ||
                sharedPref.getInt("uid", 0) == 0) {

            Log.d(TAG, "onCreate: dati non presenti");
            ApiInterface apiInterface = getApiInterface();
            Log.d(TAG, "getApiInterface"); //funziona fino a qui
            retrofit2.Call<SignUpResponse> signUpCall;
            try {
                signUpCall = apiInterface.register();
            } catch (Exception e) {
                Log.d(TAG, "Error: " + e.getMessage());
                return;
            }
            //Call<SignUpResponse> signUpCall = apiInterface.register();
            Log.d(TAG, "call created");
            signUpCall.enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                    if (!response.isSuccessful()) {
                        Log.d(TAG, "Error: " + response.code());
                        return;
                    }
                    SignUpResponse result = response.body();
                    Log.d(TAG, result.sid);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("sid", result.sid);
                    editor.putInt("uid", result.uid);
                    editor.apply();
                }

                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable t) {
                    Log.d(TAG, "Error: " + t.getMessage());
                }
            });
        } else {
            Log.d(TAG, "onCreate: " + sharedPref.getString("sid", ""));
        }



    }
}