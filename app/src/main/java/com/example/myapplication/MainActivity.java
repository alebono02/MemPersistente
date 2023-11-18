package com.example.myapplication;

import static com.example.myapplication.RetroFitProvider.getApiInterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.myapplication.appRoomDB.AppDatabase;
import com.google.common.util.concurrent.ListenableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public Button bt;
    public TextView nomeMostro;
    public TextView livelloMostro;
    public TextView latitudineMostro;
    public TextView longitudineMostro;
    public ImageView immagineMostro;

    public SharedPreferences sharedPref;
    public static final String TAG = "MiaApp";

    public AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getPreferences(Context.MODE_PRIVATE);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

        /*SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("PREF_KEY", 369);
        editor.apply();

        int highScore = sharedPref.getInt("PREF_KEY", 0);

        Log.d("MiaApp", "onCreate: "+ highScore); */

        nomeMostro = findViewById(R.id.NomeMostro);
        livelloMostro = findViewById(R.id.livelloMostro);
        latitudineMostro = findViewById(R.id.latMostro);
        longitudineMostro = findViewById(R.id.lonMostro);
        immagineMostro = findViewById(R.id.imageView);
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
            getAndSetViewMonsterFromDB(24);
        });

    }

    public void getAndSetViewMonsterFromDB(int id) {
        ListenableFuture<VirtualObjectDetails> lf = db.virtualObjectDao().findById(id);
        lf.addListener(() -> {
            try {
                VirtualObjectDetails vo = lf.get();
                Log.d(TAG, "onCreate: " + vo.name + " " + vo.image);
                setMonsterView(vo);
            } catch (Exception e) {
                Log.d(TAG, "onCreate error: " + e.getMessage());
                callVirtualObjectDetails();
            }
        }, getMainExecutor());
    }

    public void callVirtualObjectDetails() {
        ApiInterface apiInterface = getApiInterface();
        Call<VirtualObjectDetails> virtualObjectDetailsCall = apiInterface.virtualObjectDetails(24,sharedPref.getString("sid", ""));
        virtualObjectDetailsCall.enqueue(new Callback<VirtualObjectDetails>() {
            @Override
            public void onResponse(Call<VirtualObjectDetails> call, Response<VirtualObjectDetails> response) {
                if (!response.isSuccessful()) {
                    Log.d("MiaApp", "Error: " + response.code());
                    return;
                }
                VirtualObjectDetails result = response.body();
                Log.d("MiaApp", "mostro: " + result.name);
/*
                ListenableFuture<Void> virtualObjectListenableFuture = db.virtualObjectDao().insertAll(result);
                virtualObjectListenableFuture.addListener(() -> {
                    try {
                        virtualObjectListenableFuture.get();
                        Log.d(TAG, "onResponse: " + result.name);
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }
                }, getMainExecutor());
*/
                insertMonsterInDB(result);
                setMonsterView(result);
            }

            @Override
            public void onFailure(Call<VirtualObjectDetails> call, Throwable t) {
                Log.d("MiaApp", "Error: " + t.getMessage());
            }
        });
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageBytes = android.util.Base64.decode(b64, android.util.Base64.DEFAULT);
        return android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    public void getBytesFromImageBase64(String imageBase64) {

        byte[] imageBytes = android.util.Base64.decode(imageBase64, android.util.Base64.DEFAULT);
        Bitmap decodedImage = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        immagineMostro.setImageBitmap(decodedImage);
    }

    public void insertMonsterInDB(VirtualObjectDetails monster) {
        ListenableFuture<Void> virtualObjectListenableFuture = db.virtualObjectDao().insertAll(monster);
        virtualObjectListenableFuture.addListener(() -> {
            try {
                virtualObjectListenableFuture.get();
                Log.d(TAG, "onResponse: " + monster.name);
            } catch (Exception e) {
                Log.d(TAG, "onResponse: " + e.getMessage());
            }
        }, getMainExecutor());
    }

    public void setMonsterView(VirtualObjectDetails monster) {
        nomeMostro.setText(monster.name);
        livelloMostro.setText(String.valueOf(monster.level));
        latitudineMostro.setText(String.valueOf(monster.lat));
        longitudineMostro.setText(String.valueOf(monster.lon));
        immagineMostro.setImageBitmap(base64ToBitmap(monster.image));
    }
}