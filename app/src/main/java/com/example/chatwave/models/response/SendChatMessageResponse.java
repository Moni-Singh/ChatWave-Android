package com.example.chatwave.models.response;

public class SendChatMessageResponse {
    public String senderId;
    public String receiverId;
    public String data;
    public String messageType;
    public String _id;
    public String createdAt;
    public String updatedAt;
    public int __v;

    public SendChatMessageResponse(String senderId, String receiverId, String data, String messageType, String _id, String createdAt, String updatedAt, int __v) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.data = data;
        this.messageType = messageType;
        this._id = _id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.__v = __v;
    }
}
