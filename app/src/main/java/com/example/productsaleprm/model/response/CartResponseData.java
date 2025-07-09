package com.example.productsaleprm.model.response;

import com.example.productsaleprm.model.CartItem;

import java.math.BigDecimal;
import java.util.List;

public class CartResponseData {
    private List<CartItem> cartItems;
    private BigDecimal totalAmount;

    public List<CartItem> getCartItems() { return cartItems; }
    public void setCartItems(List<CartItem> cartItems) { this.cartItems = cartItems; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}
