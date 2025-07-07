package com.example.petcare.fragments.consultion;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.petcare.R;
import com.example.petcare.model.Blog;
import com.example.petcare.modelclass.BlogResponse;
import com.example.petcare.network.ApiService;
import com.example.petcare.network.RetrofitClient;
import com.example.petcare.utility.CameraUtils;
import com.example.petcare.utility.ProgressDialogUtil;
import com.example.petcare.utility.SharePreference;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;
import java.io.IOException;

public class CreateBlogBottomSheet extends BottomSheetDialogFragment {

    private ImageView blogImageView;
    private TextView pickImageText;
    private TextInputEditText blogTitleInput, blogContentInput;
    private MaterialButton postBlogButton;
    private CameraUtils cameraUtils;
    private Uri selectedImageUri;
    private SharePreference sharePreference;

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null) {
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        if (imageBitmap != null) {
                            blogImageView.setImageBitmap(imageBitmap);
                            pickImageText.setVisibility(View.GONE);
                            // TODO: Convert bitmap to Uri and save
                        }
                    }
                }
            });

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        selectedImageUri = imageUri;
                        blogImageView.setImageURI(imageUri);
                        pickImageText.setVisibility(View.GONE);
                    }
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);
        sharePreference = new SharePreference(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_create_blog, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
//        blogImageView = view.findViewById(R.id.blogImageView);
//        pickImageText = view.findViewById(R.id.pickImageText);
        blogTitleInput = view.findViewById(R.id.blogTitleInput);
        blogContentInput = view.findViewById(R.id.blogContentInput);
        postBlogButton = view.findViewById(R.id.postBlogButton);

//        cameraUtils = new CameraUtils(requireContext(), blogImageView);

//        pickImageText.setOnClickListener(v ->
//            cameraUtils.checkPermissionsAndOpen(cameraLauncher, galleryLauncher));

        postBlogButton.setOnClickListener(v -> validateAndPost());
    }

    private void validateAndPost() {
        String title = blogTitleInput.getText().toString().trim();
        String content = blogContentInput.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            blogTitleInput.setError("Title is required");
            return;
        }

        if (TextUtils.isEmpty(content)) {
            blogContentInput.setError("Content is required");
            return;
        }

        // Get user ID from SharePreference
        int userId = sharePreference.getUserId();
        if (userId == -1) {
            showError("User not logged in");
            return;
        }

        // Create blog object
        Blog blog = new Blog(userId, title, content, null);

        // Show progress dialog
        ProgressDialogUtil.showProgressBar(requireContext(), true);

        // Make API call to create blog
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.createBlog(blog).enqueue(new Callback<BlogResponse>() {
            @Override
            public void onResponse(@NonNull Call<BlogResponse> call, @NonNull Response<BlogResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BlogResponse blogResponse = response.body();
                    if (blogResponse.isSuccess()) {
                        // If image is selected, upload it
                        if (selectedImageUri != null) {
                            uploadBlogImage(blogResponse.getData().getId());
                        } else {
                            ProgressDialogUtil.showProgressBar(requireContext(), false);
                            showSuccess(blogResponse.getMessage());
                            dismiss();
                        }
                    } else {
                        ProgressDialogUtil.showProgressBar(requireContext(), false);
                        showError(blogResponse.getMessage());
                    }
                } else {
                    ProgressDialogUtil.showProgressBar(requireContext(), false);
                    showError("Failed to create blog");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BlogResponse> call, @NonNull Throwable t) {
                ProgressDialogUtil.showProgressBar(requireContext(), false);
                showError("Network error occurred");
            }
        });
    }

    private void uploadBlogImage(int blogId) {
        try {
            ProgressDialogUtil.showProgressBar(requireContext(), true);
            
            // Get the actual file path from the URI
            String filePath = null;
            if (selectedImageUri.getScheme().equals("content")) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = requireContext().getContentResolver().query(selectedImageUri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    filePath = cursor.getString(columnIndex);
                    cursor.close();
                }
            } else if (selectedImageUri.getScheme().equals("file")) {
                filePath = selectedImageUri.getPath();
            }

            if (filePath == null) {
                ProgressDialogUtil.showProgressBar(requireContext(), false);
                showError("Could not get image file path");
                return;
            }

            File file = new File(filePath);
            if (!file.exists()) {
                ProgressDialogUtil.showProgressBar(requireContext(), false);
                showError("Image file does not exist");
                return;
            }

            // Create MultipartBody.Part for image
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            // Create RequestBody for blogId (as plain text)
            RequestBody blogIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(blogId));

            // Log request details
            Log.d("BlogUpload", "File path: " + filePath);
            Log.d("BlogUpload", "File exists: " + file.exists());
            Log.d("BlogUpload", "File size: " + file.length());
            Log.d("BlogUpload", "Blog ID: " + blogId);

            ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
            apiService.uploadBlogImage(imagePart, blogIdBody).enqueue(new Callback<BlogResponse>() {
                @Override
                public void onResponse(@NonNull Call<BlogResponse> call, @NonNull Response<BlogResponse> response) {
                    ProgressDialogUtil.showProgressBar(requireContext(), false);
                    Log.d("BlogUpload", "Response code: " + response.code());
                    
                    if (response.isSuccessful() && response.body() != null) {
                        BlogResponse blogResponse = response.body();
                        Log.d("BlogUpload", "Success: " + blogResponse.isSuccess());
                        Log.d("BlogUpload", "Message: " + blogResponse.getMessage());
                        if (blogResponse.getData() != null) {
                            Log.d("BlogUpload", "Image URL: " + blogResponse.getData().getImageUrl());
                        }
                        
                        if (blogResponse.isSuccess()) {
                            showSuccess("Image uploaded successfully");
                            dismiss();
                        } else {
                            showError(blogResponse.getMessage());
                        }
                    } else {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                            Log.e("BlogUpload", "Error response: " + errorBody);
                            showError("Upload failed: " + errorBody);
                        } catch (IOException e) {
                            Log.e("BlogUpload", "Error reading error body", e);
                            showError("Failed to upload image");
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BlogResponse> call, @NonNull Throwable t) {
                    ProgressDialogUtil.showProgressBar(requireContext(), false);
                    Log.e("BlogUpload", "Network error", t);
                    showError("Network error: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            ProgressDialogUtil.showProgressBar(requireContext(), false);
            Log.e("BlogUpload", "Error preparing upload", e);
            showError("Error preparing upload: " + e.getMessage());
        }
    }

    private void showError(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void showSuccess(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    public static CreateBlogBottomSheet newInstance() {
        return new CreateBlogBottomSheet();
    }
} 