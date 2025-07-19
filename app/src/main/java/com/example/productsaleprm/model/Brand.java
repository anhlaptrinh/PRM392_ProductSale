package com.example.productsaleprm.model;

public class Brand {
    private String name;
    private String imageUrl; // Dùng URL thay vì resource ID

    public Brand(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
