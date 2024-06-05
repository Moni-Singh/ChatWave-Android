package com.example.chatwave.ui.login;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.chatwave.R;
import com.example.chatwave.models.requestmodels.LoginRequest;
import com.example.chatwave.models.responsemodel.LoginResponse;
import com.example.chatwave.util.ApplicationSharedPreferences;
import com.example.chatwave.util.Constants;
import com.example.chatwave.webservices.ApiClient;
import com.example.chatwave.webservices.ApiInterface;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {



    //Method to call Login Api
    public void performLogin(String email , String password, NavController navController, View progressBar,Context mContext){

        if (email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            // Create an instance of the LoginRequest
            LoginRequest loginRequest = new LoginRequest(email, password);

            // Call the API using Retrofit
            ApiInterface apiInterface = ApiClient.getAPIInterface();
            apiInterface.postLogin(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        LoginResponse loginResponse = response.body();
                        if (loginResponse != null) {
                            Gson gson = new Gson();
                            String jsonResponse = gson.toJson(loginResponse);
                   
                            ApplicationSharedPreferences.saveObject(Constants.LOGIN_DATA, loginResponse, mContext);

                           Log.d("Login Response JSON", jsonResponse);
                            Log.d("Login Response", "Name: " + loginResponse.getName());
                            navController.navigate(R.id.navigation_home);
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("Login Response", "Error occurred: " + t.getMessage());
                    t.printStackTrace(); // Print the stack trace for detailed error information
                }
            });
    }
    }

}