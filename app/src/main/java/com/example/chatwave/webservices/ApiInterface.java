package com.example.chatwave.webservices;

import com.example.chatwave.models.request.LoginRequest;
import com.example.chatwave.models.request.RegisterRequest;
import com.example.chatwave.models.response.LoginResponse;
import com.example.chatwave.models.response.RegisterResponse;
import com.example.chatwave.models.response.UserListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    //Login Api
    @POST(AppUrls.Login)
    Call<LoginResponse>postLogin(@Body LoginRequest reqLogin);

    //Register Api
    @POST(AppUrls.Register)
    Call<RegisterResponse>postRegister(@Body RegisterRequest reqRegister);
    //UserList Api
    @GET(AppUrls.USER_LIST)
    Call<List<UserListResponse>> getUserList();


}
