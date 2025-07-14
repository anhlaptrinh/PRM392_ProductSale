package com.example.productsaleprm.model;

import java.math.BigDecimal;

public class Product {
    private int id;
    private String productName;
    private String briDesc;
    private String fullDesc;
    private String technic;
    private BigDecimal price;
    private String imageURL;
    private int  categoryID;
    private String categoryName;
    private boolean isFavorite;

    public Product(int id, String productName, String briDesc, String fullDesc, String technic, BigDecimal price, String imageURL, int categoryID, String categoryName,boolean isFavorite) {
        this.id = id;
        this.productName = productName;
        this.briDesc = briDesc;
        this.fullDesc = fullDesc;
        this.technic = technic;
        this.price = price;
        this.imageURL = imageURL;
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.isFavorite = isFavorite;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setBriDesc(String briDesc) {
        this.briDesc = briDesc;
    }

    public void setFullDesc(String fullDesc) {
        this.fullDesc = fullDesc;
    }

    public void setTechnic(String technic) {
        this.technic = technic;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getBriDesc() {
        return briDesc;
    }

    public String getFullDesc() {
        return fullDesc;
    }

    public String getTechnic() {
        return technic;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageURL() {
        return imageURL;
    }
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }
}
