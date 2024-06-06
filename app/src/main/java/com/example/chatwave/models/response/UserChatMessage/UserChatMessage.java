package com.example.chatwave.models.response.UserChatMessage;

public class UserChatMessage {
    public String _id;
    public String senderId;
    public String receiverId;
    public String data;
    public String messageType;
    public String createdAt;
    public String updatedAt;
    public int __v;

    // Constructor
    public UserChatMessage(String _id, String senderId, String receiverId, String data, String messageType, String createdAt, String updatedAt, int __v) {
        this._id = _id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.data = data;
        this.messageType = messageType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.__v = __v;
    }
    // Getters and Setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }
}
