package com.example.chatwave.ui.chatuserlist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatwave.R;
import com.example.chatwave.databinding.ChatUserListBinding;
import com.example.chatwave.models.response.ChatUserList.ChatUserListData;
import com.example.chatwave.models.response.LoginResponse;
import com.example.chatwave.util.HelperMethod;

import java.util.List;

public class ChatUserListAdapter extends RecyclerView.Adapter<ChatUserListAdapter.ChatUserViewHolder> {

    private ChatUserListAdapter.OnChatUserClickListener listener;
    private List<ChatUserListData> mChatUserList;
    private Context mContext;
    private LoginResponse mLoginResponse;


    public ChatUserListAdapter(Context mContext, List<ChatUserListData> mChatUserList, LoginResponse loginResponse) {
        this.mContext = mContext;
        this.mChatUserList = mChatUserList;
        this.mLoginResponse = loginResponse;
    }
    public void setOnUserClickListener(OnChatUserClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_list, parent, false);
        return new ChatUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserViewHolder holder, int position) {
        ChatUserListData chatUser = mChatUserList.get(position);
        holder.bind(chatUser);
    }

    @Override
    public int getItemCount() {
        return mChatUserList.size();
    }

    public class ChatUserViewHolder extends RecyclerView.ViewHolder {
        ChatUserListBinding binding;

        public ChatUserViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ChatUserListBinding.bind(itemView);
        }

        public void bind(ChatUserListData chatUserListData) {
            if (mLoginResponse != null && chatUserListData != null) {
                String senderId = chatUserListData.getSenderDetails().userId;
                String receiverId = chatUserListData.getReceiverDetails().userId;
                String loginUserId = mLoginResponse.getId();
                if (loginUserId.equals(senderId) || loginUserId.equals(receiverId)) {
                    if (loginUserId.equals(senderId)) {
                        binding.userNameTv.setText(chatUserListData.getReceiverDetails().userFirstName);
                    } else {
                        binding.userNameTv.setText(chatUserListData.getSenderDetails().userFirstName);
                    }
                }
            } else {
                Log.d("Error", "LoginResponse or ChatUserListData is null");
            }

            String timestamp = chatUserListData.lastMessageTimeStamps;
            String time = HelperMethod.convertTimestampToTime(timestamp);
            System.out.println("Formatted Time: " + time);
            binding.lastMessageStatus.setText(time);

            binding.lastMessageTv.setText(chatUserListData.lastMessage);
            binding.usernameProfileCl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onChatUserClick(chatUserListData);
                    }
                }
            });
        }
    }

    public interface OnChatUserClickListener {
        void onChatUserClick(ChatUserListData chatUserListData);
    }
}
