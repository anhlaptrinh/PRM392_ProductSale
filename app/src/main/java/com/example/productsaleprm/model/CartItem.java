package com.example.productsaleprm.model;

import com.example.productsaleprm.model.response.CartProductResponse;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class CartItem {
    private int id;
    private int quantity;
    private BigDecimal price;
    private CartProductResponse product;

    public CartItem(int id, int quantity, BigDecimal price, CartProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public CartProductResponse getProduct() {
        return product;
    }

    public void setProduct(CartProductResponse product) {
        this.product = product;
    }

    public void increaseQuantity() { this.quantity++; }
    public void decreaseQuantity() { if (this.quantity > 1) this.quantity--; }
}
