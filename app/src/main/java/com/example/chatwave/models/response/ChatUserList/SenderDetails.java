package com.example.chatwave.models.response.ChatUserList;

public class SenderDetails {
    public String userId;
    public String userFirstName;
    public String userLastName;
    public Object userProfileImage;

    // Constructor
    public SenderDetails(String userId, String userFirstName, String userLastName, Object userProfileImage) {
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userProfileImage = userProfileImage;
    }
}

