package com.example.petcare.utility;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.petcare.modelclass.PredictionResponse;
import com.example.petcare.modelclass.TextInput;
import com.example.petcare.network.ApiService;
import com.example.petcare.network.RetrofitClient;

import java.io.File;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PredictionHelper {
    private static final String TAG = "PredictionHelper";
//    private static final String BASE_URL = "https://smartpaw.onrender.com";
    private static final String BASE_URL = "https://dbee2f80cd7b.ngrok-free.app";
    private final Context context;
    private final ApiService apiService;

    public interface PredictionCallback {
        void onSuccess(PredictionResponse response);
        void onError(String message);
    }

    public interface ProgressCallback {
        void onProgress(String message);
    }

    public PredictionHelper(Context context) {
        this.context = context;
        this.apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    private retrofit2.Retrofit buildLoggingRetrofit(String baseUrl) {
        // Verbose HTTP logger
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Log.d(TAG, message));
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Custom interceptor to capture raw request/response bodies and re-create the body
        Interceptor tapInterceptor = chain -> {
            Request request = chain.request();
            String requestBodyString = null;
            try {
                if (request.body() != null && !(request.body() instanceof MultipartBody)) {
                    Buffer buffer = new Buffer();
                    request.body().writeTo(buffer);
                    requestBodyString = buffer.readUtf8();
                } else if (request.body() instanceof MultipartBody) {
                    requestBodyString = "<multipart body>";
                }
            } catch (Exception ignored) {}
            Log.d(TAG, "RAW REQUEST → " + request.method() + " " + request.url());
            Log.d(TAG, "RAW REQUEST HEADERS → " + request.headers());
            if (requestBodyString != null) {
                Log.d(TAG, "RAW REQUEST BODY → " + requestBodyString);
            }

            okhttp3.Response response = chain.proceed(request);
            ResponseBody body = response.body();
            MediaType contentType = body != null ? body.contentType() : null;
            String bodyString = null;
            try {
                if (body != null) {
                    bodyString = body.string();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error reading RAW RESPONSE BODY: " + e.getMessage());
            }
            Log.d(TAG, "RAW RESPONSE ← code=" + response.code() + ", url=" + request.url());
            Log.d(TAG, "RAW RESPONSE HEADERS ← " + response.headers());
            if (bodyString != null) {
                Log.d(TAG, "RAW RESPONSE BODY ← " + bodyString);
            }
            // Replace consumed body so Retrofit converter can still read it
            ResponseBody newBody = bodyString != null ? ResponseBody.create(contentType, bodyString) : null;
            return response.newBuilder().body(newBody).build();
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(tapInterceptor)
                .addInterceptor(logging)
                .build();

        return new retrofit2.Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();
    }

    public void predictFromText(String text, PredictionCallback callback) {
        retrofit2.Retrofit loggingRetrofit = buildLoggingRetrofit(BASE_URL);
        ApiService loggingApiService = loggingRetrofit.create(ApiService.class);

        String preview = text == null ? "null" : (text.length() > 120 ? text.substring(0, 120) + "…" : text);
        Log.d(TAG, "predictFromText: baseUrl=" + BASE_URL + ", payloadPreview=" + preview);

        TextInput input = new TextInput(text);
        Call<PredictionResponse> call = loggingApiService.predictText(input);
        Log.d(TAG, "predictFromText REQUEST url=" + call.request().url());
        Log.d(TAG, "predictFromText REQUEST headers=" + call.request().headers());

        call.enqueue(new Callback<PredictionResponse>() {
            @Override
            public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
                Log.d(TAG, "predictFromText onResponse: code=" + response.code());
                Log.d(TAG, "predictFromText RESPONSE headers=" + response.headers());
                try {
                    String raw = response.raw().peekBody(Long.MAX_VALUE).string();
                    Log.d(TAG, "predictFromText RAW (peek) ← " + raw);
                } catch (Exception ignored) {}

                if (response.isSuccessful() && response.body() != null) {
                    PredictionResponse body = response.body();
                    Log.d(TAG, "predictFromText success: disease=" + body.getDisease() + ", accuracy=" + body.getAccuracy());
                    callback.onSuccess(body);
                } else {
                    String err = null;
                    try { err = response.errorBody() != null ? response.errorBody().string() : null; } catch (Exception ignored) {}
                    Log.e(TAG, "predictFromText failed: code=" + response.code() + ", errorBody=" + err);
                    callback.onError("Failed to get prediction: " + (err != null ? err : "Unknown error"));
                }
            }

            @Override
            public void onFailure(Call<PredictionResponse> call, Throwable t) {
                Log.e(TAG, "predictFromText onFailure: " + t.getMessage(), t);
                String errorMessage = "Network error: " + t.getMessage();
                if (t instanceof java.net.UnknownHostException) {
                    errorMessage = "No internet connection. Please check your network.";
                } else if (t instanceof java.net.SocketTimeoutException) {
                    errorMessage = "Request timeout. Please try again.";
                }
                callback.onError(errorMessage);
            }
        });
    }

    public void predictFromImage(Uri imageUri, PredictionCallback callback) {
        try {
            Log.d(TAG, "predictFromImage: uri=" + imageUri);
            File file = new File(imageUri.getPath());
            Log.d(TAG, "predictFromImage: fileExists=" + file.exists() + ", size=" + (file.exists() ? file.length() : -1));

            if (!file.exists()) {
                callback.onError("Image file not found. Please try taking the photo again.");
                return;
            }

            if (file.length() == 0) {
                callback.onError("Image file is empty. Please try taking the photo again.");
                return;
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            retrofit2.Retrofit loggingRetrofit = buildLoggingRetrofit(BASE_URL);
            ApiService loggingApiService = loggingRetrofit.create(ApiService.class);

            Log.d(TAG, "predictFromImage: baseUrl=" + BASE_URL + ", filename=" + file.getName());

            Call<PredictionResponse> call = loggingApiService.predictImage(body);
            Log.d(TAG, "predictFromImage REQUEST url=" + call.request().url());
            Log.d(TAG, "predictFromImage REQUEST headers=" + call.request().headers());

            call.enqueue(new Callback<PredictionResponse>() {
                @Override
                public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
                    Log.d(TAG, "predictFromImage onResponse: code=" + response.code());
                    Log.d(TAG, "predictFromImage RESPONSE headers=" + response.headers());
                    try {
                        String raw = response.raw().peekBody(Long.MAX_VALUE).string();
                        Log.d(TAG, "predictFromImage RAW (peek) ← " + raw);
                    } catch (Exception ignored) {}

                    if (response.isSuccessful() && response.body() != null) {
                        PredictionResponse body = response.body();
                        Log.d(TAG, "predictFromImage success: disease=" + body.getDisease() + ", accuracy=" + body.getAccuracy());
                        callback.onSuccess(body);
                    } else {
                        String err = null;
                        try { 
                            err = response.errorBody() != null ? response.errorBody().string() : null; 
                        } catch (Exception ignored) {}
                        
                        Log.e(TAG, "predictFromImage failed: code=" + response.code() + ", errorBody=" + err);
                        
                        // Handle specific error cases
                        if (response.code() == 500 && err != null) {
                            // Check if it's the specific animal recognition error
                            if (err.contains("Image is not recognized as a dog, cat, or fish")) {
                                callback.onError("Image is not recognized as a dog, cat, or fish by the animal filter. Please take a clear photo of your pet.");
                            } else if (err.contains("Prediction error")) {
                                // Extract the specific error message from the response
                                String errorMessage = "Prediction failed: " + err;
                                callback.onError(errorMessage);
                            } else {
                                callback.onError("Server error: " + err);
                            }
                        } else {
                            callback.onError("Failed to get prediction from image: " + (err != null ? err : "Unknown error"));
                        }
                    }
                }

                @Override
                public void onFailure(Call<PredictionResponse> call, Throwable t) {
                    Log.e(TAG, "predictFromImage onFailure: " + t.getMessage(), t);
                    String errorMessage = "Network error: " + t.getMessage();
                    if (t instanceof java.net.UnknownHostException) {
                        errorMessage = "No internet connection. Please check your network.";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorMessage = "Request timeout. Please try again.";
                    }
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "predictFromImage exception: " + e.getMessage(), e);
            callback.onError("Error processing image: " + e.getMessage());
        }
    }
} 