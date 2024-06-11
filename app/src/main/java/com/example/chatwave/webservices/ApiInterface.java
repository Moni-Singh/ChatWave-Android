package com.example.chatwave.webservices;

import com.example.chatwave.models.request.LoginRequest;
import com.example.chatwave.models.request.RegisterRequest;
import com.example.chatwave.models.request.SendChatMessageRequest;
import com.example.chatwave.models.request.UserChatMessage.UserChatMesaageRequest;
import com.example.chatwave.models.response.ChatUserList.ChatUserListData;
import com.example.chatwave.models.response.FcmTokenResponse;
import com.example.chatwave.models.response.LogOutResponse;
import com.example.chatwave.models.response.LoginResponse;
import com.example.chatwave.models.response.RegisterResponse;
import com.example.chatwave.models.response.SendChatMessageResponse;
import com.example.chatwave.models.response.UserChatMessage.UserChatMessage;
import com.example.chatwave.models.response.UserListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    //Login Api
    @POST(AppUrls.Login)
    Call<LoginResponse> postLogin(@Body LoginRequest reqLogin);

    //Register Api
    @POST(AppUrls.Register)
    Call<RegisterResponse> postRegister(@Body RegisterRequest reqRegister);

    //UserList Api
    @GET(AppUrls.USER_LIST)
    Call<List<UserListResponse>> getUserList();

    @GET(AppUrls.CHAT_USER_LIST)
    Call<List<ChatUserListData>> getChatUser(@Header(AppUrls.HEADER) String authorization);

    @POST(AppUrls.USER_CHAT_MESSAGE)
    Call<List<UserChatMessage>> getUserChatMessage(@Body UserChatMesaageRequest userChatMesaageRequest);

    @POST(AppUrls.SEND_CHAT_MESSAGE)
    Call<SendChatMessageResponse> sendChatMessage(@Header(AppUrls.HEADER) String authorization, @Body SendChatMessageRequest sendChatMessageRequest);

    @GET(AppUrls.USER_LOGOUT)
    Call<LogOutResponse> userLogout(@Header(AppUrls.HEADER) String authorization);

}
