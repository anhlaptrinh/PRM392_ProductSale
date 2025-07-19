package com.example.productsaleprm.model;

public class Vote {
    private int reviewID;
    private int userID;
    private String email;

    public Vote(int reviewID, int userID, String email) {
        this.reviewID = reviewID;
        this.userID = userID;
        this.email = email;
    }

    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
