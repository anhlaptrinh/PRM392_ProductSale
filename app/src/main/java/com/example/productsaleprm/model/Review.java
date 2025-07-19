package com.example.productsaleprm.model;

import java.util.List;

public class Review {
    private int reviewID;
    private int productID;
    private int userID;
    private String username;
    private String email;
    private int rating;
    private String comment;
    private int helpfulCount;
    private String createdAt;
    private List<Vote> voteList;

    public int getReviewID() { return reviewID; }
    public void setReviewID(int reviewID) { this.reviewID = reviewID; }

    public int getProductID() { return productID; }
    public void setProductID(int productID) { this.productID = productID; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public int getHelpfulCount() { return helpfulCount; }
    public void setHelpfulCount(int helpfulCount) { this.helpfulCount = helpfulCount; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public List<Vote> getVoteList() {
        return voteList;
    }

    public boolean isVotedByCurrentUser(String currentUserEmail) {
        if (voteList == null) return false;
        for (Vote vote : voteList) {
            if (vote.getEmail().equals(currentUserEmail)) return true;
        }
        return false;
    }
}
