package com.example.chatwave.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatwave.models.response.UserListResponse;
import com.example.chatwave.webservices.ApiClient;
import com.example.chatwave.webservices.ApiInterface;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<UserListResponse>> userListLiveData = new MutableLiveData<>();
     LiveData<List<UserListResponse>> getUserListLiveData(){
        return userListLiveData;
    }


    public void userList (){
        ApiInterface apiInterface = ApiClient.getAPIInterface();
        apiInterface.getUserList().enqueue(new Callback<List<UserListResponse>>() {
            @Override
            public void onResponse(Call<List<UserListResponse>> call, Response<List<UserListResponse>> response) {
                if(response.isSuccessful()){
                    List<UserListResponse> userListResponseList = response.body();
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(userListResponseList);
                    Log.d("responseApi",jsonResponse);
                    userListLiveData.setValue(userListResponseList);
                }
            }
            @Override
            public void onFailure(Call<List<UserListResponse>> call, Throwable t) {
                Log.e("API Error", "Failed to fetch user list: " + t.getMessage());
            }
        });
    }
}