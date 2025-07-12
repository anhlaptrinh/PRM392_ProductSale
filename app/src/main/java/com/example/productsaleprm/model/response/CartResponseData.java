package com.example.productsaleprm.model.response;

import com.example.productsaleprm.model.CartItem;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class CartResponseData {
    @SerializedName("cartItem")
    private List<CartItem> cartItems;
    @SerializedName("total")
    private BigDecimal totalAmount;
    private int page;
    private int totalPages;
    private long totalItems;
    private boolean isLast;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public List<CartItem> getCartItems() { return cartItems; }
    public void setCartItems(List<CartItem> cartItems) { this.cartItems = cartItems; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}
