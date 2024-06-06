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

    public LiveData<List<UserChatMessage>> getUserMessageHistory() {
        return chatUserListLiveData;
    }



    public  void  getUserChatMessage(String senderId ,String receiverId){

        UserChatMesaageRequest userChatMesaageRequest = new UserChatMesaageRequest(senderId,receiverId);
        ApiInterface apiInterface = ApiClient.getAPIInterface();
        apiInterface.getUserChatMessage(userChatMesaageRequest).enqueue(new Callback<List<UserChatMessage>>() {
            @Override
            public void onResponse(Call<List<UserChatMessage>> call, Response<List<UserChatMessage>> response) {
                if(response.isSuccessful()){
                    List<UserChatMessage> userChatMessage = response.body();
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(userChatMessage);
                    Log.d("jsonUserChatMessageResposne", jsonResponse);
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
    public void sendTextMessage(String dataMessage, String  receiverId){
        Log.d("clickerdfdfsf","yes in");
        String authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY2NGRkODU2N2QwZWNkYzlkNzk5NTcyZCIsImlhdCI6MTcxNzc2MDQ1OCwiZXhwIjoxNzE3ODQ2ODU4fQ.YDijo-d4UlqEI7PhDzKKTH1kwTstsH1GiKq0_6wRb7I";
        String authorizationHeader = "Bearer " + authToken;
//      String receiverId = "6655ac6530d9da371bc9d99f";
      String data = "Hello mam work fast";
      String messageType = "textMessage";
      Log.d("resceiverId",receiverId);
      Log.d("dataMessage",dataMessage);

        SendChatMessageRequest sendChatMessageRequest = new SendChatMessageRequest(receiverId,dataMessage,messageType);
        ApiInterface apiInterface = ApiClient.getAPIInterface();
        apiInterface.sendChatMessage(authorizationHeader,sendChatMessageRequest).enqueue(new Callback<SendChatMessageResponse>() {
            @Override
            public void onResponse(Call<SendChatMessageResponse> call, Response<SendChatMessageResponse> response) {
                if(response.isSuccessful()){
                    SendChatMessageResponse sendChatMessageResposne = response.body();
                    Gson gson = new Gson();
                    String resposne = gson.toJson(sendChatMessageResposne);
                    Log.d("response sendChatMessage",resposne);
                }
            }
            @Override
            public void onFailure(Call<SendChatMessageResponse> call, Throwable t) {
                Log.e("API Error", "Failed to this  SendChatMessageResposne: " + t.getMessage());
            }
        });
    }

}