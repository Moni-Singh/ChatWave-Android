package com.example.chatwave.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.chatwave.R;
import com.example.chatwave.databinding.FragmentLoginBinding;
import com.example.chatwave.models.request.LoginRequest;
import com.example.chatwave.models.response.LoginResponse;
import com.example.chatwave.util.ApplicationSharedPreferences;
import com.example.chatwave.util.Constants;
import com.example.chatwave.util.HelperMethod;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private Context mContext;
    private ProgressDialog progressDialog;
    private View progressLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        progressLayout = binding.progressLayout.getRoot();
        mContext = getContext();

        String fcmToken = ApplicationSharedPreferences.getFCMToken(mContext.getApplicationContext());

        final TextView textViewRegister = binding.textViewRegister;
        final TextView textUserName = binding.loginEmail;
        final TextView textUserPassword = binding.loginPassword;

        binding.btnLogin.setOnClickListener(view -> {
            String email = textUserName.getText().toString();
            String password = textUserPassword.getText().toString();

            // Check if email or password is empty
            if (email.isEmpty() || password.isEmpty()) {
                HelperMethod.showToast(getResources().getString(R.string.email_password_should_not_be_empty), mContext);
                return;
            }
            // Check if email is in gmail.com format
            if (!email.endsWith("@gmail.com")) {
                HelperMethod.showToast(getResources().getString(R.string.email_should_be_gmail), mContext);
                return;
            }

            if (!HelperMethod.isNetworkAvailable(mContext)) {
                HelperMethod.showGeneralNICToast(mContext);
                Navigation.findNavController(requireView()).navigate(R.id.navigation_no_internet);
                return;
            }
            progressLayout.setVisibility(View.VISIBLE);
            LoginRequest loginRequest = new LoginRequest(email, password, fcmToken);
            loginViewModel.performLogin(loginRequest);
        });

        loginViewModel.getLoginResponseObserver().observe(getViewLifecycleOwner(), new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                if (loginResponse != null) {
                    ApplicationSharedPreferences.saveObject(Constants.LOGIN_DATA, loginResponse, mContext);
                    Navigation.findNavController(requireView()).navigate(R.id.navigation_chat_user);
                    progressLayout.setVisibility(View.GONE);
                } else {
                    HelperMethod.showErrorToast(mContext);
                }
            }
        });

        textViewRegister.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigation_register);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}