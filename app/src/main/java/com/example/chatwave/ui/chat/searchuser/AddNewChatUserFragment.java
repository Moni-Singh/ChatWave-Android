// HomeFragment.java
package com.example.chatwave.ui.chat.searchuser;

import android.content.Context;
import android.os.Bundle;
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
import com.example.chatwave.models.response.UserListResponse;
import com.example.chatwave.util.Constants;
import com.example.chatwave.util.HelperMethod;

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

        if (!HelperMethod.isNetworkAvailable(mContext)) {
            HelperMethod.showGeneralNICToast(mContext);
            Navigation.findNavController(requireView()).navigate(R.id.navigation_no_internet);
            return root;
        }
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
            } else {
                HelperMethod.showErrorToast(mContext);
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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //OnClick on user
    @Override
    public void onUserClick(UserListResponse user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.User, user);

        Navigation.findNavController(requireView())
                .navigate(R.id.navigation_conversation, bundle);
        Navigation.findNavController(requireView()).navigate(R.id.navigation_conversation, bundle);
    }
}
