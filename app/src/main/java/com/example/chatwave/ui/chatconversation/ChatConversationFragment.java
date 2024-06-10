package com.example.chatwave.ui.chatconversation;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.chatwave.databinding.FragmentChatConversationBinding;
import com.example.chatwave.models.response.ChatUserList.ChatUserListData;
import com.example.chatwave.models.response.LoginResponse;
import com.example.chatwave.models.response.UserListResponse;
import com.example.chatwave.util.ApplicationSharedPreferences;
import com.google.gson.Gson;

public class ChatConversationFragment extends Fragment {

    private ChatConversationViewModel mViewModel;
    private Context mContext;
    private FragmentChatConversationBinding binding;
    private RecyclerView userChatHistoryRv;
    private ChatConversationAdapter chatConversationAdapter;
    private String chatReceiverId;
    private String senderId;
    private String receiverId;
    private String userToken;
    private Handler handler;
    private Runnable refreshRunnable;


    public static ChatConversationFragment newInstance() {
        return new ChatConversationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ChatConversationViewModel chatConversationViewModel = new ViewModelProvider(this).get(ChatConversationViewModel.class);
        binding = FragmentChatConversationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mContext = getContext();

        userChatHistoryRv = root.findViewById(R.id.userChatHistoryRv);
        userChatHistoryRv.setLayoutManager(new LinearLayoutManager(mContext));
        LoginResponse storedLoginResponse = (LoginResponse) ApplicationSharedPreferences.getSavedObject("loginResponse", null, LoginResponse.class, mContext);
        chatConversationViewModel.getUserMessageHistory().observe(getViewLifecycleOwner(), chatConversation -> {
            if (chatConversation != null) {
                chatConversationAdapter = new ChatConversationAdapter(chatConversation, storedLoginResponse);
                userToken = storedLoginResponse.getToken();
                userChatHistoryRv.setAdapter(chatConversationAdapter);
                userChatHistoryRv.scrollToPosition(chatConversationAdapter.getItemCount() - 1);
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ChatUserListData chatUserData = getArguments().getSerializable("chatUserList", ChatUserListData.class);
            UserListResponse userListResponse = getArguments().getSerializable("user", UserListResponse.class);
            if (userListResponse != null) {
                ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(userListResponse.getFirstname());
                chatReceiverId = userListResponse.get_id();
                senderId = storedLoginResponse.getId();
                receiverId = userListResponse.get_id();
                userToken = storedLoginResponse.getToken();
                chatConversationViewModel.getUserChatMessage(senderId, receiverId);
            } else if (chatUserData != null) {
                senderId = chatUserData.getSenderDetails().userId;
                receiverId = chatUserData.getReceiverDetails().userId;
                chatConversationViewModel.getUserChatMessage(senderId, receiverId);
                if (storedLoginResponse != null) {
                    String loginUserId = storedLoginResponse.getId();
                    if (loginUserId.equals(senderId) || loginUserId.equals(receiverId)) {
                        if (loginUserId.equals(senderId)) {
                            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(chatUserData.getReceiverDetails().userFirstName);
                            chatReceiverId = chatUserData.getReceiverDetails().userId;
                        } else {
                            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(chatUserData.getSenderDetails().userFirstName);
                            chatReceiverId = chatUserData.getSenderDetails().userId;
                        }
                    }
                }
            }
        }

        final EditText edtMessageBox = binding.edtmessagebox;
        binding.userSendMessage.setOnClickListener(view -> {
            String dataMessage = edtMessageBox.getText().toString();
            chatConversationViewModel.sendTextMessage(dataMessage, chatReceiverId, userToken);
            chatConversationViewModel.getSendChatMessageLiveData().observe(getViewLifecycleOwner(), sendChatMessageResponse -> {
                if (sendChatMessageResponse != null) {
                    chatConversationViewModel.getUserChatMessage(senderId, receiverId);
                }
            });
            chatConversationViewModel.getSendChatMessageLiveData().observe(getViewLifecycleOwner(), sendChatMessageResponse -> {
                if (sendChatMessageResponse != null) {
                    userChatHistoryRv.scrollToPosition(chatConversationAdapter.getItemCount() - 1);
                    edtMessageBox.setText("");
                }
            });
        });
        // Initialize the handler and the runnable for refreshing the chat
        handler = new Handler(Looper.getMainLooper());
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                chatConversationViewModel.getUserChatMessage(senderId, receiverId);
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(refreshRunnable, 2000);

        // Enable the back button in the action bar
        if (requireActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//        // Add logging to back button click handling
//        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                Log.d("clcikedback", "Back button pressed");
//
//                NavController navController = Navigation.findNavController(requireActivity(), R.id.navigation_chat_user);
//                navController.navigate(R.id.navigation_chat_user);
//            }
//        });
        return root;
    }
}