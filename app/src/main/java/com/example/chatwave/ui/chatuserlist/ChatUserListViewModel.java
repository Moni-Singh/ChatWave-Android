package com.example.chatwave.ui.chatuserlist;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatwave.models.response.ChatUserList.ChatUserListData;
import com.example.chatwave.webservices.ApiClient;
import com.example.chatwave.webservices.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatUserListViewModel extends ViewModel {

    private MutableLiveData<List<ChatUserListData>> chatUserListLiveData = new MutableLiveData<>();

    public LiveData<List<ChatUserListData>> getChatUserListLiveData() {
        return chatUserListLiveData;
    }

    public void chatUserList(String userToken) {
        String authorizationHeader = "Bearer " + userToken;
        ApiInterface apiInterface = ApiClient.getAPIInterface();
        apiInterface.getChatUser(authorizationHeader).enqueue(new Callback<List<ChatUserListData>>() {
            @Override
            public void onResponse(Call<List<ChatUserListData>> call, Response<List<ChatUserListData>> response) {
                if (response.isSuccessful()) {
                    List<ChatUserListData> chatUserListData = response.body();
                    Log.d("responseApi", chatUserListData.toString());
                    chatUserListLiveData.setValue(chatUserListData);
                } else {
                    Log.e("Response Error", "Failed to fetch chat user list: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ChatUserListData>> call, Throwable t) {
                Log.e("API Error", "Failed to fetch chat user list: " + t.getMessage());
            }
        });
    }
}
