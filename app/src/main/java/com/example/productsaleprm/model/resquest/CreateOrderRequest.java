package com.example.productsaleprm.model.resquest;

import java.math.BigDecimal;

public class CreateOrderRequest {
    private int userId;
    private BigDecimal totalAmount;
    private String paymentMethod;

    public CreateOrderRequest(int userId, BigDecimal totalAmount, String paymentMethod) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
