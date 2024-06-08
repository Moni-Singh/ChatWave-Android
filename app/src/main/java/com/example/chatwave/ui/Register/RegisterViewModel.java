package com.example.chatwave.ui.Register;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.example.chatwave.R;
import com.example.chatwave.models.request.RegisterRequest;
import com.example.chatwave.models.response.RegisterResponse;
import com.example.chatwave.webservices.ApiClient;
import com.example.chatwave.webservices.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {

  public void perfomRegister(String firstname, String lastname, String username, String email,
                             String selectedGender, String selectedDOB, String password,
                             String confirmPassword , String role, NavController navController, Context mContext, View progressLayout) {

    if (firstname == null || firstname.isEmpty() || lastname == null || lastname.isEmpty() || username == null || username.isEmpty() || email == null ||email.isEmpty()
    || selectedGender == null || selectedGender.isEmpty() || password == null || password.isEmpty() || selectedDOB == null|| selectedDOB.isEmpty() ||
    confirmPassword == null || confirmPassword.isEmpty() || role ==null ||role.isEmpty()) {
      Toast.makeText(mContext, "Fill all the details", Toast.LENGTH_SHORT).show();
      return;
    }

    progressLayout.setVisibility(View.VISIBLE);

    RegisterRequest registerRequest = new RegisterRequest(firstname,lastname,username,email,selectedGender,selectedDOB,password,confirmPassword,role);
    ApiInterface apiInterface = ApiClient.getAPIInterface();

    apiInterface.postRegister(registerRequest).enqueue(new Callback<RegisterResponse>() {
      @Override
      public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
        if(response.isSuccessful()){
          progressLayout.setVisibility(View.GONE);
          navController.navigate(R.id.navigation_login);
          RegisterResponse registerResponse = response.body();
          Log.d("Register Response Sucessfull",registerResponse.message);
        }
      }
      @Override
      public void onFailure(Call<RegisterResponse> call, Throwable t) {
        Log.e("Register  Response", "Error occurred: " + t.getMessage());
        t.printStackTrace();
        progressLayout.setVisibility(View.GONE);
      }
    });
  }
}