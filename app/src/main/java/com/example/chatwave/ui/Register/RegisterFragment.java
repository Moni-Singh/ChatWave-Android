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
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.chatwave.R;
import com.example.chatwave.databinding.FragmentRegisterBinding;

import org.w3c.dom.Text;

import java.util.Calendar;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    String selectedGender = "";
    String selectedDOB = "";
    private Context mContext;
    private View progressLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RegisterViewModel registerViewModel =
                new ViewModelProvider(this).get(RegisterViewModel.class);

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mContext = getContext();
        progressLayout = binding.progressLayout.getRoot();

        final TextView textViewLogin = binding.textViewLogin;
        final TextView textFirstName = binding.edtFirstName;
        final TextView textLastName = binding.edtLastName;
        final TextView textUserName = binding.edtUserName;
        final TextView textEmail = binding.edtEmail;
        final  TextView textPassword = binding.edtPassword;
        final TextView textConfirmPassword = binding.edtconfirmPassword;
        final RadioGroup radioGroup = binding.radioGrp;

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = group.findViewById(checkedId);
            if (selectedRadioButton != null) {
                String selectedGenderType = selectedRadioButton.getText().toString();
            Log.d("Gender",selectedGender);
            selectedGender = selectedGenderType;

            }
        });

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

        binding.btnRegister.setOnClickListener(view ->{
            String firstname = textFirstName.getText().toString();
            String lastname = textLastName.getText().toString();
            String username= textUserName.getText().toString();
            String email = textEmail.getText().toString();
            String password = textPassword.getText().toString();
            String confirmpassword = textConfirmPassword.getText().toString();
            String role = "user";
            NavController navController = Navigation.findNavController(view);
            registerViewModel.perfomRegister(firstname,lastname,username,email,selectedGender,selectedDOB,password,confirmpassword,role,navController,mContext,progressLayout);
        });

        textViewLogin.setOnClickListener(view->{
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