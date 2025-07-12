package com.example.productsaleprm.model;

import java.math.BigDecimal;

public class WishList {
    private int id;
    private int productId;
    private String productName;
    private String briDesc;
    private String imageURL;
    private BigDecimal price;

    public WishList(int id, int productId, String productName, String briDesc, String imageURL, BigDecimal price) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.briDesc = briDesc;
        this.imageURL = imageURL;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBriDesc() {
        return briDesc;
    }

    public void setBriDesc(String briDesc) {
        this.briDesc = briDesc;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
