package com.example.chatwave.models.response.ChatUserList;

import java.io.Serializable;

public class ChatUserListData implements Serializable {
    public SenderDetails senderDetails;
    public ReceiverDetails receiverDetails;
    public String lastMessage;
    public String lastMessageTimeStamps;

    // Constructor
    public ChatUserListData(SenderDetails senderDetails, ReceiverDetails receiverDetails, String lastMessage, String lastMessageTimeStamps) {
        this.senderDetails = senderDetails;
        this.receiverDetails = receiverDetails;
        this.lastMessage = lastMessage;
        this.lastMessageTimeStamps = lastMessageTimeStamps;
    }

    // Getter method for senderDetails
    public SenderDetails getSenderDetails() {
        return senderDetails;
    }

    // Getter method for receiverDetails
    public ReceiverDetails getReceiverDetails() {
        return receiverDetails;
    }

    // Other getter methods for lastMessage and lastMessageTimeStamps
    public String getLastMessage() {
        return lastMessage;
    }

    public String getLastMessageTimeStamps() {
        return lastMessageTimeStamps;
    }
}
