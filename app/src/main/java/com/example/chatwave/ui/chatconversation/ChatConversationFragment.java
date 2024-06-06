package com.example.chatwave.ui.chatconversation;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chatwave.R;
import com.example.chatwave.databinding.FragmentChatConversationBinding;
import com.example.chatwave.models.response.ChatUserList.ChatUserListData;
import com.example.chatwave.models.response.LoginResponse;
import com.example.chatwave.util.ApplicationSharedPreferences;
import com.google.gson.Gson;

public class ChatConversationFragment extends Fragment {

    private ChatConversationViewModel mViewModel;
    private Context mContext;
    private FragmentChatConversationBinding binding;
    private RecyclerView userChatHistoryRv;
    private  ChatConversationAdapter chatConversationAdapter;
    private  String chatReceiverId ;
    private  String senderId ;
    private  String receiverId;


    public static ChatConversationFragment newInstance() {
        return new ChatConversationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ChatConversationViewModel chatConversationViewModel = new ViewModelProvider(this).get(ChatConversationViewModel.class);
        binding = FragmentChatConversationBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        mContext = getContext();


        userChatHistoryRv= root.findViewById(R.id.userChatHistoryRv);
        userChatHistoryRv.setLayoutManager(new LinearLayoutManager(mContext));
        LoginResponse storedLoginResponse = (LoginResponse) ApplicationSharedPreferences.getSavedObject("loginResponse", null, LoginResponse.class, mContext);
        chatConversationViewModel.getUserMessageHistory().observe(getViewLifecycleOwner(),chatConversation ->{
            if(chatConversation !=null){
                chatConversationAdapter = new ChatConversationAdapter(chatConversation,storedLoginResponse);
                userChatHistoryRv.setAdapter(chatConversationAdapter);
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ChatUserListData chatUserData = getArguments().getSerializable("chatUserList", ChatUserListData.class);
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(chatUserData);

            if (chatUserData != null) {
                 senderId = chatUserData.getSenderDetails().userId;
                 receiverId = chatUserData.getReceiverDetails().userId;
                chatConversationViewModel.getUserChatMessage(senderId,receiverId);
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
        binding.userSendMessage.setOnClickListener(view ->{
            String dataMessage = edtMessageBox.getText().toString();
            chatConversationViewModel.sendTextMessage(dataMessage,chatReceiverId);
            chatConversationViewModel.getUserChatMessage(senderId,receiverId);
        });
        return  root;
    }


}