// HomeFragment.java
package com.example.chatwave.ui.addnewchatuser;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatwave.R;
import com.example.chatwave.databinding.FragmentHomeBinding;
import com.example.chatwave.models.response.LoginResponse;
import com.example.chatwave.models.response.UserListResponse;
import com.example.chatwave.util.ApplicationSharedPreferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

public class AddNewChatUserFragment extends Fragment implements UserListAdapter.OnUserClickListener {

    private FragmentHomeBinding binding;
    private Context mContext;
    private RecyclerView userListRecycleView;
    private UserListAdapter userListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewChatUserViewModel homeViewModel =
                new ViewModelProvider(this).get(NewChatUserViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mContext = getContext();
        homeViewModel.userList();

        // Initialize recyclerView
        userListRecycleView = root.findViewById(R.id.newChatUserListRv);
        userListRecycleView.setLayoutManager(new LinearLayoutManager(mContext));

        homeViewModel.getUserListLiveData().observe(getViewLifecycleOwner(), userListResponses -> {
            if (userListResponses != null) {
                // Update UI with the new data
                userListAdapter = new UserListAdapter(userListResponses);
                userListAdapter.setOnUserClickListener(this);
                userListRecycleView.setAdapter(userListAdapter);
            }
        });

        // Set up search functionality
        binding.userListSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (userListAdapter != null) {
                    userListAdapter.filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (userListAdapter != null) {
                    userListAdapter.filter(newText);
                }
                return false;
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
