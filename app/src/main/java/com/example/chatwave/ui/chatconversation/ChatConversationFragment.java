package com.example.chatwave.ui.chatconversation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatwave.R;
import com.example.chatwave.models.response.UserListResponse;

public class ChatConversationFragment extends Fragment {

    private ChatConversationViewModel mViewModel;

    public static ChatConversationFragment newInstance() {
        return new ChatConversationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_conversation, container, false);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Hello");
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve data from arguments
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            UserListResponse user = getArguments().getSerializable("user", UserListResponse.class);
            Log.d("responsedata", user.toString());
        }

        mViewModel = new ViewModelProvider(this).get(ChatConversationViewModel.class);
    }
}