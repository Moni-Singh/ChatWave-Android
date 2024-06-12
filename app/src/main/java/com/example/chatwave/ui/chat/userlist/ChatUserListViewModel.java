package com.example.chatwave.ui.chat.userlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatwave.models.response.ChatUserList.ChatUserListData;
import com.example.chatwave.models.response.LogOutResponse;
import com.example.chatwave.webservices.ApiClient;
import com.example.chatwave.webservices.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatUserListViewModel extends ViewModel {

    private MutableLiveData<List<ChatUserListData>> chatUserListLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> logoutLiveData = new MutableLiveData<>();

    public LiveData<List<ChatUserListData>> getChatUserListLiveData() {
        return chatUserListLiveData;
    }

    public LiveData<Boolean> getLogoutLiveData() {
        return logoutLiveData;
    }

    //Method to call ChatUserList
    public void chatUserList(String userToken) {
        String authorizationHeader = "Bearer " + userToken;
        ApiInterface apiInterface = ApiClient.getAPIInterface();
        apiInterface.getChatUser(authorizationHeader).enqueue(new Callback<List<ChatUserListData>>() {
            @Override
            public void onResponse(Call<List<ChatUserListData>> call, Response<List<ChatUserListData>> response) {
                if (response.isSuccessful()) {
                    List<ChatUserListData> chatUserListData = response.body();
                    chatUserListLiveData.setValue(chatUserListData);
                }
            }

            @Override
            public void onFailure(Call<List<ChatUserListData>> call, Throwable t) {
                chatUserListLiveData.setValue(null);
            }
        });
    }

    //Method to call logout Api
    public void logout(String userToken) {
        String authorizationHeader = "Bearer " + userToken;
        ApiInterface apiInterface = ApiClient.getAPIInterface();
        apiInterface.userLogout(authorizationHeader).enqueue(new Callback<LogOutResponse>() {
            @Override
            public void onResponse(Call<LogOutResponse> call, Response<LogOutResponse> response) {
                if (response.isSuccessful()) {
                    LogOutResponse logOutResponse = response.body();

                    if (logOutResponse != null) {
                        logoutLiveData.setValue(true);
                    } else {
                        logoutLiveData.setValue(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<LogOutResponse> call, Throwable t) {
                logoutLiveData.setValue(false);
            }
        });
    }
}
