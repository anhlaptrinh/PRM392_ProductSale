package com.example.productsaleprm.model.resquest;

public class ReviewReplyRequest {
    private String replyText;

    public ReviewReplyRequest(String replyText) {
        this.replyText = replyText;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }
}
