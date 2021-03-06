package com.dragon.android.spirit.network;

import com.dragon.android.spirit.model.ResponseModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface SpiritApi {

    @Multipart
    @POST("file_upload_api/upload.php")
    Call<ResponseModel> fileUpload(
            @Part("sender_information") RequestBody description,
            @Part MultipartBody.Part file);

}
