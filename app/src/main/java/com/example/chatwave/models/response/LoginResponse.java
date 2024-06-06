package com.example.chatwave.models.response;

public class LoginResponse {

    private  String id;
    private String name;
    private String email;
    private  String role;
    private  String token;

    public LoginResponse (String id, String name,String email, String role, String token){

        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.token = token;
    }
    public String getName() {
        return name;
    }

    public  String getToken(){
        return token;
    }

}
