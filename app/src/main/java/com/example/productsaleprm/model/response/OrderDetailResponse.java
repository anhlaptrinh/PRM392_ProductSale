package com.example.productsaleprm.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDetailResponse {

    private int orderId;
    private String orderStatus;
    private String pmMethod;
    private String bill;
    private String orderDate;

    private String address;
    private BigDecimal total;

    // Constructors

    public OrderDetailResponse(int orderId, String orderStatus, String pmMethod, String bill, String orderDate, String address, BigDecimal total) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.pmMethod = pmMethod;
        this.bill = bill;
        this.orderDate = orderDate;
        this.address = address;
        this.total = total;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPmMethod() {
        return pmMethod;
    }

    public void setPmMethod(String pmMethod) {
        this.pmMethod = pmMethod;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
