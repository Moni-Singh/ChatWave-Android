package com.example.chatwave.ui.chat.conversation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatwave.R;
import com.example.chatwave.databinding.ItemUserReceiveMessageBinding;
import com.example.chatwave.databinding.ItemUserSendMessageBinding;
import com.example.chatwave.models.response.LoginResponse;
import com.example.chatwave.models.response.UserChatMessage.UserChatMessage;
import com.example.chatwave.util.HelperMethod;

import java.util.List;

public class ChatConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_RECEIVE = 1;
    private static final int ITEM_SEND = 2;
    private List<UserChatMessage> mUserChatMessage;
    private LoginResponse mLoginResponse;

    public ChatConversationAdapter(List<UserChatMessage> userChatMessages, LoginResponse loginResponse) {
        this.mUserChatMessage = userChatMessages;
        this.mLoginResponse = loginResponse;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_SEND) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_send_message, parent, false);
            return new SentViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_receive_message, parent, false);
            return new ReceiveViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserChatMessage userChatMessage = mUserChatMessage.get(position);
        if (holder instanceof SentViewHolder) {
            ((SentViewHolder) holder).bind(userChatMessage);
        } else if (holder instanceof ReceiveViewHolder) {
            ((ReceiveViewHolder) holder).bind(userChatMessage);
        }
    }

    @Override
    public int getItemCount() {
        return mUserChatMessage.size();
    }

    @Override
    public int getItemViewType(int position) {
        UserChatMessage userChatMessage = mUserChatMessage.get(position);
        return userChatMessage.getSenderId().equals(mLoginResponse.getId()) ? ITEM_SEND : ITEM_RECEIVE;
    }

    public static class SentViewHolder extends RecyclerView.ViewHolder {
        ItemUserSendMessageBinding binding;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemUserSendMessageBinding.bind(itemView);
        }

        public void bind(UserChatMessage userChatMessage) {
            binding.textSendMessage.setText(userChatMessage.data);
            String timestamp = userChatMessage.getCreatedAt();
            String time = HelperMethod.convertTimestampToTime(timestamp);
            binding.tvSendMessageTime.setText(time);
        }
    }

    public static class ReceiveViewHolder extends RecyclerView.ViewHolder {
        ItemUserReceiveMessageBinding binding;

        public ReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemUserReceiveMessageBinding.bind(itemView);
        }

        public void bind(UserChatMessage userChatMessage) {
            binding.textReceiveMessage.setText(userChatMessage.data);
            String timestamp = userChatMessage.getCreatedAt();
            String time = HelperMethod.convertTimestampToTime(timestamp);
            binding.tvReceiveMessageTime.setText(time);
        }
    }
}
