package com.example.chatwave.ui.chat.searchuser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatwave.models.response.UserListResponse;
import com.example.chatwave.webservices.ApiClient;
import com.example.chatwave.webservices.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewChatUserViewModel extends ViewModel {

    private MutableLiveData<List<UserListResponse>> userListLiveData = new MutableLiveData<>();

    LiveData<List<UserListResponse>> getUserListLiveData() {
        return userListLiveData;
    }

    //Method to call UserList
    public void userList() {
        ApiInterface apiInterface = ApiClient.getAPIInterface();
        apiInterface.getUserList().enqueue(new Callback<List<UserListResponse>>() {
            @Override
            public void onResponse(Call<List<UserListResponse>> call, Response<List<UserListResponse>> response) {
                if (response.isSuccessful()) {
                    List<UserListResponse> userListResponseList = response.body();
                    userListLiveData.setValue(userListResponseList);
                }
            }

            @Override
            public void onFailure(Call<List<UserListResponse>> call, Throwable t) {
                userListLiveData.setValue(null);
            }
        });
    }
}