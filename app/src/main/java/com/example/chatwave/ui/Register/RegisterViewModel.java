package com.example.chatwave.ui.Register;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.example.chatwave.R;
import com.example.chatwave.models.request.RegisterRequest;
import com.example.chatwave.models.response.RegisterResponse;
import com.example.chatwave.util.HelperMethod;
import com.example.chatwave.webservices.ApiClient;
import com.example.chatwave.webservices.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.PUT;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterResponse> registerResponseList;

    public RegisterViewModel() {
        registerResponseList = new MutableLiveData<>();
    }

    public MutableLiveData<RegisterResponse> getRegisterResponseObserver() {
        return registerResponseList;
    }

    // Method to perform the registration process
    public void performRegister(RegisterRequest registerRequest) {

        ApiInterface apiInterface = ApiClient.getAPIInterface();
        apiInterface.postRegister(registerRequest).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    registerResponseList.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                registerResponseList.postValue(null);
            }
        });
    }
}