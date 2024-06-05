package com.example.chatwave.ui.login;

import android.content.Context;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        LoginViewModel loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textViewRegister = binding.textViewRegister;
        final TextView textUserName = binding.loginEmail;
        final  TextView textUserPassword = binding.loginPassword;
        View rootView = binding.getRoot();
        ProgressBar progressBar = rootView.findViewById(R.id.progressBar);

        binding.btnLogin.setOnClickListener(view ->{
            String email = textUserName.getText().toString();
            String password = textUserPassword.getText().toString();
            NavController navController = Navigation.findNavController(view);
            loginViewModel.performLogin(email,password,navController,progressBar,mContext);
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