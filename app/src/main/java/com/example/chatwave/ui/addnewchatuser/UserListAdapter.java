package com.example.chatwave.ui.addnewchatuser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatwave.R;
import com.example.chatwave.databinding.NewChatUserlistBinding;
import com.example.chatwave.models.response.UserListResponse;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private List<UserListResponse> userList;
    private OnUserClickListener listener;
    private List<UserListResponse> userListFiltered;

    public UserListAdapter(List<UserListResponse> userList) {
        this.userList = userList;
        this.userListFiltered = new ArrayList<>(userList);    }

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
            binding.userNameTextView.setText(user.getFirstname());
            binding.usernameProfileCl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onUserClick(user);
                    }
                }
            });
        }
    }
    public void filter(String query) {
        userListFiltered.clear();
        if (query.isEmpty()) {
            userListFiltered.addAll(userList);
        } else {
            for (UserListResponse user : userList) {
                if (user.getFirstname().toLowerCase().contains(query.toLowerCase())) {
                    userListFiltered.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }

    public interface OnUserClickListener {
        void onUserClick(UserListResponse user);
    }
}
