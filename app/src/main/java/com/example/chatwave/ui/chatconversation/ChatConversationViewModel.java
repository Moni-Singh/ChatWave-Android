package com.example.chatwave.ui.chatconversation;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatwave.models.request.SendChatMessageRequest;
import com.example.chatwave.models.request.UserChatMessage.UserChatMesaageRequest;
import com.example.chatwave.models.response.SendChatMessageResponse;
import com.example.chatwave.models.response.UserChatMessage.UserChatMessage;
import com.example.chatwave.webservices.ApiClient;
import com.example.chatwave.webservices.ApiInterface;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatConversationViewModel extends ViewModel {
    private MutableLiveData<List<UserChatMessage>> chatUserListLiveData = new MutableLiveData<>();
    private MutableLiveData<SendChatMessageResponse> sendChatMessageLiveData = new MutableLiveData<>();

    public LiveData<List<UserChatMessage>> getUserMessageHistory() {
        return chatUserListLiveData;
    }

    public LiveData<SendChatMessageResponse> getSendChatMessageLiveData() {
        return sendChatMessageLiveData;
    }

    //Method to call getUserChatMessages APi
    public void getUserChatMessage(String senderId, String receiverId) {
        UserChatMesaageRequest userChatMesaageRequest = new UserChatMesaageRequest(senderId, receiverId);
        ApiInterface apiInterface = ApiClient.getAPIInterface();
        apiInterface.getUserChatMessage(userChatMesaageRequest).enqueue(new Callback<List<UserChatMessage>>() {
            @Override
            public void onResponse(Call<List<UserChatMessage>> call, Response<List<UserChatMessage>> response) {
                if (response.isSuccessful()) {
                    List<UserChatMessage> userChatMessage = response.body();
                    chatUserListLiveData.setValue(userChatMessage);
                }
            }

            @Override
            public void onFailure(Call<List<UserChatMessage>> call, Throwable t) {
                Log.e("User Chat Message Resposne Failed", "Error occurred: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    //Method to call sendTextMessages Api
    public void sendTextMessage(String dataMessage, String receiverId, String userToken) {
        String authorizationHeader = "Bearer " + userToken;
        String messageType = "textMessage";
        SendChatMessageRequest sendChatMessageRequest = new SendChatMessageRequest(receiverId, dataMessage, messageType);
        ApiInterface apiInterface = ApiClient.getAPIInterface();
        apiInterface.sendChatMessage(authorizationHeader, sendChatMessageRequest).enqueue(new Callback<SendChatMessageResponse>() {
            @Override
            public void onResponse(Call<SendChatMessageResponse> call, Response<SendChatMessageResponse> response) {
                if (response.isSuccessful()) {
                    SendChatMessageResponse sendChatMessageResposne = response.body();
                    sendChatMessageLiveData.setValue(sendChatMessageResposne);
                }
            }

            @Override
            public void onFailure(Call<SendChatMessageResponse> call, Throwable t) {
                Log.e("API Error", "Failed to this  SendChatMessageResposne: " + t.getMessage());
            }
        });
    }

}