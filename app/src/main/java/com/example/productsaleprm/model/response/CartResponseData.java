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

    public List<CartItem> getCartItems() { return cartItems; }
    public void setCartItems(List<CartItem> cartItems) { this.cartItems = cartItems; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}
