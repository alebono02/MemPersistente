package com.example.myapplication;

import static com.example.myapplication.RetroFitProvider.getApiInterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


import com.example.myapplication.appRoomDB.AppDatabase;
import com.example.myapplication.appRoomDB.VirtualObject;
import com.google.common.util.concurrent.ListenableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public Button bt;
    public TextView tv;
    public static final String TAG = "MiaApp";

    public AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

        /*SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("PREF_KEY", 369);
        editor.apply();

        int highScore = sharedPref.getInt("PREF_KEY", 0);

        Log.d("MiaApp", "onCreate: "+ highScore); */

        tv = findViewById(R.id.textView);
        bt = findViewById(R.id.button);

        if (sharedPref.getString("sid","").isEmpty() ||
                sharedPref.getInt("uid", -1) == -1) {

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


        bt.setOnClickListener(v -> {
            ListenableFuture<VirtualObject> lf = db.virtualObjectDao().findById(24);
            lf.addListener(() -> {
                try {
                    VirtualObject vo = lf.get();
                    Log.d(TAG, "onCreate: " + vo.name);
                } catch (Exception e) {
                    Log.d(TAG, "onCreate error: " + e.getMessage());
                    callVirtualObjectDetails();
                }
            }, getMainExecutor());
        });

    }

    public void callVirtualObjectDetails() {
        ApiInterface apiInterface = getApiInterface();
        Call<VirtualObjectDetailsResponse> virtualObjectDetailsCall = apiInterface.virtualObjectDetails(24,"PULvqIxV1TQ1jRtJ6dy8");
        virtualObjectDetailsCall.enqueue(new Callback<VirtualObjectDetailsResponse>() {
            @Override
            public void onResponse(Call<VirtualObjectDetailsResponse> call, Response<VirtualObjectDetailsResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d("MiaApp", "Error: " + response.code());
                    return;
                }
                VirtualObjectDetailsResponse result = response.body();
                Log.d("MiaApp", "mostro: " + result.name);

                ListenableFuture<Void> virtualObjectListenableFuture = db.virtualObjectDao().insertAll(new VirtualObject(result.id, result.type, result.level, result.lat, result.lon, result.image, result.name));
                virtualObjectListenableFuture.addListener(() -> {
                    try {
                        virtualObjectListenableFuture.get();
                        Log.d(TAG, "onResponse: " + result.name);
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }
                }, getMainExecutor());
            }

            @Override
            public void onFailure(Call<VirtualObjectDetailsResponse> call, Throwable t) {
                Log.d("MiaApp", "Error: " + t.getMessage());
            }
        });
    }

}