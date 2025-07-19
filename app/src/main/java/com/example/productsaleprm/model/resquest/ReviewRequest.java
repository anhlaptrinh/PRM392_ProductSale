package com.example.productsaleprm.model.resquest;

public class ReviewRequest {
    private int productID;
    private int userID;
    private int rating;
    private String comment;

    public ReviewRequest() {
    }

    public ReviewRequest(int productID, int rating, String comment) {
        this.productID = productID;
        this.rating = rating;
        this.comment = comment;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
