package com.example.productsaleprm.model;

import java.math.BigDecimal;

public class Order {
    private int id;
    private String date;
    private BigDecimal total;
    private boolean shipped;
    private boolean arrived;

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


    public boolean isShipped() { return shipped; }
    public void setShipped(boolean shipped) { this.shipped = shipped; }

    public boolean isArrived() { return arrived; }
    public void setArrived(boolean arrived) { this.arrived = arrived; }
}

