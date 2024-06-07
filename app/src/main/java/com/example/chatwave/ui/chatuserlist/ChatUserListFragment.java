package com.example.chatwave.ui.chatuserlist;

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
    private  String userToken;

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
        Navigation.findNavController(requireView()).navigate(R.id.navigation_conversation, bundle);
    }
}