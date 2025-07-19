package com.example.productsaleprm.model;

public class ReviewReply {
    private int replyID;
    private int reviewID;
    private int repliedBy;
    private String username;
    private String email;
    private String replyText;
    private String repliedAt;

    public ReviewReply() {}

    public ReviewReply(int replyID, int reviewID, int repliedBy, String username, String email, String replyText, String repliedAt) {
        this.replyID = replyID;
        this.reviewID = reviewID;
        this.repliedBy = repliedBy;
        this.username = username;
        this.email = email;
        this.replyText = replyText;
        this.repliedAt = repliedAt;
    }

    public int getReplyID() { return replyID; }
    public void setReplyID(int replyID) { this.replyID = replyID; }

    public int getReviewID() { return reviewID; }
    public void setReviewID(int reviewID) { this.reviewID = reviewID; }

    public int getRepliedBy() { return repliedBy; }
    public void setRepliedBy(int repliedBy) { this.repliedBy = repliedBy; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getReplyText() { return replyText; }
    public void setReplyText(String replyText) { this.replyText = replyText; }

    public String getRepliedAt() { return repliedAt; }
    public void setRepliedAt(String repliedAt) { this.repliedAt = repliedAt; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
