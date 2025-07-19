package com.example.productsaleprm.model.resquest;

import java.util.List;

public class ReorderRequest {
    private List<Item> items;

    public ReorderRequest(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        private int productId;
        private int quantity;

        public Item(int productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public int getProductId() {
            return productId;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    public List<Item> getItems() {
        return items;
    }
}
