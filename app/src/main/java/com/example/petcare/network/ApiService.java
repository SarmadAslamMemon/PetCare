package com.example.petcare.network;

import com.example.petcare.modelclass.LoginRequest;
import com.example.petcare.modelclass.PredictionResponse;
import com.example.petcare.modelclass.TextInput;
import com.example.petcare.modelclass.UserRegisterRequest;
import com.example.petcare.modelclass.PetFoodTime;
import com.example.petcare.modelclass.PetFoodTimeResponse;
import com.example.petcare.network.model.Blog;
import com.example.petcare.modelclass.Pet;
import com.example.petcare.modelclass.User;
import com.example.petcare.modelclass.BlogResponse;
import com.example.petcare.modelclass.BlogListResponse;
import com.example.petcare.modelclass.LoginResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @Headers({"Accept: */*", "Content-Type: application/json"})
    @POST("users/register")
    Call<User> registerUser(@Body UserRegisterRequest request);

    @Headers({"Accept: */*", "Content-Type: application/json"})
    @POST("users/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @Headers({"Accept: */*", "Content-Type: application/json"})
    @POST("/food")
    Call<Void> setPetFoodTime(@Body PetFoodTime request);

    @Headers({"Accept: */*", "Content-Type: application/json"})
    @POST("/blogs")
    Call<BlogResponse> createBlog(@Body Blog blog);

    @Multipart
    @POST("blogs/upload-image")
    Call<BlogResponse> uploadBlogImage(
            @Part MultipartBody.Part image,
            @Part("blogId") RequestBody blogId
    );
    @Headers({"Accept: */*", "Content-Type: application/json"})
    @GET("/blogs")
    Call<BlogListResponse> getAllBlogs();

    @Multipart
    @POST("/predict-image/")
    Call<PredictionResponse> predictImage(@Part MultipartBody.Part image);

    @POST("/predict-text/")
    Call<PredictionResponse> predictText(@Body TextInput textInput);

    @GET("pets/user/{userId}")
    Call<List<Pet>> getPetsByUserId(@Path("userId") int userId);

    @POST("/pets")
    Call<ResponseBody> addPet(@Body Pet pet);


    @GET("/food/times/{petId}")
    Call<PetFoodTimeResponse> getPetFoodTimes(@Path("petId") int petId);
}