package com.example.productsaleprm.model;

import java.math.BigDecimal;

public class CartItem {
    private String name;
    private String imageUrl;
    private int quantity;
    private BigDecimal price;

    public CartItem(String name, String imageUrl, int quantity, BigDecimal price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.price = price;
    }

    public CartItem() {

    }

    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public int getQuantity() { return quantity; }
    public BigDecimal getPrice() { return price; }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void increaseQuantity() { this.quantity++; }
    public void decreaseQuantity() { if (this.quantity > 1) this.quantity--; }
}
