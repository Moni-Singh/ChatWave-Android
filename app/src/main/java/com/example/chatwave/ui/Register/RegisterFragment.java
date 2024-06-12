package com.example.chatwave.ui.Register;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.chatwave.R;
import com.example.chatwave.databinding.FragmentRegisterBinding;
import com.example.chatwave.models.request.RegisterRequest;
import com.example.chatwave.models.response.RegisterResponse;
import com.example.chatwave.util.Constants;
import com.example.chatwave.util.HelperMethod;

import java.util.Calendar;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    String selectedGender = "";
    String selectedDOB = "";
    private Context mContext;
    private View progressLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Initialize the ViewModel
        RegisterViewModel registerViewModel =
                new ViewModelProvider(this).get(RegisterViewModel.class);

        // Inflate the fragment layout using View Binding
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mContext = getContext();
        progressLayout = binding.progressLayout.getRoot();

        final TextView textViewLogin = binding.textViewLogin;
        final TextView textFirstName = binding.edtFirstName;
        final TextView textLastName = binding.edtLastName;
        final TextView textUserName = binding.edtUserName;
        final TextView textEmail = binding.edtEmail;
        final TextView textPassword = binding.edtPassword;
        final TextView textConfirmPassword = binding.edtconfirmPassword;
        final RadioGroup radioGroup = binding.radioGrp;

        // Set a listener for gender selection
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = group.findViewById(checkedId);
            if (selectedRadioButton != null) {
                String selectedGenderType = selectedRadioButton.getText().toString();
                selectedGender = selectedGenderType;
            }
        });

        // Set a listener for date of birth selection
        binding.edtDateOfBirth.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (dialogView, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        binding.edtDateOfBirth.setText(selectedDate);
                        selectedDOB = selectedDate;
                    },
                    year, month, dayOfMonth);
            datePickerDialog.show();
        });

        // Set a listener for the register button click
        binding.btnRegister.setOnClickListener(view -> {
            String firstname = textFirstName.getText().toString();
            String lastname = textLastName.getText().toString();
            String username = textUserName.getText().toString();
            String email = textEmail.getText().toString();
            String password = textPassword.getText().toString();
            String confirmpassword = textConfirmPassword.getText().toString();
            String role = Constants.User;

            // Check if any of the input fields are empty
            if (firstname == null || firstname.isEmpty() || lastname == null || lastname.isEmpty() || username == null || username.isEmpty() || email == null || email.isEmpty()
                    || selectedGender == null || selectedGender.isEmpty() || password == null || password.isEmpty() || selectedDOB == null || selectedDOB.isEmpty() ||
                    confirmpassword == null || confirmpassword.isEmpty() || role == null || role.isEmpty()) {
                HelperMethod.showToast(getResources().getString(R.string.fill_all_details), mContext);
                return;
            }
            RegisterRequest registerRequest = new RegisterRequest(firstname, lastname, username, email, selectedGender, selectedDOB, password, confirmpassword, role);
            if (!HelperMethod.isNetworkAvailable(mContext)) {
                HelperMethod.showGeneralNICToast(mContext);
                Navigation.findNavController(requireView()).navigate(R.id.navigation_no_internet);
                return;
            }
            registerViewModel.performRegister(registerRequest);
        });

        registerViewModel.getRegisterResponseObserver().observe(getViewLifecycleOwner(), new Observer<RegisterResponse>() {
            @Override
            public void onChanged(RegisterResponse registerResponse) {
                if (registerResponse != null) {
                    Navigation.findNavController(requireView()).navigate(R.id.navigation_login);
                    progressLayout.setVisibility(View.VISIBLE);
                } else {
                    HelperMethod.showErrorToast(mContext);
                    progressLayout.setVisibility(View.GONE);
                }
            }
        });

        textViewLogin.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigation_login);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}