package com.example.chatwave.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatwave.R;
import com.example.chatwave.databinding.NewChatUserlistBinding;
import com.example.chatwave.models.response.UserListResponse;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private List<UserListResponse> userList;
    private OnUserClickListener listener;

    public UserListAdapter(List<UserListResponse> userList) {
        this.userList = userList;
    }

    public void setOnUserClickListener(OnUserClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_chat_userlist, parent, false);
        NewChatUserlistBinding binding = NewChatUserlistBinding.bind(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserListResponse user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NewChatUserlistBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NewChatUserlistBinding.bind(itemView);
        }
        public void bind(UserListResponse user) {
            binding.userNameTextView.setText(user.getFirstname()); // Assuming getName() returns the user's name
            binding.usernameProfileCl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click event here if needed
                    if (listener != null) {
                        listener.onUserClick(user); // Notify listener about user click
                    }
                }
            });
        }
    }

    public interface OnUserClickListener {
        void onUserClick(UserListResponse user);
    }
}
