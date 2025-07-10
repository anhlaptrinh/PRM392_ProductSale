package com.example.productsaleprm.model.resquest;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    private String email;
    private String password;
    private String username;

    @SerializedName("phone")  // Nếu backend nhận key là "phone" thay vì "phoneNumber"
    private String phoneNumber;

    private String address;

    public RegisterRequest(String email, String password, String username, String phoneNumber, String address) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // Getters
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getUsername() { return username; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }

    // Setters
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setUsername(String username) { this.username = username; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setAddress(String address) { this.address = address; }
}
