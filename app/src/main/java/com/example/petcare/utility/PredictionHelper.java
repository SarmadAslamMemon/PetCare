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
        TextInput input = new TextInput(text);
        apiService.predictText(input).enqueue(new Callback<PredictionResponse>() {
            @Override
            public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to get prediction");
                }
            }

            @Override
            public void onFailure(Call<PredictionResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void predictFromImage(Uri imageUri, PredictionCallback callback) {
        try {
            File file = new File(imageUri.getPath());
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            apiService.predictImage(body).enqueue(new Callback<PredictionResponse>() {
                @Override
                public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        callback.onSuccess(response.body());
                    } else {
                        callback.onError("Failed to get prediction from image");
                    }
                }

                @Override
                public void onFailure(Call<PredictionResponse> call, Throwable t) {
                    callback.onError(t.getMessage());
                }
            });
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }
} 