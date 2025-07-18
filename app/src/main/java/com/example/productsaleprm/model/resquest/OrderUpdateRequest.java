package com.example.productsaleprm.model.resquest;

public class OrderUpdateRequest {
    private int OrderID;

    public int getOrderID() {
        return OrderID;
    }

    public OrderUpdateRequest(int orderID) {
        OrderID = orderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }
}
