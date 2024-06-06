// HomeFragment.java
package com.example.chatwave.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatwave.R;
import com.example.chatwave.databinding.FragmentHomeBinding;
import com.example.chatwave.models.response.LoginResponse;
import com.example.chatwave.models.response.UserListResponse;
import com.example.chatwave.util.ApplicationSharedPreferences;
import com.google.gson.Gson;

import java.util.List;

public class HomeFragment extends Fragment implements UserListAdapter.OnUserClickListener {

    private FragmentHomeBinding binding;
    private Context mContext;
    private RecyclerView userListRecycleView;
    private UserListAdapter userListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mContext = getContext();
        homeViewModel.userList();

        // Initialize recyclerView
        userListRecycleView = root.findViewById(R.id.newChatUserListRv);
        userListRecycleView.setLayoutManager(new LinearLayoutManager(mContext));

        // Observe the LiveData from ViewModel
        homeViewModel.getUserListLiveData().observe(getViewLifecycleOwner(), userListResponses -> {
            if (userListResponses != null) {
                // Update UI with the new data
                userListAdapter = new UserListAdapter(userListResponses);
                userListAdapter.setOnUserClickListener(this);
                userListRecycleView.setAdapter(userListAdapter);
            }
        });
        // Retrieving stored login response
        LoginResponse storedLoginResponse = (LoginResponse) ApplicationSharedPreferences.getSavedObject("loginResponse", null, LoginResponse.class, mContext);
        if (storedLoginResponse != null) {
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(storedLoginResponse);
            Log.d("Stored Login Response", jsonResponse);
        } else {
            Log.d("Stored Login Response", "No login response found.");
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onUserClick(UserListResponse user) {
        String firstName = user.getFirstname();
        Log.d("FirstName", firstName);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);

        Navigation.findNavController(requireView())
                .navigate(R.id.navigation_conversation, bundle);
        Navigation.findNavController(requireView()).navigate(R.id.navigation_conversation,bundle);
    }
}
