package com.example.chatwave.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.example.chatwave.R;
import com.example.chatwave.models.request.LoginRequest;
import com.example.chatwave.models.response.LoginResponse;
import com.example.chatwave.util.ApplicationSharedPreferences;
import com.example.chatwave.webservices.ApiClient;
import com.example.chatwave.webservices.ApiInterface;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    //Method to call Login Api
    public void performLogin(String email , String password, NavController navController, Context mContext,View progressLayout){

     if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            Toast.makeText(mContext, "Email or Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        String fcmToken = ApplicationSharedPreferences.getFCMToken(mContext.getApplicationContext());
        progressLayout.setVisibility(View.VISIBLE);
        // Create an instance of the LoginRequest
        String fcmToggken = fcmToken;
             Log.d("---Fcmtoken---",fcmToggken);
            LoginRequest loginRequest = new LoginRequest(email, password,fcmToken);
            ApiInterface apiInterface = ApiClient.getAPIInterface();
            apiInterface.postLogin(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = response.body();
                        if (loginResponse != null) {
                            progressLayout.setVisibility(View.GONE);
                            Gson gson = new Gson();
                            String jsonResponse = gson.toJson(loginResponse);
                            Log.d("Login Response JSON", jsonResponse);
                            ApplicationSharedPreferences.saveObject("loginResponse", loginResponse, mContext);
                            navController.navigate(R.id.navigation_chat_user);
                            SharedPreferences sharedPreferences = mContext.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();
                        }
                    }
                }
                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.e("Login Response", "Error occurred: " + t.getMessage());
                    t.printStackTrace();
                    progressLayout.setVisibility(View.GONE);
                }
            });
    }
    }

