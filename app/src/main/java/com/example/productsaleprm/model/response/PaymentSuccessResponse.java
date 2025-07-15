package com.example.productsaleprm.model.response;

import java.math.BigDecimal;

public class PaymentSuccessResponse {
    private int orderID;
    private BigDecimal totalAmount;
    private String paymentDate;  // Lưu dạng String để dễ parse, hoặc dùng LocalDateTime với converter
    private String paymentStatus;

    public int getOrderID() {
        return orderID;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }
}
