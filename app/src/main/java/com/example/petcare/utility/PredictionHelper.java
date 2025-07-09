package com.example.petcare.utility;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.example.petcare.modelclass.PredictionResponse;
import com.example.petcare.modelclass.TextInput;
import com.example.petcare.network.ApiService;
import com.example.petcare.network.RetrofitClient;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PredictionHelper {
    private final Context context;
    private final ApiService apiService;

    public interface PredictionCallback {
        void onSuccess(PredictionResponse response);
        void onError(String message);
    }

    public PredictionHelper(Context context) {
        this.context = context;
        this.apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    public void predictFromText(String text, PredictionCallback callback) {
        // Hardcoded Retrofit instance for ngrok URL
        retrofit2.Retrofit ngrokRetrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("https://fe672a500e42.ngrok-free.app/")
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();
        ApiService ngrokApiService = ngrokRetrofit.create(ApiService.class);
        TextInput input = new TextInput(text);
        ngrokApiService.predictText(input).enqueue(new retrofit2.Callback<PredictionResponse>() {
            @Override
            public void onResponse(retrofit2.Call<PredictionResponse> call, retrofit2.Response<PredictionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to get prediction");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PredictionResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void predictFromImage(Uri imageUri, PredictionCallback callback) {
        try {
            File file = new File(imageUri.getPath());
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            // Hardcoded Retrofit instance for ngrok URL
            retrofit2.Retrofit ngrokRetrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl("https://fe672a500e42.ngrok-free.app")
                    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                    .build();
            ApiService ngrokApiService = ngrokRetrofit.create(ApiService.class);

            ngrokApiService.predictImage(body).enqueue(new retrofit2.Callback<PredictionResponse>() {
                @Override
                public void onResponse(retrofit2.Call<PredictionResponse> call, retrofit2.Response<PredictionResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        callback.onSuccess(response.body());
                    } else {
                        callback.onError("Failed to get prediction from image");
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<PredictionResponse> call, Throwable t) {
                    callback.onError(t.getMessage());
                }
            });
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }
} 