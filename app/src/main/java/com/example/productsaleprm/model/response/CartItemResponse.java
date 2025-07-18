package com.example.productsaleprm.model.response;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class CartItemResponse {
    private int id;
    private int quantity;
    private BigDecimal price;

    @SerializedName("productId")
    private int productId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("imageURL")
    private String imageURL;

    // Getters & Setters

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
