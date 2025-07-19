package com.example.productsaleprm.model.resquest;

public class VoteRequest {
    private int reviewID;
    private String voteType;

    public VoteRequest() {}

    public VoteRequest(int reviewID, String voteType) {
        this.reviewID = reviewID;
        this.voteType = voteType;
    }

    public int getReviewID() { return reviewID; }
    public void setReviewID(int reviewID) { this.reviewID = reviewID; }

    public String getVoteType() { return voteType; }
    public void setVoteType(String voteType) { this.voteType = voteType; }
}
