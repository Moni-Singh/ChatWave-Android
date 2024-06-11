package com.example.chatwave.ui.chatuserlist;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatwave.R;
import com.example.chatwave.databinding.FragmentChatUserListBinding;
import com.example.chatwave.models.response.ChatUserList.ChatUserListData;
import com.example.chatwave.models.response.LoginResponse;
import com.example.chatwave.util.ApplicationSharedPreferences;
import com.google.gson.Gson;

public class ChatUserListFragment extends Fragment implements ChatUserListAdapter.OnChatUserClickListener {

    private FragmentChatUserListBinding binding;
    private Context mContext;
    private RecyclerView chatUserListRecycleView;
    private ChatUserListAdapter chatUserListAdapter;
    private String userToken;

    public static ChatUserListFragment newInstance() {
        return new ChatUserListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChatUserListViewModel chatUserListViewModel =
                new ViewModelProvider(this).get(ChatUserListViewModel.class);

        binding = FragmentChatUserListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mContext = getContext();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        LoginResponse storedLoginResponse = (LoginResponse) ApplicationSharedPreferences.getSavedObject("loginResponse", null, LoginResponse.class, mContext);
        userToken = storedLoginResponse.getToken();
        chatUserListViewModel.chatUserList(userToken);
        binding.addNewChatUserFab.setOnClickListener(view ->
        {
            Navigation.findNavController(view).navigate(R.id.navigation_add_new_user);
        });
        binding.chatUserListSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (chatUserListAdapter != null) {
                    chatUserListAdapter.filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (chatUserListAdapter != null) {
                    chatUserListAdapter.filter(newText);
                }
                return false;
            }
        });
        chatUserListRecycleView = root.findViewById(R.id.chatUserListRv);
        chatUserListRecycleView.setLayoutManager(new LinearLayoutManager(mContext));

        // Add MenuProvider
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.item_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.logout) {
                    chatUserListViewModel.logout(userToken);
                    chatUserListViewModel.getLogoutLiveData().observe(getViewLifecycleOwner(), isLoggedOut -> {
                        if (isLoggedOut) {
                            Navigation.findNavController(requireView())
                                    .navigate(R.id.navigation_login);
                        } else {
                            Toast.makeText(mContext, "Logout failed, please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return true;
                } else if (id == R.id.userprofile) {
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogLayout = inflater.inflate(R.layout.popup_dialog_userdetails, null);
                    TextView tvUserName = dialogLayout.findViewById(R.id.tvUserName);
                    TextView tvUserEmail = dialogLayout.findViewById(R.id.tvUserEmail);
                    tvUserEmail.setText(storedLoginResponse.getEmail());
                    tvUserName.setText(storedLoginResponse.getName());

                    // Build the dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setView(dialogLayout);
                    builder.show();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner());

        // Observe the LiveData from ViewModel
        chatUserListViewModel.getChatUserListLiveData().observe(getViewLifecycleOwner(), chatUserList -> {
            if (chatUserList != null) {
                chatUserListAdapter = new ChatUserListAdapter(mContext, chatUserList, storedLoginResponse);
                chatUserListAdapter.setOnUserClickListener(this);
                chatUserListRecycleView.setAdapter(chatUserListAdapter);
            }
        });
        return root;
    }

    @Override
    public void onChatUserClick(ChatUserListData chatUserListData) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("chatUserList", chatUserListData);
        Navigation.findNavController(requireView())
                .navigate(R.id.navigation_conversation, bundle);
    }
}