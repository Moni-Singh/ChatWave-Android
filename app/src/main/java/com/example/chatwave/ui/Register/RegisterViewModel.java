package com.example.chatwave.ui.Register;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.example.chatwave.R;
import com.example.chatwave.models.requestmodels.LoginRequest;
import com.example.chatwave.models.requestmodels.RegisterRequest;
import com.example.chatwave.models.responsemodel.RegisterResponse;
import com.example.chatwave.webservices.ApiClient;
import com.example.chatwave.webservices.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {

  public void perfomRegister(String firstname, String lastname, String username, String email,
                             String selectedGender, String selectedDOB, String password,
                             String confirmPassword , String role, NavController navController) {


    Log.d("Clicked", "Register Clicked");
    Log.d("Firstname", firstname);
    Log.d("Lastname", lastname);
    Log.d("Username", username);
    Log.d("Email", email);
    Log.d("Gender", selectedGender);
    Log.d("DOB", selectedDOB);
    Log.d("Password", password);
    Log.d("ConfirmPassword", confirmPassword);
    Log.d("Role", role);

    Log.d("Clicked","Register Clicked");
    RegisterRequest registerRequest = new RegisterRequest(firstname,lastname,username,email,selectedGender,selectedDOB,password,confirmPassword,role);
    ApiInterface apiInterface = ApiClient.getAPIInterface();

    apiInterface.postRegister(registerRequest).enqueue(new Callback<RegisterResponse>() {
      @Override
      public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
        if(response.isSuccessful()){
          navController.navigate(R.id.navigation_login);
          RegisterResponse registerResponse = response.body();
          Log.d("Response Successfull",registerResponse.message);

        }
      }

      @Override
      public void onFailure(Call<RegisterResponse> call, Throwable t) {
        Log.e("Login Response", "Error occurred: " + t.getMessage());
        t.printStackTrace();
      }
    });
  }
}