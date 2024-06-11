package com.example.chatwave.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.chatwave.R;
import com.example.chatwave.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private Context mContext;
    private ProgressDialog progressDialog;
    private View progressLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        LoginViewModel loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mContext = getContext();
        progressLayout = binding.progressLayout.getRoot();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        final TextView textViewRegister = binding.textViewRegister;
        final TextView textUserName = binding.loginEmail;
        final TextView textUserPassword = binding.loginPassword;
        binding.btnLogin.setOnClickListener(view -> {
            String email = textUserName.getText().toString();
            String password = textUserPassword.getText().toString();

            NavController navController = Navigation.findNavController(view);
            loginViewModel.performLogin(email, password, navController, mContext, progressLayout);
        });

        textViewRegister.setOnClickListener(view ->
        {
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