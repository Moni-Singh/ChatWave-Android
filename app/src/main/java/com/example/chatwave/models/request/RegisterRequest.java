package com.example.chatwave.models.request;

public class RegisterRequest {
    public String firstname;
    public String lastname;
    public String username;
    public String email;
    public String gender;
    public String dob;
    public String password;
    public String confirmpassword;
    public String role;

    public RegisterRequest(String firstname, String lastname, String username, String email, String gender,
                           String dob, String password, String confirmpassword, String role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.dob = dob;
        this.password = password;
        this.confirmpassword = confirmpassword;
        this.role = role;
    }
}
