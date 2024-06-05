package com.example.chatwave.webservices;

import com.example.chatwave.models.requestmodels.LoginRequest;
import com.example.chatwave.models.requestmodels.RegisterRequest;
import com.example.chatwave.models.responsemodel.LoginResponse;
import com.example.chatwave.models.responsemodel.RegisterResponse;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST(AppUrls.Login)
    Call<LoginResponse>postLogin( @Body LoginRequest reqLogin);

    @POST(AppUrls.Register)
    Call<RegisterResponse>postRegister(@Body RegisterRequest reqRegister);

}
