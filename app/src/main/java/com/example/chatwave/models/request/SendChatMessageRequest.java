package com.example.chatwave.models.request;

public class SendChatMessageRequest {

    public String receiverId;
    public String data;
    public String messageType;


    public SendChatMessageRequest(String receiverId, String data, String messageType) {
        this.receiverId = receiverId;
        this.data = data;
        this.messageType = messageType;
    }
}
