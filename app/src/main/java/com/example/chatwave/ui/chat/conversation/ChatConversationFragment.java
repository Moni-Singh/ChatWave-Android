package com.example.chatwave.ui.chat.conversation;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatwave.R;
import com.example.chatwave.databinding.FragmentChatConversationBinding;
import com.example.chatwave.models.request.SendChatMessageRequest;
import com.example.chatwave.models.request.UserChatMessage.UserChatMesaageRequest;
import com.example.chatwave.models.response.ChatUserList.ChatUserListData;
import com.example.chatwave.models.response.LoginResponse;
import com.example.chatwave.models.response.UserListResponse;
import com.example.chatwave.util.ApplicationSharedPreferences;
import com.example.chatwave.util.Constants;
import com.example.chatwave.util.HelperMethod;

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
        LoginResponse storedLoginResponse = (LoginResponse) ApplicationSharedPreferences.getSavedObject(Constants.LOGIN_DATA, null, LoginResponse.class, mContext);
        chatConversationViewModel.getUserMessageHistory().observe(getViewLifecycleOwner(), chatConversation -> {
            if (chatConversation != null) {
                chatConversationAdapter = new ChatConversationAdapter(chatConversation, storedLoginResponse);
                userToken = storedLoginResponse.getToken();
                userChatHistoryRv.setAdapter(chatConversationAdapter);
                userChatHistoryRv.scrollToPosition(chatConversationAdapter.getItemCount() - 1);
            } else {
                HelperMethod.showErrorToast(mContext);
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ChatUserListData chatUserData = getArguments().getSerializable(Constants.Chat_User_List, ChatUserListData.class);
            UserListResponse userListResponse = getArguments().getSerializable(Constants.User, UserListResponse.class);
            if (userListResponse != null) {
                ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(userListResponse.getFirstname());
                chatReceiverId = userListResponse.get_id();
                senderId = storedLoginResponse.getId();
                receiverId = userListResponse.get_id();
                userToken = storedLoginResponse.getToken();
                if (!HelperMethod.isNetworkAvailable(mContext)) {
                    HelperMethod.showGeneralNICToast(mContext);
                    Navigation.findNavController(requireView()).navigate(R.id.navigation_no_internet);
                    return root;
                }
                UserChatMesaageRequest userChatMesaageRequest = new UserChatMesaageRequest(senderId, receiverId);
                chatConversationViewModel.getUserChatMessage(userChatMesaageRequest);
            } else if (chatUserData != null) {
                senderId = chatUserData.getSenderDetails().userId;
                receiverId = chatUserData.getReceiverDetails().userId;
                if (!HelperMethod.isNetworkAvailable(mContext)) {
                    HelperMethod.showGeneralNICToast(mContext);
                    Navigation.findNavController(requireView()).navigate(R.id.navigation_no_internet);
                    return root;
                }
                UserChatMesaageRequest userChatMesaageRequest = new UserChatMesaageRequest(senderId, receiverId);
                chatConversationViewModel.getUserChatMessage(userChatMesaageRequest);
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
            if (!HelperMethod.isNetworkAvailable(mContext)) {
                HelperMethod.showGeneralNICToast(mContext);
                Navigation.findNavController(requireView()).navigate(R.id.navigation_no_internet);
                return;
            }
            String messageType = "textMessage";
            SendChatMessageRequest sendChatMessageRequest = new SendChatMessageRequest(receiverId, dataMessage, messageType);
            chatConversationViewModel.sendTextMessage(sendChatMessageRequest, userToken);
            chatConversationViewModel.getSendChatMessageLiveData().observe(getViewLifecycleOwner(), sendChatMessageResponse -> {
                if (sendChatMessageResponse != null) {
                    if (!HelperMethod.isNetworkAvailable(mContext)) {
                        HelperMethod.showGeneralNICToast(mContext);
                        Navigation.findNavController(requireView()).navigate(R.id.navigation_no_internet);
                        return;
                    }
                    UserChatMesaageRequest userChatMesaageRequest = new UserChatMesaageRequest(senderId, receiverId);
                    chatConversationViewModel.getUserChatMessage(userChatMesaageRequest);
                } else {
                    HelperMethod.showErrorToast(mContext);
                }
            });
            chatConversationViewModel.getSendChatMessageLiveData().observe(getViewLifecycleOwner(), sendChatMessageResponse -> {
                if (sendChatMessageResponse != null) {
                    userChatHistoryRv.scrollToPosition(chatConversationAdapter.getItemCount() - 1);
                    edtMessageBox.setText("");
                } else {
                    HelperMethod.showErrorToast(mContext);
                }
            });
        });

        // Initialize the handler and the runnable for refreshing the chat
        handler = new Handler(Looper.getMainLooper());
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                if (!HelperMethod.isNetworkAvailable(mContext)) {
                    HelperMethod.showGeneralNICToast(mContext);
                    Navigation.findNavController(requireView()).navigate(R.id.navigation_no_internet);
                    return;
                }
                UserChatMesaageRequest userChatMesaageRequest = new UserChatMesaageRequest(senderId, receiverId);
                chatConversationViewModel.getUserChatMessage(userChatMesaageRequest);
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(refreshRunnable, 2000);

        return root;
    }


}