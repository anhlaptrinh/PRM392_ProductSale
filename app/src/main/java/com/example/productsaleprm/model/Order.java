package com.example.productsaleprm.model;

import java.math.BigDecimal;

public class Order {
    private int id;
    private String date;
    private BigDecimal total;
    private String status;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getter/Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }


}

